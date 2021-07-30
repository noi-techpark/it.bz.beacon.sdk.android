package it.bz.beacon.beaconsuedtirolsdk;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BatteryLevelInfoDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.BatteryLevelInfo;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.TrustedBeaconControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BeaconBatteryLevelUpdate;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("it.bz.beacon.beaconsuedtirolsdk.test", appContext.getPackageName());
    }

    @Test
    public void trustedTest() {

        Context context = InstrumentationRegistry.getTargetContext();

        BeaconDatabase db = BeaconDatabase.getDatabase(context);

        BatteryLevelInfoDao batteryLevelInfoDao = db.batteryLevelInfoDao();

        TrustedBeaconControllerApi trustedApi = new TrustedBeaconControllerApi();

        trustedApi.getInvoker().setUsername("admin");
        trustedApi.getInvoker().setPassword("xxx");

        String id = "rRzz5H"; //rRzz5H //enelB7
        Integer batteryLevel = Integer.valueOf(98);
        long lastSent;

        BatteryLevelInfo batteryLevelInfo = batteryLevelInfoDao.getById(id);

        if (batteryLevelInfo == null) {
            batteryLevelInfo = new BatteryLevelInfo();
            batteryLevelInfo.setId(id);
            batteryLevelInfo.setBatteryLevel(batteryLevel);
            batteryLevelInfo.setLastUpdated(new Date());
            batteryLevelInfo.setLastSent(null);
            batteryLevelInfoDao.insert(batteryLevelInfo);
        } else {
            if (batteryLevelInfo.getLastSent() != null) {
                lastSent = batteryLevelInfo.getLastSent().getTime();
            }
            batteryLevelInfoDao.update(id, batteryLevel, System.currentTimeMillis());
        }

        //if (lastSent < System.currentTimeMillis() - 24 * 60 * 60 * 1000L) {
            BeaconBatteryLevelUpdate update = new BeaconBatteryLevelUpdate();
            update.setBatteryLevel(batteryLevel);
            // if (trustedAuth != null) {
                try {
                    Beacon result = trustedApi.updateUsingPATCH1(update, id);
                    if (result != null) {
                        batteryLevelInfoDao.updateLastSent(id, System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        //}
    }

}
