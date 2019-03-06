package it.bz.beacon.beaconsuedtirolsdk.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Info;

@Dao
public abstract class InfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Info info);

    @Transaction
    @Query("SELECT * FROM Info ORDER BY name ASC")
    public abstract LiveData<List<Info>> getAll();

    @Query("SELECT * FROM Info WHERE id = :id")
    public abstract LiveData<Info> getByIdLive(String id);

    @Query("SELECT * FROM Info WHERE id = :id")
    public abstract Info getById(String id);
}
