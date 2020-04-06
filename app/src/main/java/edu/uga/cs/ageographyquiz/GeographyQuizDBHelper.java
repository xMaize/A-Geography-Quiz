package edu.uga.cs.ageographyquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GeographyQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "JobLeadsDBHelper";
    private static final String DB_NAME = "geographyquiz.db";
    private static final int DB_VERSION = 1;
    private static GeographyQuizDBHelper helperInstance;

    // Table and column names for questions.
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_QUESTION_ID = "question_id";
    public static final String QUESTIONS_COLUMN_COUNTRY = "country";
    public static final String QUESTIONS_COLUMN_CONTINENT = "continent";

    // Table and column names for quizzes.
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_QUIZ_ID = "quiz_id";
    public static final String QUIZZES_COLUMN_DATE = "date";
    public static final String QUIZZES_COLUMN_CORRECT = "correct";

    // Table and column names for quiz questions
    public static final String TABLE_ANSWERS = "answers";
    public static final String ANSWERS_COLUMN_ANSWER = "answer";
    public static final String ANSWERS_COLUMN_QUIZ_ID = "quiz_id";
    public static final String ANSWERS_COLUMN_QUESTION_ID = "question_id";

    // A Create table SQL statement to create a table for questions.
    private static final String CREATE_QUESTIONS =
            "create table " + TABLE_QUESTIONS + " ("
                + QUESTIONS_COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUESTIONS_COLUMN_COUNTRY + " TEXT, "
                + QUESTIONS_COLUMN_CONTINENT + " TEXT "
            + ")";

    // A Create table SQL statement to create a table for quizzes.
    private static final String CREATE_QUIZZES =
            "create table " + TABLE_QUIZZES + " ("
                + QUIZZES_COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUIZZES_COLUMN_DATE + " TEXT, "
                + QUIZZES_COLUMN_CORRECT + " INTEGER "
            + ")";

    // A Create table SQL statement to create a table for quiz questions.
    private static final String CREATE_ANSWERS =
            "create table " + TABLE_ANSWERS + " ("
                + ANSWERS_COLUMN_QUESTION_ID + " TEXT, "
                + ANSWERS_COLUMN_QUIZ_ID + " TEXT, "
                + ANSWERS_COLUMN_ANSWER + " TEXT, "
            + "FOREIGN KEY (" + ANSWERS_COLUMN_QUESTION_ID + ") "
            + "REFERENCES " + TABLE_QUESTIONS + " (" + ANSWERS_COLUMN_QUESTION_ID + "), "
            + "FOREIGN KEY (" + ANSWERS_COLUMN_QUIZ_ID + ") "
            + "REFERENCES " + TABLE_QUIZZES + " (" + ANSWERS_COLUMN_QUIZ_ID + "))";

    private GeographyQuizDBHelper(Context context){super(context, DB_NAME, null, DB_VERSION); }

    public static synchronized GeographyQuizDBHelper getInstance(Context context){
        if(helperInstance == null){
            helperInstance = new GeographyQuizDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_QUESTIONS);
        db.execSQL(CREATE_QUIZZES);
        db.execSQL(CREATE_ANSWERS);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + TABLE_QUESTIONS);
        onCreate(db);
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " upgraded" );
    }
}
