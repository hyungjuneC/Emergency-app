package com.example.whwkd.tmapexample;

public class MapPoint {
    private String Name;
    private double latitude;
    private double longitude;
    private double distance;

    public MapPoint(){
        super();
    }

    public MapPoint(String Name, double latitude, double longitude) {
        //super();
        this.Name = Name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
