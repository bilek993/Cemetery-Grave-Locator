package com.jakubbilinski.cemeterygravelocator;

import android.graphics.Bitmap;

/**
 * Created by bilek on 03.01.2017.
 */

public class Grave {

    private String name;
    private String birthDate;
    private String deathDate;
    private double latitude;
    private double longitude;
    private Bitmap photo;

    public Grave(String name, String birthDate, String deathDate, double latitude, double longitude, Bitmap photo) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
