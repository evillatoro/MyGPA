package com.gpaye.exv0948.mygpa.model;

/**
 * Created by EXV0948 on 7/17/2017.
 */
public class Grade {

    private String letter;
    private double points;

    public Grade(String letter, double points) {
        this.letter = letter;
        this.points = points;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return letter + " " + points;
    }
}
