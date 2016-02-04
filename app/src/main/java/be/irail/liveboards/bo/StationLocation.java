package be.irail.liveboards.bo;

import android.util.Log;

public class StationLocation implements Comparable<Object> {

    private String name;
    private String id;
    private double locationY;
    private double locationX;
    private String distance;
    private double away = -1;

    public String getName() {
        return name;
    }


    public String getDistance() {
        return distance;
    }

    public void setAway(double p) {
        this.away = p;
    }

    public double getLat() {
        return locationY;
    }

    public double getLon() {
        return locationX;
    }


    public String getId() {
        return id;
    }


    public int compareTo(Object toCompare) {
        StationLocation otherStation = (StationLocation) toCompare;
        // return  Double.compare(Double.valueOf(this.distance),Double.valueOf(otherStation.getDistance()));
        if (this.getAway() > 0 && otherStation.getAway() > 0) {
            return this.getAway() > otherStation.getAway() ? 1 : -1;
        } else {
            return (this.getName().compareTo(otherStation.getName()));
        }


    }
    public double getAway() {
        return away;
    }

    @Override
    public String toString() {
        return name;
    }
}
