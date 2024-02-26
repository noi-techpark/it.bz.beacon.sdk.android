// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.beaconsuedtirolsdk.data.event;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public interface LoadAllBeaconsEvent {
    void onSuccess(List<Beacon> beacons);

    void onError();
}
