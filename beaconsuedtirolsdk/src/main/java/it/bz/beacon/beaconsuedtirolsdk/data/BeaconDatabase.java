package it.bz.beacon.beaconsuedtirolsdk.data;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BeaconDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

@Database(
        entities = {
                Beacon.class
        },
        version = 1, exportSchema = false)

public abstract class BeaconDatabase extends RoomDatabase {

    private static BeaconDatabase INSTANCE;
    public static String DB_NAME = "beacon_db";

    public abstract BeaconDao beaconDao();

    public static BeaconDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BeaconDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BeaconDatabase.class, DB_NAME)
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbTask(INSTANCE).execute();
                }
            };

    private static class PopulateDbTask extends AsyncTask<Void, Void, Void> {

        private final BeaconDao beaconDao;

        PopulateDbTask(BeaconDatabase db) {
            beaconDao = db.beaconDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            Random random = new Random();

            Beacon beacon = new Beacon();
            beacon.setId("ME123#FshHDf");
            beacon.setUuid(UUID.randomUUID().toString().replace("-", "").toUpperCase());
            beacon.setInstanceId("FshHDf");
            beacon.setName("Bozner Dom");
            beacon.setMajor(6553);
            beacon.setMinor(56);
            beacon.setLatitude(46.56f + (random.nextInt(10000) / 50000.0f));
            beacon.setLongitude(10.62f + (random.nextInt(14000) / 10000.0f));
            if (random.nextInt(2) == 1) {
                beacon.setLocation(Beacon.LOCATION_OUTDOOR);
            }
            else {
                beacon.setLocation(Beacon.LOCATION_INDOOR);
            }
            beaconDao.insert(beacon);

            beacon = new Beacon();
            beacon.setId("BZ456#Fsojta");
            beacon.setUuid(UUID.randomUUID().toString().replace("-", "").toUpperCase());
            beacon.setInstanceId("Fsojta");
            beacon.setName("Waltherplatz");
            beacon.setMajor(128);
            beacon.setMinor(24);
            beacon.setLatitude(46.56f + (random.nextInt(10000) / 50000.0f));
            beacon.setLongitude(10.62f + (random.nextInt(14000) / 10000.0f));
            if (random.nextInt(2) == 1) {
                beacon.setLocation(Beacon.LOCATION_OUTDOOR);
            }
            else {
                beacon.setLocation(Beacon.LOCATION_INDOOR);
            }
            beaconDao.insert(beacon);

            beacon = new Beacon();
            beacon.setId("BX987#ctG9jM");
            beacon.setUuid(UUID.randomUUID().toString().replace("-", "").toUpperCase());
            beacon.setInstanceId("ctG9jM");
            beacon.setName("Eiswelle");
            beacon.setMajor(31921);
            beacon.setMinor(47792);
            beacon.setLatitude(46.56f + (random.nextInt(10000) / 50000.0f));
            beacon.setLongitude(10.62f + (random.nextInt(14000) / 10000.0f));
            if (random.nextInt(2) == 1) {
                beacon.setLocation(Beacon.LOCATION_OUTDOOR);
            }
            else {
                beacon.setLocation(Beacon.LOCATION_INDOOR);
            }
            beaconDao.insert(beacon);

            return null;
        }
    }
}
