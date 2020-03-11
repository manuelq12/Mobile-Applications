package com.example.reto1;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Marker implements Serializable {

    private static final double EARTH_RADIUS_KM = 6371;

    private String name;

    private double latitude;

    private double longitude;


    public Marker() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double calculateDistance (Marker one){
        double latR1=   this.latitude*(Math.PI/180);
        double latR2 =  one.getLatitude()* (Math.PI/180);
        double latDif = (one.getLatitude()-this.latitude)*(Math.PI/180);
        double longDif = (one.getLongitude()-this.longitude)*(Math.PI/180);

        double a = Math.sin(latDif/2) * Math.sin(latDif/2) + Math.cos(latR1)*Math.cos(latR2)
                * Math.sin(longDif/2)*Math.sin(longDif/2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double distance = Math.round(EARTH_RADIUS_KM * c*100)/100;
        return distance;
    }
}
