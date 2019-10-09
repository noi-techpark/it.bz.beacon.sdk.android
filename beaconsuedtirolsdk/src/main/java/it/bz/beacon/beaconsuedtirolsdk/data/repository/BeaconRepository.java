package it.bz.beacon.beaconsuedtirolsdk.data.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.bz.beacon.beaconsuedtirolsdk.R;
import it.bz.beacon.beaconsuedtirolsdk.data.BeaconDatabase;
import it.bz.beacon.beaconsuedtirolsdk.data.dao.BeaconDao;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.beaconsuedtirolsdk.data.event.GetDateEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.InsertEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadAllBeaconsEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadBeaconEvent;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiCallback;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiClient;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.ApiException;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.Configuration;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.api.InfoControllerApi;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Info;

public class BeaconRepository {

    private BeaconDao beaconDao;
    private InfoControllerApi infoControllerApi;
    private int timeout;
    private int reduced_timeout;

    public BeaconRepository(Context context) {
        BeaconDatabase db = BeaconDatabase.getDatabase(context);
        beaconDao = db.beaconDao();
        timeout = context.getResources().getInteger(R.integer.timeout);
        reduced_timeout = context.getResources().getInteger(R.integer.reduced_timeout);

        ApiClient apiClient = new ApiClient();
        apiClient.setConnectTimeout(timeout);
        apiClient.setReadTimeout(timeout);
        Configuration.setDefaultApiClient(apiClient);
        infoControllerApi = new InfoControllerApi();

        refreshBeacons(null);
    }

    public void getByInstanceId(final String instanceId, final LoadBeaconEvent loadEvent) {
        loadFromCacheByInstanceId(loadEvent, instanceId);
    }

    public void getById(final String id, final LoadBeaconEvent loadEvent) {
        loadFromCacheById(loadEvent, id);
    }

    public void getByMajorMinor(final int major, final int minor, final LoadBeaconEvent loadEvent) {
        try {
            infoControllerApi.getApiClient().setConnectTimeout(reduced_timeout);
            infoControllerApi.getApiClient().setReadTimeout(reduced_timeout);
            infoControllerApi.getiBeaconUsingGETAsync(major, minor, new ApiCallback<Info>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                    loadFromCacheByMajorMinor(loadEvent, major, minor);
                }

                @Override
                public void onSuccess(Info result, int statusCode, Map<String, List<String>> responseHeaders) {
                    Beacon beacon = Beacon.fromInfo(result);
                    if (beacon != null) {
                        if (loadEvent != null) {
                            loadEvent.onSuccess(beacon);
                        }
                        insert(beacon, null);
                    }
                    else {
                        loadFromCacheByMajorMinor(loadEvent, major, minor);
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
            e.printStackTrace();
            loadFromCacheByMajorMinor(loadEvent, major, minor);
        }
    }

    private void loadFromCacheByInstanceId(LoadBeaconEvent loadEvent, String instanceId) {
        new LoadByInstanceIdTask(beaconDao, loadEvent).execute(instanceId);
    }

    private void loadFromCacheById(LoadBeaconEvent loadEvent, String id) {
        new LoadByIdTask(beaconDao, loadEvent).execute(id);
    }

    private void loadFromCacheByMajorMinor(LoadBeaconEvent loadEvent, int major, int minor) {
        new LoadByMajorMinorTask(beaconDao, loadEvent).execute(major, minor);
    }

    private void loadAllFromCache(LoadAllBeaconsEvent loadEvent) {
        new LoadAllTask(beaconDao, loadEvent).execute();
    }

    public void getAll(LoadAllBeaconsEvent loadEvent) {
        refreshBeacons(loadEvent);
    }

    private void getMaxUpdatedAt(GetDateEvent loadEvent) {
        new GetMaxUpdatedAtTask(beaconDao, loadEvent).execute();
    }

    private void refreshBeacons(final LoadAllBeaconsEvent loadAllBeaconsEvent) {
        getMaxUpdatedAt(new GetDateEvent() {
            @Override
            public void onSuccess(long date) {
                try {
                    infoControllerApi.getApiClient().setConnectTimeout(timeout);
                    infoControllerApi.getApiClient().setReadTimeout(timeout);
                    infoControllerApi.getListUsingGET2Async(date, new ApiCallback<List<Info>>() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                            if (loadAllBeaconsEvent != null) {
                                loadAllFromCache(loadAllBeaconsEvent);
                            }
                        }

                        @Override
                        public void onSuccess(List<Info> result, int statusCode, Map<String, List<String>> responseHeaders) {
                            if (result != null) {
//                                Log.d("SDK", "number of results: " + result.size());
                                final List<Beacon> beacons = new ArrayList<>();
                                for (int i = 0; i < result.size(); i++) {
                                    Beacon beacon = Beacon.fromInfo(result.get(i));
                                    beacons.add(beacon);
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        beaconDao.insertMultiple(beacons);
                                    }
                                });
                                if (loadAllBeaconsEvent != null) {
                                    loadAllFromCache(loadAllBeaconsEvent);
                                }
                            }
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    });
                } catch (ApiException e) {
                    if (loadAllBeaconsEvent != null) {
                        loadAllFromCache(loadAllBeaconsEvent);
                    }
                }
            }

            @Override
            public void onError() {
                Log.e("Beacon SDK", "error");
            }
        });
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
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (insertEvent != null) {
                if (success) {
                    insertEvent.onSuccess();
                } else {
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
                } else {
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
                } else {
                    loadEvent.onError();
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
                } else {
                    loadEvent.onError();
                }
            }
        }
    }

    private static class GetMaxUpdatedAtTask extends AsyncTask<String, Void, Long> {

        private BeaconDao asyncTaskDao;
        private GetDateEvent loadEvent;

        GetMaxUpdatedAtTask(BeaconDao dao, GetDateEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected Long doInBackground(String... ids) {
            return asyncTaskDao.getMaxUpdatedAt();
        }

        @Override
        protected void onPostExecute(Long date) {
            if (loadEvent != null) {
                if (date != null) {
                    loadEvent.onSuccess(date);
                } else {
                    loadEvent.onError();
                }
            }
        }
    }

    private static class LoadAllTask extends AsyncTask<Void, Void, List<Beacon>> {

        private BeaconDao asyncTaskDao;
        private LoadAllBeaconsEvent loadEvent;

        LoadAllTask(BeaconDao dao, LoadAllBeaconsEvent event) {
            asyncTaskDao = dao;
            loadEvent = event;
        }

        @Override
        protected List<Beacon> doInBackground(Void... voids) {
            return asyncTaskDao.getAll();
        }

        @Override
        protected void onPostExecute(List<Beacon> beacons) {
            if (loadEvent != null) {
                if (beacons != null) {
                    loadEvent.onSuccess(beacons);
                } else {
                    loadEvent.onError();
                }
            }
        }
    }
}
