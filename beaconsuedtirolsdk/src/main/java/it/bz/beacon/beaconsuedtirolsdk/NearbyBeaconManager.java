package it.bz.beacon.beaconsuedtirolsdk;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.device.EddystoneNamespace;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadAllBeaconsEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.listener.EddystoneListener;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.workmanager.SynchronizationWorker;

public class NearbyBeaconManager {

    private com.kontakt.sdk.android.ble.manager.ProximityManager proximityManager;
    private BeaconRepository repository;
    private EddystoneListener eddystoneListener;
    private IBeaconListener iBeaconListener;
    private Context context;

    public NearbyBeaconManager(Context context) {
        this.context = context;
        repository = new BeaconRepository(context);
        KontaktSDK.initialize(" ");
        proximityManager = ProximityManagerFactory.create(context);
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
                proximityManager.startScanning();
            }
        });
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
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
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
