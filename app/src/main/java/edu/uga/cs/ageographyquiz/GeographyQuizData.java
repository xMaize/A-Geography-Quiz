package edu.uga.cs.ageographyquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GeographyQuizData {

    public static final String DEBUG_TAG = "GeographyQuizData";

    private SQLiteDatabase db;
    private SQLiteOpenHelper geographyQuizDbHelper;
    private static final String[] allQuestionColumns = {
            GeographyQuizDBHelper.QUESTIONS_COLUMN_QUESTION_ID,
            GeographyQuizDBHelper.QUESTIONS_COLUMN_COUNTRY,
            GeographyQuizDBHelper.QUESTIONS_COLUMN_CONTINENT
    };
    private static final String[] allQuizColumns = {
            GeographyQuizDBHelper.QUIZZES_COLUMN_QUIZ_ID,
            GeographyQuizDBHelper.QUIZZES_COLUMN_DATE,
            GeographyQuizDBHelper.QUIZZES_COLUMN_CORRECT
    };
    private static final String[] allAnswersColumns = {
            GeographyQuizDBHelper.ANSWERS_COLUMN_QUESTION_ID,
            GeographyQuizDBHelper.ANSWERS_COLUMN_QUIZ_ID,
            GeographyQuizDBHelper.ANSWERS_COLUMN_ANSWER
    };

    public GeographyQuizData(Context context){
        this.geographyQuizDbHelper = GeographyQuizDBHelper.getInstance(context);
    }

    // Open the database
    public void open(){
        db = geographyQuizDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "GeographyQuizData: db open");
    }

    // Close the database
    public void close(){
        if(geographyQuizDbHelper != null){
            geographyQuizDbHelper.close();
            Log.d(DEBUG_TAG, "GeographyQuizData: db closed");
        }
    }

    public List<Quiz> retrieveAllQuizzes(){
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.query(GeographyQuizDBHelper.TABLE_QUIZZES, allQuizColumns,
                    null, null, null, null, null);

            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    long id = cursor.getLong(cursor.getColumnIndex(GeographyQuizDBHelper.QUIZZES_COLUMN_QUIZ_ID));
                    String date = cursor.getString(cursor.getColumnIndex(GeographyQuizDBHelper.QUIZZES_COLUMN_DATE));
                    long correct = cursor.getLong(cursor.getColumnIndex(GeographyQuizDBHelper.QUIZZES_COLUMN_CORRECT));

                    Quiz quiz = new Quiz(date, correct);
                    quiz.setId(id);
                    quizzes.add(quiz);
                    Log.d(DEBUG_TAG, "Retrieved Quiz: " + quiz);
                }
            }
            Log.d(DEBUG_TAG, "Number of quizzes from DB: " + cursor.getCount());
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        }
        finally{
            if(cursor != null){
                cursor.close();
            }
        }

        return quizzes;
    }

    public List<Answer> retrieveAllAnswers(){

        ArrayList<Answer> answers = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.query(GeographyQuizDBHelper.TABLE_QUIZZES, allQuizColumns,
                    null, null, null, null, null);

            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    long questionId = cursor.getLong(cursor.getColumnIndex(GeographyQuizDBHelper.ANSWERS_COLUMN_QUESTION_ID));
                    long quizId = cursor.getLong(cursor.getColumnIndex(GeographyQuizDBHelper.ANSWERS_COLUMN_QUIZ_ID));
                    String response = cursor.getString(cursor.getColumnIndex(GeographyQuizDBHelper.ANSWERS_COLUMN_ANSWER));

                    Answer answer = new Answer(questionId, quizId, response);
                    answers.add(answer);
                    Log.d(DEBUG_TAG, "Retrieved Quiz: " + answer);
                }
            }
            Log.d(DEBUG_TAG, "Number of quizzes from DB: " + cursor.getCount());
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        }
        finally{
            if(cursor != null){
                cursor.close();
            }
        }

        return answers;
    }

    public Question storeQuestion(Question question){

        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUESTIONS_COLUMN_COUNTRY, question.getCountry());
        values.put(GeographyQuizDBHelper.QUESTIONS_COLUMN_CONTINENT, question.getContinent());

        long id = db.insert(GeographyQuizDBHelper.TABLE_QUESTIONS, null, values);

        question.setId(id);

        Log.d(DEBUG_TAG, "Stored a new question with id: " + String.valueOf(question.getId()));

        return question;
    }

    public Quiz storeQuiz(Quiz quiz){

        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_DATE, quiz.getDate());
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_CORRECT, quiz.getCorrect());

        long id = db.insert(GeographyQuizDBHelper.TABLE_QUESTIONS, null, values);

        quiz.setId(id);

        Log.d(DEBUG_TAG, "Stored a new quiz with id: " + String.valueOf(quiz.getId()));

        return quiz;
    }

    public Answer storeAnswer(Answer answer){
        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_DATE, answer.getAnswer());

        db.insert(GeographyQuizDBHelper.TABLE_QUESTIONS, null, values);

        Log.d(DEBUG_TAG, "Stored a new answer: " + String.valueOf(answer.getAnswer()));

        return answer;
    }

}
