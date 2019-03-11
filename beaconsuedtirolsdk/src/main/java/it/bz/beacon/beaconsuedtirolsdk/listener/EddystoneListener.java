package it.bz.beacon.beaconsuedtirolsdk.listener;

import java.util.List;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

/**
 * Listener used to report Eddystone scanning results.
 */
public interface EddystoneListener {

    /**
     * Called when a Eddystone beacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onEddystoneDiscovered(Beacon beacon);

    /**
     * Called when a Eddystone beacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onEddystoneLost(Beacon beacon);
}
