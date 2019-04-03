package it.bz.beacon.beaconsuedtirolsdk.result;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

import java.util.UUID;

public class IBeacon implements IBeaconInfo {

    private UUID uuid;
    private int major;
    private int minor;
    private Beacon info;

    public IBeacon(UUID uuid, int major, int minor, Beacon info) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.info = info;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    @Override
    public Beacon getInfo() {
        return info;
    }
}
