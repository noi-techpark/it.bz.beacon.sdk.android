package it.bz.beacon.beaconsuedtirolsdk.data.event;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public interface LoadAllBeaconsEvent {
    void onSuccess(List<Beacon> beacons);

    void onError();
}
