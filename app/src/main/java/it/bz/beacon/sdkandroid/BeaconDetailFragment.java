package it.bz.beacon.sdkandroid;

import android.app.Activity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
            ((TextView) rootView.findViewById(R.id.beacon_name)).setText(beacon.getName());
            ((TextView) rootView.findViewById(R.id.beacon_address)).setText(beacon.getAddress());
            ((TextView) rootView.findViewById(R.id.beacon_location)).setText(beacon.getCap() + " " + beacon.getLocation());
            ((TextView) rootView.findViewById(R.id.beacon_major)).setText("Major: " + beacon.getMajor());
            ((TextView) rootView.findViewById(R.id.beacon_minor)).setText("Minor: " + beacon.getMinor());

            FloatingActionButton fab = getActivity().findViewById(R.id.fab);
            if (beacon.getLatitude() == 0 || beacon.getLongitude() == 0) {
                fab.hide();
            }
            else {
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=" + beacon.getLatitude() + "," + beacon.getLongitude()));
                        startActivity(intent);
                    }
                });
            }

            Button btn = rootView.findViewById(R.id.btn_website);
            if (TextUtils.isEmpty(beacon.getWebsite())) {
                btn.setVisibility(View.GONE);
            }
            else {
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = beacon.getWebsite();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
            }
        }

        return rootView;
    }
}
