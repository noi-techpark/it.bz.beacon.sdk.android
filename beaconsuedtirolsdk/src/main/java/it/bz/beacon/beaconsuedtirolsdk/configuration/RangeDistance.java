// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

package it.bz.beacon.beaconsuedtirolsdk.configuration;

import com.kontakt.sdk.android.common.Proximity;

public enum RangeDistance {
    /**
     * Android device distance from Beacon is within [0 - 0,5]m.
     */
    immediate,
    /**
     * Android device distance from Beacon is within [0,5 - 3]m.
     */
    near,
    /**
     * Android device distance from Beacon is higher than 3m.
     */
    far,
    /**
     * The UNKNOWN.
     */
    unknown;

    /**
     * Categorizes Proximity to RangeDistance.
     *
     * @param proximity
     * @return the rangeDistance
     */
    public static RangeDistance fromProximity(final Proximity proximity) {
        switch (proximity) {
            case IMMEDIATE:
                return immediate;
            case NEAR:
                return near;
            case FAR:
                return far;
            case UNKNOWN:
            default:
                return unknown;
        }
    }
}
