package it.bz.beacon.beaconsuedtirolsdk.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.api.InfoControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.R;
import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.InfoDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Info;
import it.bz.beacon.beaconsuedtirolsdk.data.event.DataUpdateEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.InsertEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadInfoEvent;

public class InfoRepository {

    private final static String LAST_REFRESH = "LAST_REFRESH";
    private final static String LOG_TAG = "Beacon SDK";

    private InfoDao infoDao;
    private LiveData<List<Info>> infos;
    private int synchronizationInterval;
    private SharedPreferences sharedPreferences;

    public InfoRepository(Context context) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        infoDao = db.infoDao();
        infos = infoDao.getAll();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        synchronizationInterval = context.getResources().getInteger(R.integer.synchronization_interval);
    }

    public LiveData<List<Info>> getAll() {
        if (shouldSynchronize()) {
            refreshInfos(null);
        }
        return infos;
    }

    public LiveData<Info> getByIdLive(String id) {
        return infoDao.getByIdLive(id);
    }

    public void getById(String id, LoadInfoEvent loadEvent) {
        new LoadByIdTask(infoDao, loadEvent).execute(id);
    }

    private boolean shouldSynchronize() {
        long lastRefresh = sharedPreferences.getLong(LAST_REFRESH, 0L);
        return (lastRefresh + synchronizationInterval * 60000L < System.currentTimeMillis());
    }

    private void refreshInfos(final DataUpdateEvent dataUpdateEvent) {
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
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void saveInfo(io.swagger.client.model.Info remoteInfo) {
        Info info;
        info = new Info();
        info.setId(remoteInfo.getId());
        info.setAddress(remoteInfo.getAddress());
        info.setBeaconId(remoteInfo.getBeaconId());
        info.setCap(remoteInfo.getCap());
        info.setFloor(remoteInfo.getFloor());
        info.setInstanceId(remoteInfo.getInstanceId());
        info.setLatitude(remoteInfo.getLatitude());
        info.setLongitude(remoteInfo.getLongitude());
        info.setLocation(remoteInfo.getLocation());
        info.setMajor(remoteInfo.getMajor());
        info.setMinor(remoteInfo.getMinor());
        info.setName(remoteInfo.getName());
        info.setNamespace(remoteInfo.getNamespace());
        info.setOpenDataPoiId(remoteInfo.getOpenDataPoiId());
        info.setUuid(remoteInfo.getUuid().toString());
        info.setWebsite(remoteInfo.getWebsite());
        insert(info, null);
    }

    public void insert(Info info, InsertEvent event) {
        new InsertAsyncTask(infoDao, event).execute(info);
    }

    private static class InsertAsyncTask extends AsyncTask<Info, Void, Boolean> {

        private InfoDao asyncTaskDao;
        private InsertEvent insertEvent;

        InsertAsyncTask(InfoDao dao, InsertEvent event) {
            asyncTaskDao = dao;
            insertEvent = event;
        }

        @Override
        protected Boolean doInBackground(final Info... params) {
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

    private static class LoadByIdTask extends AsyncTask<String, Void, Info> {

        private InfoDao asyncTaskDao;
        private LoadInfoEvent loadEvent;

        LoadByIdTask(InfoDao dao, LoadInfoEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected Info doInBackground(String... ids) {
            return asyncTaskDao.getById(ids[0]);
        }

        @Override
        protected void onPostExecute(Info info) {
            if (loadEvent != null) {
                if (info != null) {
                    loadEvent.onSuccess(info);
                }
                else {
                    loadEvent.onError();
                }
            }
        }
    }
}
