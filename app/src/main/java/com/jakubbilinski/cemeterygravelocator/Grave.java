package com.jakubbilinski.cemeterygravelocator;

/**
 * Created by bilek on 03.01.2017.
 */

public class Grave {

    private int id;
    private int color;
    private String name;
    private String birthDate;
    private String deathDate;
    private String note;

    public Grave(int id, int color, String name, String birthDate, String deathDate, String note) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
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

    public String getNote() {
        return note;
    }
}
