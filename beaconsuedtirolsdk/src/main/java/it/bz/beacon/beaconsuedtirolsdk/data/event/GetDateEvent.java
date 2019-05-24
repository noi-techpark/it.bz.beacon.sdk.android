package it.bz.beacon.beaconsuedtirolsdk.data.event;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public interface GetDateEvent {
    void onSuccess(long date);

    void onError();
}
