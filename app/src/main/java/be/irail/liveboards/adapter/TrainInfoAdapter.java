package be.irail.liveboards.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import be.irail.liveboards.R;
import be.irail.liveboards.Utils;
import be.irail.liveboards.bo.Station;
import be.irail.liveboards.bo.Vehicle;


public class TrainInfoAdapter extends RecyclerView.Adapter<TrainInfoAdapter.ViewHolder> {
    ArrayList<Vehicle.VehicleStop> list;

    public TrainInfoAdapter(ArrayList<Vehicle.VehicleStop> list) {
        this.list = list;
    }

    @Override
    public TrainInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemLayoutView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info_train, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Vehicle.VehicleStop vehicleStop=list.get(position);

        viewHolder.station.setText(Html.fromHtml(vehicleStop.getStation()));
        viewHolder.time.setText(Utils.formatDate(vehicleStop.getTime(), false, false));

        if (vehicleStop.getDelay().contentEquals("0"))
            viewHolder.delay.setText("");
        else
            try {
                viewHolder.delay.setText("+"
                        + (Integer.valueOf(vehicleStop.getDelay()) / 60)
                        + "'");
            } catch (Exception e) {
                viewHolder.delay.setText(vehicleStop.getDelay());
            }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time ;
        TextView delay;
        TextView station;

        public ViewHolder(View v) {
            super(v);
            time = (TextView) v.findViewById(R.id.time);
            delay = (TextView) v.findViewById(R.id.delay);
            station = (TextView) v.findViewById(R.id.station);
        }
    }
}
