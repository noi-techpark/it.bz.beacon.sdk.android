package it.bz.beacon.beaconsuedtirolsdk.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import it.bz.beacon.beaconsuedtirolsdk.data.repository.BeaconRepository;

public class SynchronizationWorker extends Worker {

    private Context context;

    public SynchronizationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {
        BeaconRepository repository = new BeaconRepository(context);
        repository.getAll(null);
        return Result.success();
    }
}