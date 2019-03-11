package it.bz.beacon.beaconsuedtirolsdk.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;
import java.util.Map;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.api.InfoControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.R;
import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BeaconDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.DataUpdateEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.InsertEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;

public class BeaconRepository {

    private final static String LAST_REFRESH = "LAST_REFRESH";

    private BeaconDao beaconDao;
    private int synchronizationInterval;
    private SharedPreferences sharedPreferences;

    public BeaconRepository(Context context) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        beaconDao = db.beaconDao();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        synchronizationInterval = context.getResources().getInteger(R.integer.synchronization_interval);
    }

    public void getByInstanceId(String instanceId, LoadBeaconEvent loadEvent) {
        new LoadByInstanceIdTask(beaconDao, loadEvent).execute(instanceId);
    }

    public void getByMajorMinor(int major, int minor, LoadBeaconEvent loadEvent) {
        new LoadByMajorMinorTask(beaconDao, loadEvent).execute(major, minor);
    }

    private boolean shouldSynchronize() {
        long lastRefresh = sharedPreferences.getLong(LAST_REFRESH, 0L);
        return (lastRefresh + synchronizationInterval * 60000L < System.currentTimeMillis());
    }

    private void refreshBeacons(final DataUpdateEvent dataUpdateEvent) {
        try {
            InfoControllerApi infoControllerApi = new InfoControllerApi();
            infoControllerApi.getListUsingGET2Async(new ApiCallback<List<io.swagger.client.model.Info>>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    dataUpdateEvent.onError();
                }

                @Override
                public void onSuccess(List<io.swagger.client.model.Info> result, int statusCode, Map<String, List<String>> responseHeaders) {
                    if (result != null) {
                        for (int i = 0; i < result.size(); i++) {
                            saveInfo(result.get(i));
                        }
                        if (dataUpdateEvent != null) {
                            dataUpdateEvent.onSuccess();
                        }
                        sharedPreferences.edit().putLong(LAST_REFRESH, System.currentTimeMillis()).apply();
                    }
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });
        }
        catch (ApiException e) {
            if (dataUpdateEvent != null) {
                dataUpdateEvent.onError();
            }
        }
    }

    private void saveInfo(io.swagger.client.model.Info info) {
        Beacon beacon;
        beacon = new Beacon();
        beacon.setId(info.getId());
        beacon.setAddress(info.getAddress());
        beacon.setBeaconId(info.getBeaconId());
        beacon.setCap(info.getCap());
        beacon.setFloor(info.getFloor());
        beacon.setInstanceId(info.getInstanceId());
        beacon.setLatitude(info.getLatitude());
        beacon.setLongitude(info.getLongitude());
        beacon.setLocation(info.getLocation());
        beacon.setMajor(info.getMajor());
        beacon.setMinor(info.getMinor());
        beacon.setName(info.getName());
        beacon.setNamespace(info.getNamespace());
        beacon.setOpenDataPoiId(info.getOpenDataPoiId());
        beacon.setUuid(info.getUuid().toString());
        beacon.setWebsite(info.getWebsite());
        insert(beacon, null);
    }

    public void insert(Beacon info, InsertEvent event) {
        new InsertAsyncTask(beaconDao, event).execute(info);
    }

    private static class InsertAsyncTask extends AsyncTask<Beacon, Void, Boolean> {

        private BeaconDao asyncTaskDao;
        private InsertEvent insertEvent;

        InsertAsyncTask(BeaconDao dao, InsertEvent event) {
            asyncTaskDao = dao;
            insertEvent = event;
        }

        @Override
        protected Boolean doInBackground(final Beacon... params) {
            try {
                asyncTaskDao.insert(params[0]);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (insertEvent != null) {
                if (success) {
                    insertEvent.onSuccess();
                }
                else {
                    insertEvent.onError();
                }
            }
        }
    }

    private static class LoadByMajorMinorTask extends AsyncTask<Integer, Void, Beacon> {

        private BeaconDao asyncTaskDao;
        private LoadBeaconEvent loadEvent;

        LoadByMajorMinorTask(BeaconDao dao, LoadBeaconEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected Beacon doInBackground(Integer... ids) {
            return asyncTaskDao.getByMajorMinor(ids[0], ids[1]);
        }

        @Override
        protected void onPostExecute(Beacon beacon) {
            if (loadEvent != null) {
                if (beacon != null) {
                    loadEvent.onSuccess(beacon);
                }
                else {
                    loadEvent.onError();
                }
            }
        }
    }

    private static class LoadByInstanceIdTask extends AsyncTask<String, Void, Beacon> {

        private BeaconDao asyncTaskDao;
        private LoadBeaconEvent loadEvent;

        LoadByInstanceIdTask(BeaconDao dao, LoadBeaconEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected Beacon doInBackground(String... instanceIds) {
            return asyncTaskDao.getByInstanceId(instanceIds[0]);
        }

        @Override
        protected void onPostExecute(Beacon beacon) {
            if (loadEvent != null) {
                if (beacon != null) {
                    loadEvent.onSuccess(beacon);
                }
                else {
                    loadEvent.onError();
                }
            }
        }
    }
}
