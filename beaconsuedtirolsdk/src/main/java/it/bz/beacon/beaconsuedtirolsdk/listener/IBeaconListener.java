package it.bz.beacon.beaconsuedtirolsdk.listener;

import com.kontakt.sdk.android.common.profile.IBeaconDevice;

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
     * @param ibeacon the original IBeaconDevice package from the discovered beacon
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onIBeaconDiscovered(IBeaconDevice ibeacon, Beacon beacon);

    /**
     * Called when a iBeacon gets out of range.
     * {@code onBeaconDiscovered} will be called when beacon is back in range.
     *
     * @param ibeacon the original IBeaconDevice package from the discovered beacon
     * @param beacon the Beacon Südtirol SDK beacon
     */
    void onIBeaconLost(IBeaconDevice ibeacon, Beacon beacon);
}
