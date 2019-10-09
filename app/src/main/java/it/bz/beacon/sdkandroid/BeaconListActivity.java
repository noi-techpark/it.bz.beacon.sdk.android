package it.bz.beacon.sdkandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import it.bz.beacon.beaconsuedtirolsdk.NearbyBeaconManager;
import it.bz.beacon.beaconsuedtirolsdk.exception.MissingLocationPermissionException;
import it.bz.beacon.beaconsuedtirolsdk.exception.NoBluetoothException;
import it.bz.beacon.beaconsuedtirolsdk.listener.EddystoneListener;
import it.bz.beacon.beaconsuedtirolsdk.listener.IBeaconListener;
import it.bz.beacon.beaconsuedtirolsdk.result.Eddystone;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeacon;
import it.bz.beacon.beaconsuedtirolsdk.result.IBeaconExtended;
import it.bz.beacon.sdkandroid.adapter.InfoAdapter;

public class BeaconListActivity extends AppCompatActivity implements IBeaconListener, EddystoneListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private boolean isTablet;
    private InfoAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.beacon_detail_container) != null) {
            isTablet = true;
        }

        RecyclerView recyclerView = findViewById(R.id.beacon_list);
        assert recyclerView != null;
        adapter = new InfoAdapter(this, isTablet);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        NearbyBeaconManager.getInstance().setIBeaconListener(this);
        NearbyBeaconManager.getInstance().setEddystoneListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScanningIfLocationPermissionGranted();
    }

    private void startScanningIfLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            tryStartScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tryStartScanning();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void tryStartScanning() {
        try {
            NearbyBeaconManager.getInstance().startScanning();
        } catch (NoBluetoothException e) {
            e.printStackTrace();
            showSnackbarWithRetry(getString(R.string.no_bluetooth));
        } catch (MissingLocationPermissionException e) {
            e.printStackTrace();
            showSnackbarWithRetry(getString(R.string.missing_location_permission));
        }
    }

    private void showSnackbarWithRetry(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryStartScanning();
                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NearbyBeaconManager.getInstance().stopScanning();
    }

    @Override
    public void onEddystoneDiscovered(final Eddystone eddystone) {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.addItem(eddystone.getInfo());
                setTitle();
            }
        });
    }

    @Override
    public void onEddystoneLost(final Eddystone eddystone) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.removeItem(eddystone.getInfo());
            }
        });
    }

    @Override
    public void onIBeaconDiscovered(final IBeacon iBeacon) {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.addItem(iBeacon.getInfo());
                setTitle();
            }
        });
    }

    @Override
    public void onIBeaconDiscovered(final IBeaconExtended iBeacon) {
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.addItem(iBeacon.getInfo());
                setTitle();
            }
        });
    }

    @Override
    public void onIBeaconUpdated(final List<IBeaconExtended> iBeacons) {
        /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (IBeaconExtended b : iBeacons) {
                    adapter.removeItem(b.getInfo());
                    adapter.addItem(b.getInfo());
                }
                setTitle();
            }
        }); */
    }

    @Override
    public void onIBeaconLost(final IBeacon iBeacon) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.removeItem(iBeacon.getInfo());
            }
        });
    }

    private void setTitle() {
        toolbar.setTitle(String.format(Locale.getDefault(), "%s (%d)", getString(R.string.app_name), adapter.getItemCount()));
    }
}
