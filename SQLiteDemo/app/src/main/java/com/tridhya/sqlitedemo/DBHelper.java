package com.tridhya.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "School";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our Grades table.
    private static final String TABLE_GRADES = "Grades";

    // below variable is for our studentID column.
    private static final String STUDENT_ID = "StudentId";

    // below variable is for our student name column
    private static final String STUDENT_NAME = "StudentName";

    // below variable id for our program column.
    private static final String PROGRAM = "Program";

    // below variable for our course 1 column.
    private static final String COURSE_1 = "Course1";

    // below variable is for our course 2 column.
    private static final String COURSE_2 = "Course2";

    // below variable is for our course 3 column.
    private static final String COURSE_3 = "Course3";

    // below variable is for our course 4 column.
    private static final String COURSE_4 = "Course4";

    // below variable is for our Grades table.
    private static final String TABLE_IMPROVEMENTS = "Improvements";

    // below variable is for our studentID column.
    private static final String IMPROVEMENT_ID = "ImprovementId";

    // below variable is for our student name column
    private static final String COURSE = "Course";

    // below variable id for our program column.
    private static final String MARKS = "Marks";

    // creating a constructor for our database handler.
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String gradesQuery = "CREATE TABLE " + TABLE_GRADES + " ("
                + STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STUDENT_NAME + " TEXT,"
                + PROGRAM + " TEXT,"
                + COURSE_1 + " TEXT,"
                + COURSE_2 + " TEXT,"
                + COURSE_3 + " TEXT,"
                + COURSE_4 + " TEXT)";

        String improvementQuery = "CREATE TABLE " + TABLE_IMPROVEMENTS + " ("
                + IMPROVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STUDENT_ID + " TEXT,"
                + COURSE + " TEXT,"
                + MARKS + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(gradesQuery);
        db.execSQL(improvementQuery);
    }

    // this method is use to add new grade to our Grades table with relevant details.
    public void addGrades(String studentName, String program, String course1, String course2, String course3, String course4) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(STUDENT_NAME, studentName);
        values.put(PROGRAM, program);
        values.put(COURSE_1, course1);
        values.put(COURSE_2, course2);
        values.put(COURSE_3, course3);
        values.put(COURSE_4, course4);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_GRADES, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<StudentDetailsData> fetchStudentDetails() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_GRADES, null);

        // on below line we are creating a new array list.
        ArrayList<StudentDetailsData> studentDataArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                studentDataArrayList.add(new StudentDetailsData(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(5),
                        cursorCourses.getString(6)));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return studentDataArrayList;
    }

    // below is the method for updating our course in Grades table
    public void updateImprovements(String studentId, String courseName, String marks) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        if (courseName.equalsIgnoreCase("Course 1")) {
            values.put(COURSE_1, marks);
        }
        if (courseName.equalsIgnoreCase("Course 2")) {
            values.put(COURSE_2, marks);
        }
        if (courseName.equalsIgnoreCase("Course 3")) {
            values.put(COURSE_3, marks);
        }
        if (courseName.equalsIgnoreCase("Course 4")) {
            values.put(COURSE_4, marks);
        }

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_GRADES, values, "StudentId=?", new String[]{studentId});
        db.close();
    }

    // this method is use to add new grade to our Improvements table with relevant details.
    public void addImprovements(String studentId, String course, String marks) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(STUDENT_ID, studentId);
        values.put(COURSE, course);
        values.put(MARKS, marks);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_IMPROVEMENTS, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<String> fetchImprovements() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_IMPROVEMENTS, null);

        // on below line we are creating a new array list.
        ArrayList<String> improvementList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                improvementList.add(cursorCourses.getString(0));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return improvementList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMPROVEMENTS);
        onCreate(db);
    }
}
