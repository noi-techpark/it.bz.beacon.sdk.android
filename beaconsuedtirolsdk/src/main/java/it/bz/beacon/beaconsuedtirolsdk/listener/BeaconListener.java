package it.bz.beacon.beaconsuedtirolsdk.listener;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

/**
 * Listener used to report beacons scanning results.
 */
public interface BeaconListener {

    /**
     * Called when a beacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onBeaconDiscovered(Beacon beacon);

    /**
     * Called when a beacon is updated.
     *
     * @param beacons the Beacon Südtirol SDK beacons
     */
    void onBeaconUpdated(List<Beacon> beacons);

    /**
     * Called when a beacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onBeaconLost(Beacon beacon);
}
