package it.bz.beacon.beaconsuedtirolsdk.exception;

public class MissingLocationPermissionException extends Exception {

    public MissingLocationPermissionException() {
    }

    public MissingLocationPermissionException(Throwable throwable) {
        super(throwable);
    }

    public MissingLocationPermissionException(String message) {
        super(message);
    }
}
