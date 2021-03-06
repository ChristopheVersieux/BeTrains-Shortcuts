package be.irail.liveboards.bo;

import android.text.Html;

import java.util.ArrayList;

/**
 * Created by 201601 on 04-Dec-15.
 */
public class Vehicle {

    private VehicleStops stops;
    private String version;
    private String vehicle;
    private long timestamp;

    public VehicleStops getVehicleStops() {
        return stops;
    }

    public String getVersion() {
        return version;
    }

    public String getId() {
        return vehicle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public class VehicleStops {

        private ArrayList<VehicleStop> stop;

        public ArrayList<VehicleStop> getVehicleStop() {
            return stop;
        }
    }

    public static class VehicleStop {

        private String station;
        private long time;
        private String delay;
        Station.StationInfo stationinfo;


        public Station.StationInfo getStationInfo() {
            return stationinfo;
        }

        public String getStation() {
            return Html.fromHtml(station).toString();
        }

        public long getTime() {
            return time;
        }

        public String getDelay() {
            return delay;
        }

        public String getStatus() {
            if (delay.contentEquals("0"))
                return "";

            return "+" + Integer.valueOf(delay) / 60 + "'";
        }
    }

}