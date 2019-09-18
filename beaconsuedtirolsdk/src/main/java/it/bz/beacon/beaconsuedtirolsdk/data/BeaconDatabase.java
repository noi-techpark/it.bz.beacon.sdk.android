package it.bz.beacon.beaconsuedtirolsdk.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import it.bz.beacon.beaconsuedtirolsdk.data.dao.BatteryLevelInfoDao;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BeaconDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.BatteryLevelInfo;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

@Database(
        entities = {
                Beacon.class,
                BatteryLevelInfo.class
        },
        version = 3, exportSchema = true)

public abstract class BeaconDatabase extends RoomDatabase {

    private static BeaconDatabase INSTANCE;
    public static String DB_NAME = "info_db";

    public abstract BeaconDao beaconDao();
    public abstract BatteryLevelInfoDao batteryLevelInfoDao();

    public static BeaconDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BeaconDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BeaconDatabase.class, DB_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Beacon ADD COLUMN updatedAt INTEGER");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `BatteryLevelInfo` (`id` TEXT NOT NULL, `batteryLevel` INTEGER, `lastSent` INTEGER, `lastUpdated` INTEGER, PRIMARY KEY(`id`))");
        }
    };
}
