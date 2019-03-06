package it.bz.beacon.beaconsuedtirolsdk.data.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Info;
import it.bz.beacon.beaconsuedtirolsdk.data.event.InsertEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.event.LoadInfoEvent;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.InfoRepository;

public class InfoViewModel extends AndroidViewModel {

    private InfoRepository repository;

    private LiveData<List<Info>> infos;

    public InfoViewModel(Application application) {
        super(application);
        repository = new InfoRepository(application);
        infos = repository.getAll();
    }

    public LiveData<List<Info>> getAll() {
        return infos;
    }

    public void getById(String id, LoadInfoEvent loadEvent) {
        repository.getById(id, loadEvent);
    }

    public void insert(Info info, InsertEvent event) {
        repository.insert(info, event);
    }

    public void insert(Info info) {
        repository.insert(info, null);
    }
}
