package it.bz.beacon.beaconsuedtirolsdk.data.event;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public interface LoadBeaconEvent {
    void onSuccess(Beacon beacon);

    void onError();
}
