package com.gpaye.exv0948.mygpa.model;

import java.util.ArrayList;

/**
 * Created by EXV0948 on 7/17/2017.
 */
public class GradeScale {

    private String gradeScaleName;
    private ArrayList<Grade> gradeScaleGrades;

    public GradeScale() {
        this("default scale");
        addGradeLetter(new Grade("A+", 4.0));
        addGradeLetter(new Grade("A", 4.0));
        addGradeLetter(new Grade("A-", 3.7));
        addGradeLetter(new Grade("B+", 3.3));
        addGradeLetter(new Grade("B", 3.0));
        addGradeLetter(new Grade("B-", 2.7));
        addGradeLetter(new Grade("C+", 2.3));
        addGradeLetter(new Grade("C", 2.0));
        addGradeLetter(new Grade("C-", 1.7));
        addGradeLetter(new Grade("D+", 1.3));
        addGradeLetter(new Grade("D", 1.0));
        addGradeLetter(new Grade("D-", 0.0));
        addGradeLetter(new Grade("F", 0.0));
    }

    public GradeScale(String gradeScaleName) {
        this.gradeScaleName = gradeScaleName;
        gradeScaleGrades = new ArrayList<>();
    }

    public void addGradeLetter(Grade newGradeLetter) {
        if (newGradeLetter == null) {
            throw new IllegalArgumentException("grade is null");
        }
        gradeScaleGrades.add(newGradeLetter);
    }

    @Override
    public String toString() {
        return "Grade scale: " + gradeScaleName + " " + gradeScaleGrades.toString();
    }

    public ArrayList<Grade> getGradeScaleGrades() {
        return gradeScaleGrades;
    }
}
