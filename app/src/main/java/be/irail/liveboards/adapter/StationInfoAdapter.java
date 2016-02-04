package be.irail.liveboards.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import be.irail.liveboards.R;
import be.irail.liveboards.Utils;
import be.irail.liveboards.bo.Station;


public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.ViewHolder> {
    ArrayList<Station.StationDeparture> list;

    public StationInfoAdapter( ArrayList<Station.StationDeparture> list) {
        this.list = list;
    }

    @Override
    public StationInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View itemLayoutView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info_station, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Station.StationDeparture trainstop;
        trainstop=list.get(position);

        viewHolder.station.setText(Html.fromHtml(trainstop.getStation().replace(" [NMBS/SNCB]", "")));

        if (trainstop.isCancelled())
            viewHolder.time.setText(Html.fromHtml("<font color=\"red\">XXXX</font>"));
        else
            viewHolder.time.setText(Utils.formatDate(trainstop.getTime(), false, false));

        if (trainstop.getDelay().contentEquals("0"))
            viewHolder.delay.setText("");
        else try {
            viewHolder.delay.setText("+" + (Integer.valueOf(trainstop.getDelay()) / 60) + "'");
        } catch (Exception e) {
            viewHolder.delay.setText(trainstop.getDelay());
        }

        viewHolder.platform.setText(trainstop.getPlatform());
        viewHolder.train.setText(Utils.getTrainId(trainstop.getVehicle()));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView platform;
        TextView time;
        TextView delay;
        TextView station;
        TextView train;

        public ViewHolder(View v) {
            super(v);
            platform = (TextView) v.findViewById(R.id.tv_platform);
            time = (TextView) v.findViewById(R.id.tv_time);
            delay = (TextView) v.findViewById(R.id.tv_delay);
            station = (TextView) v.findViewById(R.id.tv_station);
            train = (TextView) v.findViewById(R.id.tv_train);
        }
    }
}
