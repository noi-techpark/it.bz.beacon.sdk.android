package it.bz.beacon.beaconsuedtirolsdk.data.repository;

import android.content.Context;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.bz.beacon.beaconsuedtirolsdk.R;
import it.bz.beacon.beaconsuedtirolsdk.auth.TrustedAuth;
import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BatteryLevelInfoDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.BatteryLevelInfo;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiCallback;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiClient;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiException;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.Configuration;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.TrustedBeaconControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BeaconBatteryLevelUpdate;

public class BatteryLevelInfoRepository {

    private BatteryLevelInfoDao batteryLevelInfoDao;
    private TrustedBeaconControllerApi trustedApi;
    private TrustedAuth trustedAuth;

    public BatteryLevelInfoRepository(Context context, TrustedAuth trustedAuth) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        batteryLevelInfoDao = db.batteryLevelInfoDao();
        int reduced_timeout = context.getResources().getInteger(R.integer.reduced_timeout);

        ApiClient apiClient = new ApiClient();
        apiClient.setConnectTimeout(reduced_timeout);
        apiClient.setReadTimeout(reduced_timeout);
        Configuration.setDefaultApiClient(apiClient);
        trustedApi = new TrustedBeaconControllerApi();
        this.trustedAuth = trustedAuth;
    }

    public void update(final String id, int batteryLevel) {
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
            lastSent = batteryLevelInfo.getLastSent().getTime();
            batteryLevelInfoDao.update(id, batteryLevel, System.currentTimeMillis());
        }

        if (lastSent < System.currentTimeMillis() - 24 * 60 * 60 * 1000L) {
            BeaconBatteryLevelUpdate update = new BeaconBatteryLevelUpdate();
            update.setBatteryLevel(batteryLevel);
            if (trustedAuth != null) {
                try {
                    trustedApi.updateUsingPATCH1Async(update, id, new ApiCallback<Beacon>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {

                        }

                        @Override
                        public void onSuccess(Beacon result, int statusCode, Map<String, List<String>> responseHeaders) {
                            batteryLevelInfoDao.updateLastSent(id, System.currentTimeMillis());
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    }).execute();
                } catch (IOException | ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
