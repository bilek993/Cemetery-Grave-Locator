package com.jakubbilinski.cemeterygravelocator;

import android.graphics.Bitmap;

/**
 * Created by bilek on 03.01.2017.
 */

public class Grave {

    private String name;
    private String birthDate;
    private String deathDate;

    public Grave(String name, String birthDate, String deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
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
}
