package it.bz.beacon.beaconsuedtirolsdk;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.device.EddystoneNamespace;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.SecureProfileListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.ISecureProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import it.bz.beacon.beaconsuedtirolsdk.auth.TrustedAuth;
import it.bz.beacon.beaconsuedtirolsdk.configuration.BluetoothMode;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadAllBeaconsEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BatteryLevelInfoRepository;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;
import it.bz.beacon.beaconsuedtirolsdk.exception.AlreadyInitializedException;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NotInitializedException;
import it.bz.beacon.beaconsuedtirolsdk.listener.EddystoneListener;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.result.Eddystone;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeaconExtended;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiClient;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.TrustedBeaconControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.workmanager.SynchronizationWorker;

import static it.bz.beacon.beaconsuedtirolsdk.configuration.RangeDistance.fromProximity;

public class NearbyBeaconManager implements SecureProfileListener {

    private static volatile NearbyBeaconManager instance;

    private com.kontakt.sdk.android.ble.manager.ProximityManager proximityManager;
    private BeaconRepository beaconRepository;
    private BatteryLevelInfoRepository batteryLevelInfoRepository;
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
        beaconRepository = new BeaconRepository(application);
        batteryLevelInfoRepository = new BatteryLevelInfoRepository(application, trustedAuth);
        KontaktSDK.initialize(" ");
        proximityManager = ProximityManagerFactory.create(application);
        trustedApi = new TrustedBeaconControllerApi(new ApiClient());
        if (this.trustedAuth != null) {
            trustedApi.getApiClient().setUsername(this.trustedAuth.getUsername());
            trustedApi.getApiClient().setPassword(this.trustedAuth.getPassword());
        } else {
            trustedApi.getApiClient().setUsername(null);
            trustedApi.getApiClient().setPassword(null);
        }
        createPeriodicWorkRequest(application);
    }

    private void createPeriodicWorkRequest(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(SynchronizationWorker.class, 30, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueue(saveRequest);
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

    /**
     * Returns true if scan is currently in progress
     */
    public boolean isScanning() {
        return proximityManager.isScanning();
    }

    @Override
    public void onProfileDiscovered(ISecureProfile profile) {
        updateBatteryStatus(profile);
    }

    @Override
    public void onProfilesUpdated(List<ISecureProfile> profiles) {

    }

    @Override
    public void onProfileLost(ISecureProfile profile) {

    }

    private void updateBatteryStatus(ISecureProfile profile) {
        if (profile.getBatteryLevel() > 0) {
            String[] nameParts = profile.getName().split("#");
            if (nameParts.length > 1) {
                batteryLevelInfoRepository.update(nameParts[1], profile.getBatteryLevel());
            }
        }
    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        } else {
            return bluetoothAdapter.isEnabled();
        }
    }

    private boolean isLocationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Configure BluetoothMode for PromximityManager
     */
    public void configureScanMode(BluetoothMode bluetoothMode) {
        proximityManager.configuration()
                .scanMode(convertScanMode(bluetoothMode));
    }

    /**
     * Configure update callback interval
     */
    public void setDeviceupdateCallbackInterval(int seconds) {
        proximityManager.configuration().deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(seconds));
    }


    private ScanMode convertScanMode(BluetoothMode bluetoothMode) {
        switch (bluetoothMode) {
            case low_power:
                return ScanMode.LOW_POWER;
            default:
            case balanced:
                return ScanMode.BALANCED;
            case low_latency:
                return ScanMode.LOW_LATENCY;
        }
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
     * @param loadAllBeaconsEvent
     */
    public void getAllBeacons(LoadAllBeaconsEvent loadAllBeaconsEvent) {
        beaconRepository.getAll(loadAllBeaconsEvent);
    }

    /**
     * Get a single beacon from local cache
     * @param id              The id of the beacon to load from cache
     * @param loadBeaconEvent
     */
    public void getBeacon(String id, LoadBeaconEvent loadBeaconEvent) {
        beaconRepository.getById(id, loadBeaconEvent);
    }

    private com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(final IBeaconDevice device, final IBeaconRegion region) {
                super.onIBeaconDiscovered(device, region);
                beaconRepository.getByMajorMinor(device.getMajor(), device.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(new IBeaconExtended(device.getProximityUUID(),
                                    device.getMajor(), device.getMinor(), beacon, device.getDistance(), fromProximity(device.getProximity())));
                        }
                    }

                    @Override
                    public void onError() {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(new IBeacon(device.getProximityUUID(),
                                    device.getMajor(), device.getMinor(), null));
                        }
                    }
                });

            }

            @Override
            public void onIBeaconsUpdated(final List<IBeaconDevice> devices, IBeaconRegion region) {
                super.onIBeaconsUpdated(devices, region);

                beaconRepository.getAll(new LoadAllBeaconsEvent() {
                    @Override
                    public void onSuccess(List<Beacon> beacons) {
                        List<IBeaconExtended> updated = new ArrayList<>();

                        for (IBeaconDevice d : devices) {
                            updated.add(new IBeaconExtended(d.getProximityUUID(), d.getMajor(), d.getMinor(), findUsingEnhancedForLoop(d.getMajor(), d.getMinor(), beacons), d.getDistance(), fromProximity(d.getProximity())));
                        }

                        iBeaconListener.onIBeaconUpdated(updated);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            private Beacon findUsingEnhancedForLoop(int major, int minor, List<Beacon> beacons) {
                for (Beacon beacon : beacons) {
                    if (beacon.getMajor() == major && beacon.getMinor() == minor) {
                        return beacon;
                    }
                }
                return null;
            }

            @Override
            public void onIBeaconLost(final IBeaconDevice device, final IBeaconRegion region) {
                super.onIBeaconLost(device, region);
                beaconRepository.getByMajorMinor(device.getMajor(), device.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconLost(new IBeacon(device.getProximityUUID(),
                                    device.getMajor(), device.getMinor(), beacon));
                        }
                    }

                    @Override
                    public void onError() {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(new IBeacon(device.getProximityUUID(),
                                    device.getMajor(), device.getMinor(), null));
                        }
                    }
                });
            }
        };
    }

    private com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(final IEddystoneDevice device, final IEddystoneNamespace namespace) {
                super.onEddystoneDiscovered(device, namespace);
                beaconRepository.getByInstanceId(device.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(new Eddystone(device.getNamespace(),
                                    device.getInstanceId(), device.getUrl(), device.getEid(),
                                    device.getEncryptedTelemetry(), device.getTelemetry(), beacon));
                        }
                    }

                    @Override
                    public void onError() {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(new Eddystone(device.getNamespace(),
                                    device.getInstanceId(), device.getUrl(), device.getEid(),
                                    device.getEncryptedTelemetry(), device.getTelemetry(), null));
                        }
                    }
                });
            }

            @Override
            public void onEddystoneLost(final IEddystoneDevice device, final IEddystoneNamespace namespace) {
                super.onEddystoneLost(device, namespace);
                beaconRepository.getByInstanceId(device.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneLost(new Eddystone(device.getNamespace(),
                                    device.getInstanceId(), device.getUrl(), device.getEid(),
                                    device.getEncryptedTelemetry(), device.getTelemetry(), beacon));
                        }
                    }

                    @Override
                    public void onError() {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(new Eddystone(device.getNamespace(),
                                    device.getInstanceId(), device.getUrl(), device.getEid(),
                                    device.getEncryptedTelemetry(), device.getTelemetry(), null));
                        }
                    }
                });
            }
        };
    }
}
