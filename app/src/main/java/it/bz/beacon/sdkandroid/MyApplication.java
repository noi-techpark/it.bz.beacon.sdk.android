// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.sdkandroid;

import android.app.Application;
import it.bz.beacon.beaconsuedtirolsdk.NearbyBeaconManager;
import it.bz.beacon.beaconsuedtirolsdk.auth.TrustedAuth;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NearbyBeaconManager.initialize(this, new TrustedAuth("user", "unauthorized"));
    }
}
