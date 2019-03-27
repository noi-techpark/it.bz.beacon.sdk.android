package it.bz.beacon.beaconsuedtirolsdk.listener;

import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;

/**
 * Listener used to report iBeacons scanning results.
 */
public interface IBeaconListener {

    /**
     * Called when a iBeacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     *
     * @param iBeacon the iBeacon
     */
    void onIBeaconDiscovered(IBeacon iBeacon);

    /**
     * Called when a iBeacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param iBeacon the iBeacon
     */
    void onIBeaconLost(IBeacon iBeacon);
}
