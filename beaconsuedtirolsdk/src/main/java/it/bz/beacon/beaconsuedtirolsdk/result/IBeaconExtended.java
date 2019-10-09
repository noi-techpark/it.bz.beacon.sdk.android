package it.bz.beacon.beaconsuedtirolsdk.result;

import java.util.UUID;

import it.bz.beacon.beaconsuedtirolsdk.configuration.RangeDistance;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

public class IBeaconExtended extends IBeacon {

    private double distance;
    private RangeDistance rangeDistance;

    public IBeaconExtended(UUID uuid, int major, int minor, Beacon info, double distance, RangeDistance rangeDistance) {
        super(uuid, major, minor, info);
        this.distance = distance;
        this.rangeDistance = rangeDistance;
    }

    public double getDistance() {
        return distance;
    }

    public RangeDistance getRangeDistance() {
        return rangeDistance;
    }
}
