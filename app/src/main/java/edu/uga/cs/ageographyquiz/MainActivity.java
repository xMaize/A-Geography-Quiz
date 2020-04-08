package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private GeographyQuizData geographyQuizData = null;
    private ArrayList<Question> questionList;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(DEBUG_TAG, "MainActivity.onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geographyQuizData = new GeographyQuizData(this);
        new QuestionDBReaderTask().execute();

        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private class QuestionDBReaderTask extends AsyncTask<Void, Void, ArrayList<Question>>{

        @Override
        protected  ArrayList<Question> doInBackground(Void... params){
            try {
                geographyQuizData.open();
                questionList = new ArrayList<Question>();

                Resources res = getResources();
                InputStream in_s = res.openRawResource(R.raw.country_continent);

                CSVReader reader = new CSVReader(new InputStreamReader(in_s));
                String[] nextLine;

                while ((nextLine = reader.readNext()) != null) {

                    Question question = new Question();
                    for(int i = 0; i < nextLine.length; i++){
                        if(i == 0) question.setCountry(nextLine[i]);
                        if(i == 1) question.setContinent(nextLine[i]);
                    }
                    questionList.add(question);
                }
            }
            catch (Exception e){
                Log.e(DEBUG_TAG, e.toString());
            }

            return questionList;

        }


        @Override
        protected void onPostExecute(ArrayList<Question> questionArrayList) {
            super.onPostExecute(questionArrayList);
            if(geographyQuizData.numQuestions() < 195) {
                for (int i = 0; i < questionArrayList.size(); i++) {
                    geographyQuizData.storeQuestion(questionArrayList.get(i));
                }
            }
        }
    }

    @Override
    protected void onResume(){
        Log.d(DEBUG_TAG, "MainActivity.onResume()");
        if(geographyQuizData != null){
            geographyQuizData.open();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onPause()" );
        // close the database in onPause
        if(geographyQuizData != null)
            geographyQuizData.close();
        super.onPause();
    }

}
