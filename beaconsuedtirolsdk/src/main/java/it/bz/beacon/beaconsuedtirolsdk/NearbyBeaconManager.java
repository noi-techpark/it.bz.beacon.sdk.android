package it.bz.beacon.beaconsuedtirolsdk;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.List;

import androidx.core.content.ContextCompat;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.listener.EddystoneListener;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;

public class NearbyBeaconManager {

    private final static String LOG_TAG = "Beacon SDK";

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
    }

    public void setIBeaconListener(IBeaconListener iBeaconListener) {
        this.iBeaconListener = iBeaconListener;
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    public void setEddystoneListener(EddystoneListener eddystoneListener) {
        this.eddystoneListener = eddystoneListener;
        // TODO: set namespace
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

    private com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                repository.getByMajorMinor(ibeacon.getMajor(), ibeacon.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconDiscovered(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onIBeaconDiscovered(ibeacon, region);
            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                repository.getByMajorMinor(ibeacon.getMajor(), ibeacon.getMinor(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (iBeaconListener != null) {
                            iBeaconListener.onIBeaconLost(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onIBeaconLost(ibeacon, region);
            }
        };
    }

    private com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                repository.getByInstanceId(eddystone.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneDiscovered(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onEddystoneDiscovered(eddystone, namespace);
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                repository.getByInstanceId(eddystone.getInstanceId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (eddystoneListener != null) {
                            eddystoneListener.onEddystoneLost(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onEddystoneLost(eddystone, namespace);
            }
        };
    }
}
