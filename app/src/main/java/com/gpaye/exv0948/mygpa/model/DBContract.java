package com.gpaye.exv0948.mygpa.model;

import android.provider.BaseColumns;

/**
 * Created by EXV0948 on 7/18/2017.
 */

public class DBContract {


    private DBContract() {

    }

    public static class DatabaseSemester {
        public static final String TABLE_NAME = "semesters";
        public static final String COLUMN_SEMESTER_NAME = "semesterName";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_SEMESTER_NAME + " TEXT PRIMARY KEY" + ")";
    }

    public static class DatabaseCourse {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_COURSE_NAME = "courseName";
        public static final String COLUMN_COURSE_SEMESTER = "courseSemester";
        public static final String COLUMN_COURSE_CREDITS = "courseCredits";
        public static final String COLUMN_COURSE_GRADE = "courseGrade";
        public static final String COLUMN_COURSE_COUNT_TOWARDS_GPA = "courseCountTowardsGPA";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_COURSE_NAME + " TEXT," +
                COLUMN_COURSE_SEMESTER + " TEXT, " +
                COLUMN_COURSE_CREDITS + " FLOAT, " +
                COLUMN_COURSE_GRADE + " FLOAT, " +
                COLUMN_COURSE_COUNT_TOWARDS_GPA + " INTEGER," +
                "PRIMARY KEY(" + COLUMN_COURSE_NAME + ", " + COLUMN_COURSE_SEMESTER + ")," +
                "FOREIGN KEY(" + COLUMN_COURSE_SEMESTER + ") REFERENCES " +
                DatabaseSemester.TABLE_NAME + "(" + DatabaseSemester.COLUMN_SEMESTER_NAME + ") " + " ON DELETE CASCADE" +")";
    }
}
