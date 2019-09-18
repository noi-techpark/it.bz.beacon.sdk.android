package it.bz.beacon.sdkandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;
import it.bz.beacon.sdkandroid.BeaconDetailActivity;
import it.bz.beacon.sdkandroid.BeaconDetailFragment;
import it.bz.beacon.sdkandroid.BeaconListActivity;
import it.bz.beacon.sdkandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InfoAdapter
        extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private final BeaconListActivity parentActivity;
    private List<Beacon> infos;
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

    public InfoAdapter(BeaconListActivity parent, boolean twoPane) {
        parentActivity = parent;
        this.infos = new ArrayList<>();
        this.isTablet = twoPane;
    }

    public void setItems(List<Beacon> beacons) {
        this.infos = beacons;
        sortItems();
        notifyDataSetChanged();
    }

    private void sortItems() {
        Collections.sort(this.infos, new Comparator<Beacon>() {
            public int compare(Beacon obj1, Beacon obj2) {
                if ((obj1 != null) && (obj2 != null) && (obj1.getName() != null) && (obj2.getName() != null)) {
                    return obj1.getName().compareTo(obj2.getName());
                }
                else {
                    return 0;
                }
            }
        });
    }

    public void addItem(Beacon beacon) {
        if (beacon == null)
            return;
        if (!isBeaconInList(beacon)) {
            this.infos.add(beacon);
        }
        sortItems();
        notifyDataSetChanged();
    }

    public void removeItem(Beacon beacon) {
        if (beacon == null)
            return;
        if (isBeaconInList(beacon)) {
            this.infos.remove(beacon);
        }
        sortItems();
        notifyDataSetChanged();
    }

    private boolean isBeaconInList(Beacon newInfo) {
        for (Beacon info : this.infos) {
            if (info.getId().equals(newInfo.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(infos.get(position).getName());
        holder.mContentView.setText(infos.get(position).getInstanceId());

        holder.itemView.setTag(infos.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return infos.size();
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
