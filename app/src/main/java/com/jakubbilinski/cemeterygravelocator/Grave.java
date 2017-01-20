package com.jakubbilinski.cemeterygravelocator;

/**
 * Created by bilek on 03.01.2017.
 */

public class Grave {

    private int id;
    private String name;
    private String birthDate;
    private String deathDate;
    private String note;

    public Grave(int id, String name, String birthDate, String deathDate, String note) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.note = note;
    }

    public int getId() {
        return id;
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
