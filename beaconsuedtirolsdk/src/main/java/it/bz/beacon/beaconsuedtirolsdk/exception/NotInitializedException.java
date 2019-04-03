package it.bz.beacon.beaconsuedtirolsdk.exception;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException() {
        super("The Beacon SDK has not been initialized yet. Make sure to call initialize() once before using it.");
    }
}
