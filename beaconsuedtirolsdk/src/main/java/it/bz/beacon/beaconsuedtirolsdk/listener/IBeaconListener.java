package it.bz.beacon.beaconsuedtirolsdk.listener;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

/**
 * Listener used to report iBeacons scanning results.
 */
public interface IBeaconListener {

    /**
     * Called when a iBeacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onIBeaconDiscovered(Beacon beacon);

    /**
     * Called when a iBeacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onIBeaconLost(Beacon beacon);
}
