package it.bz.beacon.beaconsuedtirolsdk.listener;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeaconExtended;

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
     * Called when a iBeacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     * This listener also dispatch distance and Proximity information.
     *
     * @param iBeacon the iBeacon
     */
    void onIBeaconDiscovered(IBeaconExtended iBeacon);

     /**
     * Called when a List<iBeacon> is updated with new information.
     * This listener also dispatch distance and Proximity data about every devices.
     *
     * @param iBeacons the list of iBeacon
     */
     void onIBeaconUpdated(List<IBeaconExtended> iBeacons);

    /**
     * Called when a iBeacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param iBeacon the iBeacon
     */
    void onIBeaconLost(IBeacon iBeacon);
}
