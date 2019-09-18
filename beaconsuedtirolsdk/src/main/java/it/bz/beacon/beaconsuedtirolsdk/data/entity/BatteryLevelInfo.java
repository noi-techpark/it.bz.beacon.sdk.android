package it.bz.beacon.beaconsuedtirolsdk.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import it.bz.beacon.beaconsuedtirolsdk.data.converter.DateConverter;

@Entity
@TypeConverters(DateConverter.class)
public class BatteryLevelInfo {

    @NonNull
    @PrimaryKey
    private String id;
    private Integer batteryLevel;
    private Date lastSent;
    private Date lastUpdated;

    public BatteryLevelInfo() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Date getLastSent() {
        return lastSent;
    }

    public void setLastSent(Date lastSent) {
        this.lastSent = lastSent;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
