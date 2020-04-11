package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ResultActivity";

    private TextView result;
    private Button restart;
    private Button history;
    private long numCorrect;
    private ArrayList<Question> questionList;
    private GeographyQuizData geographyQuizData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        result = findViewById(R.id.resultmessage);
        restart = findViewById(R.id.restart);
        history = findViewById(R.id.history);
        numCorrect = intent.getLongExtra("numCorrect", 0);
        result.setText("Your score is " + numCorrect + " out of 6.");
        geographyQuizData = new GeographyQuizData(this);

        restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                geographyQuizData.open();
                List<Question> questions = geographyQuizData.retrieveQuestions();

                String[] countries = new String[6];
                String[] continents = new String[6];

                for(int i = 0; i < questions.size(); i++){
                    countries[i] = questions.get(i).getCountry();
                    continents[i] = questions.get(i).getContinent();
                }
                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                intent.putExtra("questions", countries);
                intent.putExtra("answers", continents);
                v.getContext().startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ReviewQuizzesActivity.class);
                v.getContext().startActivity(intent);
            }
        });
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
