package edu.uga.cs.ageographyquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static edu.uga.cs.ageographyquiz.GeographyQuizDBHelper.QUESTIONS_COLUMN_CONTINENT;
import static edu.uga.cs.ageographyquiz.GeographyQuizDBHelper.QUESTIONS_COLUMN_COUNTRY;
import static edu.uga.cs.ageographyquiz.GeographyQuizDBHelper.QUESTIONS_COLUMN_QUESTION_ID;
import static edu.uga.cs.ageographyquiz.GeographyQuizDBHelper.TABLE_QUESTIONS;

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

    public List<Question> retrieveQuestions(){
        ArrayList<Question> questions = new ArrayList<>();

        int[] questionNums = new int[6];
        boolean allUnique = false;

        for(int i = 0; i < questionNums.length; i++){
            if(i == 0){
                questionNums[i] = (int) (Math.random() * 195) + 1;
            }
            else{
                while(allUnique == false){
                    questionNums[i] = (int) (Math.random() * 195) + 1;
                    for(int j = i - 1; j >= 0; j--){
                        if(questionNums[i] != questionNums[j]){
                            allUnique = true;
                        }
                        else{
                            allUnique = false;
                            break;
                        }
                    }
                }
            }
            allUnique = false;
        }

        Log.d(DEBUG_TAG, questionNums[0] + " " + questionNums[1] + " " + questionNums[2] + " " + questionNums[3] + " " + questionNums[4] + " " + questionNums[5]);

        for(int i = 0; i < questionNums.length; i++){
            String query = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + QUESTIONS_COLUMN_QUESTION_ID + " = " + questionNums[i];
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    long id = cursor.getLong(cursor.getColumnIndex(QUESTIONS_COLUMN_QUESTION_ID));
                    String country = cursor.getString(cursor.getColumnIndex(QUESTIONS_COLUMN_COUNTRY));
                    String continent = cursor.getString(cursor.getColumnIndex(QUESTIONS_COLUMN_CONTINENT));

                    Question question = new Question(country, continent);
                    question.setId(id);
                    questions.add(question);
                    Log.d(DEBUG_TAG, "Retrieved Question: " + question);
                }
            }
        }

        return questions;
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

        long id = db.insert(TABLE_QUESTIONS, null, values);

        question.setId(id);

        Log.d(DEBUG_TAG, "Stored a new question with id: " + String.valueOf(question.getId()));

        return question;
    }

    public Quiz storeQuiz(Quiz quiz){

        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_DATE, quiz.getDate());
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_CORRECT, quiz.getCorrect());

        long id = db.insert(TABLE_QUESTIONS, null, values);

        quiz.setId(id);

        Log.d(DEBUG_TAG, "Stored a new quiz with id: " + String.valueOf(quiz.getId()));

        return quiz;
    }

    public Answer storeAnswer(Answer answer){
        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUIZZES_COLUMN_DATE, answer.getAnswer());

        db.insert(TABLE_QUESTIONS, null, values);

        Log.d(DEBUG_TAG, "Stored a new answer: " + String.valueOf(answer.getAnswer()));

        return answer;
    }

    public long numQuestions(){

        long count = DatabaseUtils.queryNumEntries(db, TABLE_QUESTIONS);

        return count;

    }

}
