package it.bz.beacon.beaconsuedtirolsdk.data.repository;

import android.content.Context;

import java.util.Date;

import it.bz.beacon.beaconsuedtirolsdk.R;
import it.bz.beacon.beaconsuedtirolsdk.auth.TrustedAuth;
import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BatteryLevelInfoDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.BatteryLevelInfo;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.TrustedBeaconControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BeaconBatteryLevelUpdate;

public class BatteryLevelInfoRepository {

    private final BatteryLevelInfoDao batteryLevelInfoDao;
    private final TrustedBeaconControllerApi trustedApi;
    private final TrustedAuth trustedAuth;

    public BatteryLevelInfoRepository(Context context, TrustedAuth trustedAuth) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        batteryLevelInfoDao = db.batteryLevelInfoDao();
        int reduced_timeout = context.getResources().getInteger(R.integer.reduced_timeout);

        trustedApi = new TrustedBeaconControllerApi();
        trustedApi.getInvoker().setConnectionTimeout(reduced_timeout);
        this.trustedAuth = trustedAuth;
        if (this.trustedAuth != null) {
            trustedApi.getInvoker().setUsername(this.trustedAuth.getUsername());
            trustedApi.getInvoker().setPassword(this.trustedAuth.getPassword());
        }
    }

    public void update(final String id, final int batteryLevel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BatteryLevelInfo batteryLevelInfo = batteryLevelInfoDao.getById(id);
                long lastSent = 0;

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

                if (lastSent < System.currentTimeMillis() - 24 * 60 * 60 * 1000L) {
                    BeaconBatteryLevelUpdate update = new BeaconBatteryLevelUpdate();
                    update.setBatteryLevel(batteryLevel);
                    if (trustedAuth != null) {
                        try {
                            Beacon result = trustedApi.updateUsingPATCH1(update, id);
                            if (result != null) {
                                batteryLevelInfoDao.updateLastSent(id, System.currentTimeMillis());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
