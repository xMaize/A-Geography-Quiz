package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an activity controller class that is used for the final result screen
 * This activity contains a text view with two buttons
 */
public class ResultActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ResultActivity";

    private TextView result;
    private Button restart;
    private Button history;
    private Long numCorrect;
    private String date;
    private GeographyQuizData geographyQuizData = null;

    /**
     * Overridden method for when a ResultActivity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        result = findViewById(R.id.resultmessage);
        restart = findViewById(R.id.restart);
        history = findViewById(R.id.history);
        //This is where we take the data from the intent sent from QuizActivity to display results
        numCorrect = intent.getLongExtra("numCorrect", 0);
        date = intent.getStringExtra("date");
        result.setText("The date and time is: " + date + "\nYour score is " + numCorrect + " out of 6.");

        //We create a geographyQuizData instance because the user may elect to start another quiz
        geographyQuizData = new GeographyQuizData(this);

        //Create the listener for the restart button being pressed which should start a new quiz
        //This is essentially the same method as the one used in MainActivity and starts the QuizActivity class
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
        //This is the listener for the history button being pressed which should
        // start the ReviewQuizzesActivity which displays previous quiz results
        history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ReviewQuizzesActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    //These are overrides to ensure that the app saves previous quiz data when interrupted
    //by another app
    @Override
    protected void onResume(){
        Log.d(DEBUG_TAG, "ResultActivity.onResume()");
        if(geographyQuizData != null){
            geographyQuizData.open();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "ResultActivity.onPause()" );
        // close the database in onPause
        if(geographyQuizData != null)
            geographyQuizData.close();
        super.onPause();
    }
}
