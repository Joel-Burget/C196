package com.burgetjoel.c196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
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
        static final String NOTES = "notes";


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
                NOTES + " TEXT DEFAULT 'no notes'," +
                "FOREIGN KEY(" + TERM_ID + ") REFERENCES " +
                TermTable.DATABASE_TABLE_TERM + "(" + TermTable.TERM_ID_COLUMN + ") ON DELETE CASCADE)";

    }

    //Exam Table
    private static final class AssessmentTable {
        static final String DATABASE_TABLE_ASSESSMENT = "exam";
        static final String ASSESSMENT_ID  = "assessmentID";
        static final String COURSE_ID = "courseID";
        static final String ASSESSMENT_DATE = "assessment_date";
        static final String ASSESSMENT_NAME = "assessment_name";
        static final String ASSESSMENT_TYPE = "assessment_type";
        static final String START_TIME = "start_time";
        static final String END_TIME = "end_time";
        static final String NOTES = "notes";


        private static final String CREATE_DB_ASSESSMENT = "CREATE TABLE " + DATABASE_TABLE_ASSESSMENT+ "( " +
                ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COURSE_ID + " INTEGER, " +
                ASSESSMENT_NAME + " TEXT NOT NULL, " +
                ASSESSMENT_DATE + " TEXT NOT NULL, " +
                START_TIME + " TEXT NOT NULL, " +
                END_TIME + " TEXT NOT NULL, " +
                ASSESSMENT_TYPE + " TEXT NOT NULL, " +
                NOTES + "TEXT, " +
                "FOREIGN KEY(" + COURSE_ID + ") REFERENCES " +
                CourseTable.DATABASE_TABLE_COURSE + "(" + CourseTable.COURSE_ID + ") ON DELETE CASCADE)";

    }

    //Scheduler Database Table
    private static final class SchedulerTable{
        static final String DATABASE_TABLE_SCHEDULER = "scheduler";
        static final String SCHEDULER_ID = "schedulerID";
        static final String TERM_NAME = "termName";
        static final String TERM_START_DATE = "termStartDate";
        static final String TERM_END_DATE = "termEndDate";
        static final String TERM_COLOR = "color";

        private static final String CREATE_DB_SCHEDULER = "CREATE TABLE " + DATABASE_TABLE_SCHEDULER + "( " +
                SCHEDULER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TERM_NAME + " TEXT NOT NULL, " +
                TERM_START_DATE + " TEXT NOT NULL, " +
                TERM_END_DATE + " TEXT NOT NULL, " +
                TERM_COLOR + " TEXT NOT NULL)";
    }


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TermTable.CREATE_DB_QUERY);
        db.execSQL(CourseTable.CREATE_DB_COURSE);
        db.execSQL(AssessmentTable.CREATE_DB_ASSESSMENT);
        db.execSQL(SchedulerTable.CREATE_DB_SCHEDULER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TermTable.DATABASE_TABLE_TERM);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.DATABASE_TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentTable.DATABASE_TABLE_ASSESSMENT);
        db.execSQL("DROP TABLE IF EXISTS " + SchedulerTable.DATABASE_TABLE_SCHEDULER);
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
        String query = "SELECT * FROM " + CourseTable.DATABASE_TABLE_COURSE + " WHERE " + CourseTable.TERM_ID + " = "
                + row_id + ";";
        Cursor numCourses = db.rawQuery(query, null);
        if(numCourses.getCount() > 0){
            Toast.makeText(context, "Cannot Delete Terms With Active Courses", Toast.LENGTH_SHORT).show();
        }else{
            long result = db.delete(TermTable.DATABASE_TABLE_TERM, "_id=?", new String[]{row_id});
            if(result == -1){
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void deleteAllTermData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TermTable.DATABASE_TABLE_TERM);
    }

    void addCourse(String termID, String courseName, String startDate, String endDate, String instructorName, String instructorPhone, String instructorEmail, String courseStatus, String courseNotes){
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
        cv.put(CourseTable.NOTES, courseNotes);

        long result = db.insert(CourseTable.DATABASE_TABLE_COURSE, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readCourses(int ID){
        String query = "SELECT * FROM " + CourseTable.DATABASE_TABLE_COURSE + " WHERE TERM_ID = '"
                + ID + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor courseCursor = null;
        if(db != null){
            courseCursor = db.rawQuery(query, null);
        }
        return courseCursor;
    }

    void updateCourseData(String row_id, String name, String start_date, String end_date, String course_status, String instructor_name, String instructor_email, String instructor_phone, String notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COURSE_NAME, name);
        cv.put(CourseTable.COURSE_START_DATE, start_date);
        cv.put(CourseTable.COURSE_END_END_DATE, end_date);
        cv.put(CourseTable.COURSE_STATUS, course_status);
        cv.put(CourseTable.INSTRUCTOR_NAME, instructor_name);
        cv.put(CourseTable.INSTRUCTOR_EMAIL, instructor_email);
        cv.put(CourseTable.INSTRUCTOR_PHONE_NUMBER, instructor_phone);
        cv.put(CourseTable.NOTES, notes);

        long result = db.update(CourseTable.DATABASE_TABLE_COURSE, cv, "cid=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to update course", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Course updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteCourseRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + AssessmentTable.DATABASE_TABLE_ASSESSMENT + " WHERE " + AssessmentTable.COURSE_ID + " = "
                + row_id + ";";
        Cursor numCourses = db.rawQuery(query, null);
        if(numCourses.getCount() > 0){
            Toast.makeText(context, "Cannot Delete Courses With Active Assessments", Toast.LENGTH_SHORT).show();
        }else{
            long result = db.delete(CourseTable.DATABASE_TABLE_COURSE, "cid=?", new String[]{row_id});
            if(result == -1){
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Cursor readAssessments(int ID){
        String assessmentQuery = "SELECT * FROM " + AssessmentTable.DATABASE_TABLE_ASSESSMENT + " WHERE " + AssessmentTable.COURSE_ID + " = "
                + ID + ";";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor courseCursor = null;
        if(db != null){
            courseCursor = db.rawQuery(assessmentQuery, null);
        }
        return courseCursor;
    }

    void addAssessment(String course_id, String assessment_name, String assessment_type, String assessment_date, String start_time, String end_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AssessmentTable.COURSE_ID, course_id);
        cv.put(AssessmentTable.ASSESSMENT_NAME, assessment_name);
        cv.put(AssessmentTable.ASSESSMENT_TYPE, assessment_type);
        cv.put(AssessmentTable.ASSESSMENT_DATE, assessment_date);
        cv.put(AssessmentTable.START_TIME, start_time);
        cv.put(AssessmentTable.END_TIME, end_time);

        long result = db.insert(AssessmentTable.DATABASE_TABLE_ASSESSMENT, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Assessment Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Assessment Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void editAssessment(String row_id, String assessment_name, String assessment_type, String assessment_date, String start_time, String end_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(AssessmentTable.ASSESSMENT_NAME, assessment_name);
        cv.put(AssessmentTable.ASSESSMENT_TYPE, assessment_type);
        cv.put(AssessmentTable.ASSESSMENT_DATE, assessment_date);
        cv.put(AssessmentTable.START_TIME, start_time);
        cv.put(AssessmentTable.END_TIME, end_time);

        long result = db.update(AssessmentTable.DATABASE_TABLE_ASSESSMENT, cv, "assessmentID=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Add Assessment Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Assessment Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAssessmentRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(AssessmentTable.DATABASE_TABLE_ASSESSMENT, "assessmentID=?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readScheduler(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + SchedulerTable.DATABASE_TABLE_SCHEDULER + " WHERE " + SchedulerTable.SCHEDULER_ID + " = " + id + ";";

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readSchedulerByName(String termName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Properly wrap termName in single quotes
        String query = "SELECT * FROM scheduler WHERE termName = ?";

        // Use a parameterized query to avoid SQL injection and ensure correct syntax
        return db.rawQuery(query, new String[]{termName});
    }


    void addScheduler(String name, String startDate, String endDate, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SchedulerTable.TERM_NAME, name);
        cv.put(SchedulerTable.TERM_START_DATE, startDate);
        cv.put(SchedulerTable.TERM_END_DATE, endDate);
        cv.put(SchedulerTable.TERM_COLOR, color);

        long result = db.insert(SchedulerTable.DATABASE_TABLE_SCHEDULER, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllScheduler(){
        String query = "SELECT * FROM " + SchedulerTable.DATABASE_TABLE_SCHEDULER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor courseCursor = null;
        if(db != null){
            courseCursor = db.rawQuery(query, null);
        }
        return courseCursor;
    }

    void editScheduler(String row_id, String name, String startDate, String endDate, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SchedulerTable.TERM_NAME, name);
        cv.put(SchedulerTable.TERM_START_DATE, startDate);
        cv.put(SchedulerTable.TERM_END_DATE, endDate);
        cv.put(SchedulerTable.TERM_COLOR, color);

        long result = db.update(SchedulerTable.DATABASE_TABLE_SCHEDULER, cv, "schedulerID=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Edit Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Edit Success", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteSchedulerRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(SchedulerTable.DATABASE_TABLE_SCHEDULER, "schedulerID=?", new String[]{row_id});

        if(result == -1){
            Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
        }
    }

}
