package it.bz.beacon.sdkandroid;

import android.app.Activity;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Beacon detail screen.
 * This fragment is either contained in a {@link BeaconListActivity}
 * in two-pane mode (on tablets) or a {@link BeaconDetailActivity}
 * on handsets.
 */
public class BeaconDetailFragment extends Fragment {

    public static final String ARG_BEACON = "BEACON";

    private Beacon beacon;

    public BeaconDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_BEACON)) {
            beacon = getArguments().getParcelable(ARG_BEACON);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(beacon.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.beacon_detail, container, false);

        if (beacon != null) {
            ((TextView) rootView.findViewById(R.id.beacon_instance_id)).setText(beacon.getInstanceId());
            ((TextView) rootView.findViewById(R.id.beacon_id)).setText(beacon.getId());
            ((TextView) rootView.findViewById(R.id.beacon_major)).setText("Major: " + beacon.getMajor());
            ((TextView) rootView.findViewById(R.id.beacon_minor)).setText("Minor: " + beacon.getMinor());
        }

        return rootView;
    }
}
