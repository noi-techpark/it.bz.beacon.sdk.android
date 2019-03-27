package it.bz.beacon.beaconsuedtirolsdk.exception;

public class AlreadyInitializedException extends RuntimeException {
    public AlreadyInitializedException() {
        super("The Beacon SDK has already been initialized.");
    }
}
