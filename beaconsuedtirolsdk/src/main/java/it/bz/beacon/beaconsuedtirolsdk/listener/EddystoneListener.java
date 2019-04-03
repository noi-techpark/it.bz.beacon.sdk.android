package it.bz.beacon.beaconsuedtirolsdk.listener;

import it.bz.beacon.beaconsuedtirolsdk.result.Eddystone;

/**
 * Listener used to report Eddystone scanning results.
 */
public interface EddystoneListener {

    /**
     * Called when a Eddystone beacon is discovered for the first time.
     * This will be called only once per scan or after beacon is reported lost.
     *
     * @param eddystone the eddystone
     */
    void onEddystoneDiscovered(Eddystone eddystone);

    /**
     * Called when a Eddystone beacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param eddystone the eddystone
     */
    void onEddystoneLost(Eddystone eddystone);
}
