package it.bz.beacon.sdkandroid;

import android.app.Application;
import it.bz.beacon.beaconsuedtirolsdk.NearbyBeaconManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NearbyBeaconManager.initialize(this);
    }
}
