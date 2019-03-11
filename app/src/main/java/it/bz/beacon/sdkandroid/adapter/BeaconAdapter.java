package it.bz.beacon.sdkandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.sdkandroid.BeaconDetailActivity;
import it.bz.beacon.sdkandroid.BeaconDetailFragment;
import it.bz.beacon.sdkandroid.BeaconListActivity;
import it.bz.beacon.sdkandroid.R;

public class BeaconAdapter
        extends RecyclerView.Adapter<BeaconAdapter.ViewHolder> {

    private final BeaconListActivity parentActivity;
    private List<Beacon> beacons;
    private final boolean isTablet;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Beacon item = (Beacon) view.getTag();
            if (isTablet) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(BeaconDetailFragment.ARG_BEACON, item);
                BeaconDetailFragment fragment = new BeaconDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.beacon_detail_container, fragment)
                        .commit();
            }
            else {
                Context context = view.getContext();
                Intent intent = new Intent(context, BeaconDetailActivity.class);
                intent.putExtra(BeaconDetailFragment.ARG_BEACON, item);
                context.startActivity(intent);
            }
        }
    };

    public BeaconAdapter(BeaconListActivity parent, boolean twoPane) {
        parentActivity = parent;
        this.beacons = new ArrayList<>();
        this.isTablet = twoPane;
    }

    public void setItems(List<Beacon> beacons) {
        Collections.sort(beacons, new Comparator<Beacon>() {
            public int compare(Beacon obj1, Beacon obj2) {
                if ((obj1 != null) && (obj2 != null) && (obj1.getName() != null) && (obj2.getName() != null)) {
                    return obj1.getName().compareTo(obj2.getName());
                }
                else {
                    return 0;
                }
            }
        });
        this.beacons = beacons;
        notifyDataSetChanged();
    }

    public void addItem(Beacon beacon) {
        this.beacons.add(0, beacon);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(beacons.get(position).getName());
        holder.mContentView.setText(beacons.get(position).getInstanceId());

        holder.itemView.setTag(beacons.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.beacon_id);
            mContentView = view.findViewById(R.id.beacon_description);
        }
    }
}
