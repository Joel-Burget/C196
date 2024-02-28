package com.burgetjoel.c196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    static final String DATABASE_NAME = "TERM.DB";
    static final int DATABASE_VERSION = 1;


    //Terms Database Table
    private static final class TermTable{
        static final String DATABASE_TABLE_TERM = "term";
        static final String TERM_ID_COLUMN =  "_id";
        static final String TERM_NAME_COLUMN = "term_name";
        static final String START_DATE_COLUMN = "start_date";
        static final String END_DATE_COLUMN = "end_date";

        private static final String CREATE_DB_QUERY = "CREATE TABLE " + DATABASE_TABLE_TERM + "( " + TERM_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TERM_NAME_COLUMN
                + " TEXT NOT NULL, " + START_DATE_COLUMN + " TEXT NOT NULL, " + END_DATE_COLUMN + " TEXT NOT NULL );";
    }

    //Course Database Table
    private static final class CourseTable{
        static final String DATABASE_TABLE_COURSE = "course";
        static final String COURSE_ID = "cID";
        static final String TERM_ID = "term_ID";
        static final String COURSE_NAME = "course_name";
        static final String COURSE_START_DATE = "courseStartDate";
        static final String COURSE_END_END_DATE = "courseEndDate";
        static final String COURSE_STATUS = "courseStatus";
        static final String  INSTRUCTOR_NAME = "instructorName";
        static final String INSTRUCTOR_PHONE_NUMBER = "instructorPhoneNumber";
        static final String INSTRUCTOR_EMAIL = "instructorEmail";


        private static final String CREATE_DB_COURSE = "CREATE TABLE " + DATABASE_TABLE_COURSE + "( " +
                COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TERM_ID + " INTEGER, " +
                COURSE_NAME + " TEXT NOT NULL, " +
                COURSE_START_DATE + " TEXT NOT NULL, " +
                COURSE_END_END_DATE + " TEXT NOT NULL, " +
                COURSE_STATUS + " TEXT NOT NULL, " +
                INSTRUCTOR_NAME + " TEXT, " +
                INSTRUCTOR_PHONE_NUMBER + " TEXT, " +
                INSTRUCTOR_EMAIL + " TEXT, " +
                "FOREIGN KEY(" + TERM_ID + ") REFERENCES " +
                TermTable.DATABASE_TABLE_TERM + "(" + TermTable.TERM_ID_COLUMN + ") ON DELETE CASCADE)";

    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TermTable.CREATE_DB_QUERY);
        db.execSQL(CourseTable.CREATE_DB_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL( "DROP TABLE IF EXISTS " + TermTable.DATABASE_TABLE_TERM);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.DATABASE_TABLE_COURSE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isOpen()){
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    void addTerm(String name, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TermTable.TERM_NAME_COLUMN, name);
        cv.put(TermTable.START_DATE_COLUMN, startDate);
        cv.put(TermTable.END_DATE_COLUMN, endDate);
        long result = db.insert(TermTable.DATABASE_TABLE_TERM, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllTerms(){
        String query = "SELECT * FROM " + TermTable.DATABASE_TABLE_TERM;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor getTermID(String name){
        String query = "SELECT * FROM " + TermTable.DATABASE_TABLE_TERM + "WHERE " + name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readAllCourses(){
        String query = "SELECT * FROM " + CourseTable.DATABASE_TABLE_COURSE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor courseCursor = null;
        if(db != null){
            courseCursor = db.rawQuery(query, null);
        }
        return courseCursor;
    }

    void updateTermData(String row_id, String name, String start_date, String end_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TermTable.TERM_NAME_COLUMN, name);
        cv.put(TermTable.START_DATE_COLUMN, start_date);
        cv.put(TermTable.END_DATE_COLUMN, end_date);

        long result = db.update(TermTable.DATABASE_TABLE_TERM, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneTermRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TermTable.DATABASE_TABLE_TERM, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllTermData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TermTable.DATABASE_TABLE_TERM);
    }

    void addCourse(String termID, String courseName, String startDate, String endDate, String instructorName, String instructorPhone, String instructorEmail, String courseStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CourseTable.TERM_ID, termID);
        cv.put(CourseTable.COURSE_NAME, courseName);
        cv.put(CourseTable.COURSE_START_DATE, startDate);
        cv.put(CourseTable.COURSE_END_END_DATE, endDate);
        cv.put(CourseTable.INSTRUCTOR_NAME, instructorName);
        cv.put(CourseTable.INSTRUCTOR_PHONE_NUMBER, instructorPhone);
        cv.put(CourseTable.INSTRUCTOR_EMAIL, instructorEmail);
        cv.put(CourseTable.COURSE_STATUS, courseStatus);

        long result = db.insert(CourseTable.DATABASE_TABLE_COURSE, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }
}
