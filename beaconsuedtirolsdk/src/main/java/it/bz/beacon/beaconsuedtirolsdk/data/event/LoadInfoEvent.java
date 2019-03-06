package it.bz.beacon.beaconsuedtirolsdk.data.event;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Info;

public interface LoadInfoEvent {
    void onSuccess(Info info);

    void onError();
}
