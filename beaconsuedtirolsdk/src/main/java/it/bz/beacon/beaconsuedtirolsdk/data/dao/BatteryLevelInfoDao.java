package it.bz.beacon.beaconsuedtirolsdk.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import it.bz.beacon.beaconsuedtirolsdk.data.entity.BatteryLevelInfo;

@Dao
public abstract class BatteryLevelInfoDao {

    @Insert()
    public abstract void insert(BatteryLevelInfo batteryLevelInfo);

    @Query("UPDATE BatteryLevelInfo SET batteryLevel = :batteryLevel, lastUpdated = :timestamp WHERE id = :id")
    public abstract int update(String id, int batteryLevel, long timestamp);

    @Query("UPDATE BatteryLevelInfo SET lastSent = :timestamp WHERE id = :id")
    public abstract void updateLastSent(String id, long timestamp);

    @Query("SELECT * FROM BatteryLevelInfo WHERE id = :id")
    public abstract BatteryLevelInfo getById(String id);
}
