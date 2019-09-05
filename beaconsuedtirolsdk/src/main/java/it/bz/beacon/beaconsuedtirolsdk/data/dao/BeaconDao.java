package it.bz.beacon.beaconsuedtirolsdk.data.dao;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

@Dao
public abstract class BeaconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Beacon beacon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMultiple(List<Beacon> beacons);

    @Transaction
    @Query("SELECT * FROM Beacon ORDER BY name ASC")
    public abstract List<Beacon> getAll();

    @Query("SELECT * FROM Beacon WHERE id = :id")
    public abstract Beacon getById(String id);

    @Query("SELECT * FROM Beacon WHERE major = :major AND minor = :minor")
    public abstract Beacon getByMajorMinor(int major, int minor);

    @Query("SELECT * FROM Beacon WHERE instanceId = :instanceId")
    public abstract Beacon getByInstanceId(String instanceId);

    @Query("SELECT MAX(updatedAt) FROM Beacon")
    public abstract long getMaxUpdatedAt();
}
