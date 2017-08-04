package com.gpaye.exv0948.mygpa.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gpaye.exv0948.mygpa.model.DBContract.DatabaseCourse;
import com.gpaye.exv0948.mygpa.model.DBContract.DatabaseSemester;

import java.util.ArrayList;

/**
 * easy way to manage databse creation and versioning
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 13;
    public static final String DATABASE_NAME = "mygpa_database";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.DatabaseSemester.CREATE_TABLE);
        db.execSQL(DBContract.DatabaseCourse.CREATE_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DatabaseSemester.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DatabaseCourse.TABLE_NAME);
        onCreate(db);
    }

    public boolean addSemester(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSemester.COLUMN_SEMESTER_NAME, name);
        Log.d(TAG, "addSemester: Adding " + name + " to " + DatabaseSemester.TABLE_NAME);

        long result = db.insert(DatabaseSemester.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseCourse.COLUMN_COURSE_NAME, course.getName());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_SEMESTER, course.getSemester());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_CREDITS, course.getCredits());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_GRADE, course.getGrade());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_COUNT_TOWARDS_GPA, course.getCountTowardsGPA());

        long result = db.insert(DatabaseCourse.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }

        return true;

    }

    public boolean updateCourse(Course course, String oldCourseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseCourse.COLUMN_COURSE_NAME, course.getName());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_SEMESTER, course.getSemester());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_CREDITS, course.getCredits());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_GRADE, course.getGrade());
        contentValues.put(DatabaseCourse.COLUMN_COURSE_COUNT_TOWARDS_GPA, course.getCountTowardsGPA());

        String selection =
                DatabaseCourse.COLUMN_COURSE_NAME + " = ?" +
                "AND " + DatabaseCourse.COLUMN_COURSE_SEMESTER + " = ?";

        String[] selectionArgs = { oldCourseName, course.getSemester()};

        try {
            long result = db.update(DatabaseCourse.TABLE_NAME,
                    contentValues, selection, selectionArgs);
            return true;
        } catch (SQLiteConstraintException e) {
            return false;
        }
    }

    public ArrayList<Semester> getSemesters() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseSemester.TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        ArrayList<Semester> semesters = new ArrayList<>();
        while (data.moveToNext()) {
            Semester s = new Semester(data.getString(0));
            semesters.add(0, s);
            Cursor courses = getCourses(s.getSemesterName());
            while(courses.moveToNext()) {
                Course course = new Course(courses.getString(0), courses.getString(1), courses.getDouble(2), courses.getDouble(3), courses.getInt(4));
                s.addCourse(course);
            }
        }

        return semesters;
    }

    public Cursor getCourses(String semesterName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseCourse.TABLE_NAME + " WHERE "
                + DatabaseCourse.COLUMN_COURSE_SEMESTER + "= \"" + semesterName + "\"";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    public void deleteSemester(String semesterName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selection =
                DatabaseSemester.COLUMN_SEMESTER_NAME + " = ?";

        String[] selectionArgs = { semesterName};

        long result = db.delete(DatabaseSemester.TABLE_NAME, selection, selectionArgs);
//        if (result == -1) {
//            return false;
//        }
//
//        return true;
    }

    public void deleteCourse(Course courseToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection =
                DatabaseCourse.COLUMN_COURSE_SEMESTER + " = ?"+
                "AND " + DatabaseCourse.COLUMN_COURSE_NAME + " = ?";

        String[] selectionArgs = { courseToDelete.getSemester(), courseToDelete.getName()};

        long result = db.delete(DatabaseCourse.TABLE_NAME, selection, selectionArgs);

    }
}
