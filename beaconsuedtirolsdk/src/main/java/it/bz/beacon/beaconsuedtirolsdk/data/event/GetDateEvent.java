package it.bz.beacon.beaconsuedtirolsdk.data.event;

public interface GetDateEvent {
    void onSuccess(long date);

    void onError();
}
