// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.beaconsuedtirolsdk.data.event;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public interface LoadBeaconEvent {
    void onSuccess(Beacon beacon);

    void onError();
}
