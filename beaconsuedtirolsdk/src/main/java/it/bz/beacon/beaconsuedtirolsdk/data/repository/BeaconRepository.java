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
    private final static String LOG_TAG = "Beacon SDK";

    private BeaconDao beaconDao;
    private int synchronizationInterval;
    private SharedPreferences sharedPreferences;

    public BeaconRepository(Context context) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        beaconDao = db.infoDao();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        synchronizationInterval = context.getResources().getInteger(R.integer.synchronization_interval);
    }
// TODO: decide if give back LiveData, otherwise move in AsyncTask
    public List<Beacon> getAll() {
        if (shouldSynchronize()) {
            refreshBeacons(null);
        }
        return beaconDao.getAll();
    }

    public void getById(String id, LoadBeaconEvent loadEvent) {
        new LoadByIdTask(beaconDao, loadEvent).execute(id);
    }

    public void getByInstanceId(String instanceId, LoadBeaconEvent loadEvent) {
        new LoadByInstanceIdTask(beaconDao, loadEvent).execute(instanceId);
    }

    public void getManyByInstanceIds(String[] instanceIds, LoadBeaconEvent loadEvent) {
        new LoadManyByInstanceIdsTask(beaconDao, loadEvent).execute(instanceIds);
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
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void saveInfo(io.swagger.client.model.Info remoteInfo) {
        Beacon info;
        info = new Beacon();
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

    private static class LoadByIdTask extends AsyncTask<String, Void, Beacon> {

        private BeaconDao asyncTaskDao;
        private LoadBeaconEvent loadEvent;

        LoadByIdTask(BeaconDao dao, LoadBeaconEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected Beacon doInBackground(String... ids) {
            return asyncTaskDao.getById(ids[0]);
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

    private static class LoadManyByInstanceIdsTask extends AsyncTask<String, Void, List<Beacon>> {

        private BeaconDao asyncTaskDao;
        private LoadBeaconEvent loadEvent;

        LoadManyByInstanceIdsTask(BeaconDao dao, LoadBeaconEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected List<Beacon> doInBackground(String... instanceIds) {
            return asyncTaskDao.getManyByInstanceIds(instanceIds);
        }

        @Override
        protected void onPostExecute(List<Beacon> beacons) {
            if (loadEvent != null) {
                if (beacons != null) {
                    loadEvent.onSuccess(beacons);
                }
                else {
                    loadEvent.onError();
                }
            }
        }
    }
}
