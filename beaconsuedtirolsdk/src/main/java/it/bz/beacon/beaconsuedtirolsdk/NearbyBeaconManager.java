package it.bz.beacon.beaconsuedtirolsdk;

import android.content.Context;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.SecureProfileListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleSecureProfileListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.ISecureProfile;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;
import it.bz.beacon.beaconsuedtirolsdk.listener.BeaconListener;

public class NearbyBeaconManager {

    private com.kontakt.sdk.android.ble.manager.ProximityManager proximityManager;
    private BeaconRepository repository;
    private BeaconListener beaconListener;

    public NearbyBeaconManager(Context context, BeaconListener beaconListener) {
        this.beaconListener = beaconListener;
        repository = new BeaconRepository(context);
        KontaktSDK.initialize(context.getString(R.string.app_name));
        proximityManager = ProximityManagerFactory.create(context);
        proximityManager.setSecureProfileListener(createSecureProfileListener());
    }

    /**
     * Starts scanning for nearby beacons.
     */
    public void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    /**
     * Stops scanning.
     */
    public void stopScanning() {
        proximityManager.stopScanning();
        proximityManager.disconnect();
    }

    private SecureProfileListener createSecureProfileListener() {
        return new SimpleSecureProfileListener() {
            @Override
            public void onProfileDiscovered(final ISecureProfile profile) {
                repository.getByInstanceId(profile.getUniqueId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (beaconListener != null) {
                            beaconListener.onBeaconDiscovered(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onProfileDiscovered(profile);
            }

            @Override
            public void onProfilesUpdated(List<ISecureProfile> profiles) {
                for (ISecureProfile profile : profiles) {
                    repository.getByInstanceId(profile.getUniqueId(), new LoadBeaconEvent() {
                        @Override
                        public void onSuccess(Beacon beacon) {

                        }

                        @Override
                        public void onSuccess(List<Beacon> beacons) {
                            if (beaconListener != null) {
                                beaconListener.onBeaconUpdated(beacons);
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
                }
                super.onProfilesUpdated(profiles);
            }

            @Override
            public void onProfileLost(ISecureProfile profile) {
                repository.getByInstanceId(profile.getUniqueId(), new LoadBeaconEvent() {
                    @Override
                    public void onSuccess(Beacon beacon) {
                        if (beaconListener != null) {
                            beaconListener.onBeaconLost(beacon);
                        }
                    }

                    @Override
                    public void onSuccess(List<Beacon> beacons) {

                    }

                    @Override
                    public void onError() {
                    }
                });
                super.onProfileLost(profile);
            }
        };
    }
}
