package it.bz.beacon.beaconsuedtirolsdk;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.device.EddystoneNamespace;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.SecureProfileListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.*;
import it.bz.beacon.beaconsuedtirolsdk.auth.TrustedAuth;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadAllBeaconsEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;
import it.bz.beacon.beaconsuedtirolsdk.exception.AlreadyInitializedException;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NotInitializedException;
import it.bz.beacon.beaconsuedtirolsdk.listener.EddystoneListener;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiCallback;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiException;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.TrustedBeaconControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BeaconBatteryLevelUpdate;
import it.bz.beacon.beaconsuedtirolsdk.workmanager.SynchronizationWorker;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NearbyBeaconManager implements SecureProfileListener {

    private static volatile NearbyBeaconManager instance;

    private com.kontakt.sdk.android.ble.manager.ProximityManager proximityManager;
    private BeaconRepository repository;
    private EddystoneListener eddystoneListener;
    private IBeaconListener iBeaconListener;
    private Application application;
    private TrustedBeaconControllerApi trustedApi;
    private TrustedAuth trustedAuth;

    public static void initialize(@NonNull Application application) {
        initialize(application, null);
    }

    public static void initialize(@NonNull Application application, @Nullable TrustedAuth auth) {
        if (instance != null) {
            throw new AlreadyInitializedException();
        }

        instance = new NearbyBeaconManager(application, auth);
    }

    public static NearbyBeaconManager getInstance() {
        if (instance == null) {
            throw new NotInitializedException();
        }

        return instance;
    }

    private NearbyBeaconManager(@NonNull Application application, @Nullable TrustedAuth trustedAuth) {
        this.application = application;
        this.trustedAuth = trustedAuth;
        repository = new BeaconRepository(application);
        KontaktSDK.initialize(" ");
        proximityManager = ProximityManagerFactory.create(application);
        trustedApi = new TrustedBeaconControllerApi();
        createPeriodicWorkRequest();
    }

    private void createPeriodicWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(SynchronizationWorker.class, 20, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance().enqueue(saveRequest);
    }

    public void setIBeaconListener(IBeaconListener iBeaconListener) {
        this.iBeaconListener = iBeaconListener;
        Collection<IBeaconRegion> iBeaconRegions = new ArrayList<>();
        IBeaconRegion region = new BeaconRegion.Builder()
                .identifier("iBeacon Südtirol")
                .proximity(UUID.fromString("6a84c716-0f2a-1ce9-f210-6a63bd873dd9"))
                .build();
        iBeaconRegions.add(region);
        proximityManager.spaces().iBeaconRegions(iBeaconRegions);
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    public void setEddystoneListener(EddystoneListener eddystoneListener) {
        this.eddystoneListener = eddystoneListener;
        Collection<IEddystoneNamespace> eddystoneNamespaces = new ArrayList<>();
        IEddystoneNamespace namespace = new EddystoneNamespace.Builder()
                .identifier("Eddystone Südtirol")
                .namespace("6a84c7166a63bd873dd9")
                .build();
        eddystoneNamespaces.add(namespace);
        proximityManager.spaces().eddystoneNamespaces(eddystoneNamespaces);
        proximityManager.setEddystoneListener(createEddystoneListener());
    }

    /**
     * Starts scanning for nearby beacons if Bluetooth is enabled and Location permission granted
     */
    public void startScanning() throws NoBluetoothException, MissingLocationPermissionException {
        if (!isBluetoothEnabled()) {
            throw new NoBluetoothException("Bluetooth is deactivated or not available on this device.");
        }
        if (!isLocationPermissionGranted()) {
            throw new MissingLocationPermissionException("Permission ACCESS_FINE_LOCATION not granted.");
        }
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                if (trustedAuth != null) {
                    proximityManager.setSecureProfileListener(NearbyBeaconManager.this);
                }
                proximityManager.startScanning();
            }
        });
    }

    @Override
    public void onProfileDiscovered(ISecureProfile profile) {
        if (trustedAuth != null) {
            try {
                BeaconBatteryLevelUpdate update = new BeaconBatteryLevelUpdate();
                update.setBatteryLevel(profile.getBatteryLevel());
                String[] nameParts = profile.getName().split("#");
                trustedApi.getApiClient().setUsername(trustedAuth.getUsername());
                trustedApi.getApiClient().setPassword(trustedAuth.getPassword());
                trustedApi.updateUsingPATCH2Async(update, nameParts[1], new ApiCallback<it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Beacon>() {
                    @Override
                    public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {

                    }

                    @Override
                    public void onSuccess(it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Beacon result, int statusCode, Map<String, List<String>> responseHeaders) {

                    }

                    @Override
                    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                    }

                    @Override
                    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                    }
                }).execute();
            } catch (Exception e) {
                //
            }
        }
    }

    @Override
    public void onProfilesUpdated(List<ISecureProfile> profiles) {

    }

    @Override
    public void onProfileLost(ISecureProfile profile) {

    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }
        else {
            return bluetoothAdapter.isEnabled();
        }
    }

    private boolean isLocationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Stops scanning.
     */
    public void stopScanning() {
        proximityManager.stopScanning();
        proximityManager.disconnect();
    }

    /**
     * Get all beacons from server, fall back to local cache if there is no internet connection
     *
     * @param loadAllBeaconsEvent
     */
    public void getAllBeacons(LoadAllBeaconsEvent loadAllBeaconsEvent) {
        repository.getAll(loadAllBeaconsEvent);
    }

    /**
     * Get a single beacon from local cache
     *
     * @param loadBeaconEvent
     */
    public void getBeacon(String id, LoadBeaconEvent loadBeaconEvent) {
        repository.getById(id, loadBeaconEvent);
    }

    private com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(final IBeaconDevice ibeacon, final IBeaconRegion region) {
                repository.getByMajorMinor(ibeacon.getMajor(), ibeacon.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(ibeacon, beacon);
                        }
                    }

                    @Override
                    public void onError() {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(ibeacon, null);
                        }
                    }
                });
                super.onIBeaconDiscovered(ibeacon, region);
            }

            @Override
            public void onIBeaconLost(final IBeaconDevice ibeacon, final IBeaconRegion region) {
                repository.getByMajorMinor(ibeacon.getMajor(), ibeacon.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconLost(ibeacon, beacon);
                        }
                    }

                    @Override
                    public void onError() {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(ibeacon, null);
                        }
                    }
                });
                super.onIBeaconLost(ibeacon, region);
            }
        };
    }

    private com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(final IEddystoneDevice eddystone, final IEddystoneNamespace namespace) {
                repository.getByInstanceId(eddystone.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(eddystone, beacon);
                        }
                    }

                    @Override
                    public void onError() {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(eddystone, null);
                        }
                    }
                });
                super.onEddystoneDiscovered(eddystone, namespace);
            }

            @Override
            public void onEddystoneLost(final IEddystoneDevice eddystone, final IEddystoneNamespace namespace) {
                repository.getByInstanceId(eddystone.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneLost(eddystone, beacon);
                        }
                    }

                    @Override
                    public void onError() {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(eddystone, null);
                        }
                    }
                });
                super.onEddystoneLost(eddystone, namespace);
            }
        };
    }
}
