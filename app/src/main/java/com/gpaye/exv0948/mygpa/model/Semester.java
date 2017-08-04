package com.gpaye.exv0948.mygpa.model;

import java.util.ArrayList;

/**
 * Created by EXV0948 on 7/17/2017.
 */
public class Semester {

    private String semesterName;
    private ArrayList<Course> courses;
    private int numberOfCourses;

    private double qualityPoints;
    private double semesterCredits;
    private double semesterGpa;

    public Semester(String semesterName) {
        this.semesterName = semesterName;
        this.courses = new ArrayList<>();
        this.numberOfCourses = 0;
        this.qualityPoints = 0;
        this.semesterCredits = 0;
        this.semesterGpa = 0;
    }

    public void addCourse(Course newCourse) {
        if (newCourse == null) {
            throw new IllegalArgumentException("course is null");
        }

        this.courses.add(0, newCourse);
        this.numberOfCourses++;
        if (newCourse.getCountTowardsGPA() == 1) {
            this.qualityPoints += newCourse.getQualityPoints();
            this.semesterCredits += newCourse.getCredits();
            calculateGPA();
        } else {

        }
    }

    public void removeCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("course is null");
        }

        this.courses.remove(course);
        this.numberOfCourses--;
        this.qualityPoints -= course.getQualityPoints();
        this.semesterCredits -= course.getCredits();
        calculateGPA();
    }

    private void calculateGPA() {
        if (this.semesterCredits == 0) {
            this.semesterGpa = 0;
        } else {
            this.semesterGpa = this.qualityPoints / this.semesterCredits;
        }
    }

    @Override
    public String toString() {
        String semesterCourses = semesterName + " GPA:" + semesterGpa + "\n";
        return semesterCourses;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public double getSemesterGpa() {
        return semesterGpa;
    }

    public double getQualityPoints() {
        return qualityPoints;
    }

    public double getSemesterCredits() {
        return semesterCredits;
    }
}