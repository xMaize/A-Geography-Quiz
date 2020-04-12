package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * This is an activity that lists the previous quizzes taken
 * and their results
 * The quizzes are listed as a RecyclerView
 */
public class ReviewQuizzesActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "ReviewQuizzesActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private GeographyQuizData geographyQuizData = null;
    private List<Quiz> quizList;

    /**
     * Overridden method for when a ReviewQuizzesActivity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_quizzes);

        recyclerView = findViewById(R.id.recyclerView);

        //we use a linear layout for the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );

        //We create a geographyQuizData since we need so save quizzes to the database
        geographyQuizData = new GeographyQuizData( this );

        //Retrieve the quizzes in an asynchronous way
        new QuizDBReaderTask().execute();
    }

    /**
     * An AsyncTask class that runs in the background that
     * reads from the database
     */
    private class QuizDBReaderTask extends AsyncTask<Void, Void, List<Quiz>> {

        // This method will run as a background process to read from db.
        // It returns a list of retrieved quiz objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the job leads review activity is started).
        @Override
        protected List<Quiz> doInBackground( Void... params ) {
            geographyQuizData.open();
            quizList = geographyQuizData.retrieveAllQuizzes();

            Log.d( DEBUG_TAG, "Quizzes retrieved: " + quizList.size() );

            return quizList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        @Override
        protected void onPostExecute( List<Quiz> quizList ) {
            super.onPostExecute(quizList);
            recyclerAdapter = new QuizRecyclerAdapter(quizList);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
    //The overrides for onResume and onPause are here to ensure that the app still continues to run even
    //after it may be interrupted by another app
    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onResume()" );
        // open the database in onResume
        if(geographyQuizData!= null )
            geographyQuizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onPause()" );
        // close the database in onPause
        if(geographyQuizData != null )
            geographyQuizData.close();
        super.onPause();
    }

    // These activity callback methods are not needed and are for educational purposes only
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onStop()" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onDestroy()" );
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "ReviewQuizzesActivity.onRestart()" );
        super.onRestart();
    }
}
