package it.bz.beacon.beaconsuedtirolsdk.listener;

import com.kontakt.sdk.android.common.profile.IEddystoneDevice;

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
     * @param eddystone the original Eddystone package from the discovered beacon
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onEddystoneDiscovered(IEddystoneDevice eddystone, Beacon beacon);

    /**
     * Called when a Eddystone beacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param eddystone the original Eddystone package from the discovered beacon
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onEddystoneLost(IEddystoneDevice eddystone, Beacon beacon);
}
