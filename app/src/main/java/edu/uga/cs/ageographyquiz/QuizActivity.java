package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is an activity that creates a new quiz
 */
public class QuizActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "QuizActivity";
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static String[] questions;
    public static String[] answers;
    private static String[] userAnswers;
    public long numCorrect;
    private GeographyQuizData geographyQuizData = null;
    public boolean[] isAnswered;
    public static final String[] continents = {"Asia", "Africa", "Europe", "South America"
            , "North America", "Oceania", "Antartica"};

    /**
     * Overridden method for when a QuizActivity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //On creating a new quiz, we must take the information from the intent and save them as local variables to use in our quiz
        Intent intent = getIntent();
        questions = intent.getStringArrayExtra("questions");
        answers = intent.getStringArrayExtra("answers");
        userAnswers = new String[6];
        isAnswered = new boolean[6];
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), questions.length);
        mViewPager = findViewById(R.id.Swiper);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        geographyQuizData = new GeographyQuizData(this);
        geographyQuizData.open();
        final Date currentTime = Calendar.getInstance().getTime();
        Log.d(DEBUG_TAG, currentTime.toString());

        //We are using a view pager to allow swiping after each question so we override all of the methods
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            /**
             * A method where we we save answer choices and checking if they are correct
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                //Inside this method is where we are retaining the values of the answer choices for each
                //question as well as checking if they are correct
                if(state == 1) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.Swiper + ":" + mViewPager.getCurrentItem());
                    View view = fragment.getView();
                    RadioGroup rg = view.findViewById(R.id.choices);
                    RadioButton selected = view.findViewById(rg.getCheckedRadioButtonId());
                    int currentQuestion = fragment.getArguments().getInt("Current Question") - 1;
                    Log.d(DEBUG_TAG, Integer.toString(currentQuestion));
                    if (selected != null) Log.d(DEBUG_TAG, selected.toString());

                    //as long as the user inputs an answer choice, it will check if it is correct
                    if (selected != null) {
                        userAnswers[currentQuestion] = selected.getText().toString().substring(3);
                        if (userAnswers[currentQuestion].equals(answers[currentQuestion]) && isAnswered[currentQuestion]==false) {
                            isAnswered[currentQuestion] = true;
                            numCorrect ++;
                        }
                        Log.d(DEBUG_TAG, Long.toString(numCorrect));
                    }

                    //Here is where we manage if the user is on the last question and swipes to the left
                    //in which they will be taken to a results screen taking the date as well as score
                    int lastIndex = mSectionsPagerAdapter.getCount() - 1;
                    if(lastIndex == fragment.getArguments().getInt("Current Question") - 1){
                        Quiz quiz = new Quiz(currentTime.toString(), numCorrect);
                        Log.d(DEBUG_TAG, Boolean.toString(quiz==null));
                        new QuizDBWriterTask().execute(quiz);
                        Intent intent = new Intent(view.getContext(), ResultActivity.class);
                        intent.putExtra("numCorrect", numCorrect);
                        intent.putExtra("date", currentTime.toString());
                        startActivity(intent);
                    }
                }
            }
        });

        // During initial setup, plug in the country fragment.
        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setArguments(getIntent().getExtras());
    }

    /**
     * A method where questions and answers are loaded and ensures
     * they are random and not duplicates
     * @param textView A TextView that contains the question
     * @param question The question going into the TextView
     * @param answer The correct answer choice
     * @param r1 Radiobutton answer choice 1
     * @param r2 Radiobutton answer choice 1
     * @param r3 Radiobutton answer choice 1
     */
    public void loadView(TextView textView, String question, String answer, RadioButton r1, RadioButton r2, RadioButton r3){
        //This method is where the questions and answer choices are loaded. It will ensure that the answers
        //are places randomly and are not duplicates of each other.
        textView.setText(question);

        boolean isUnique = true;
        String[] answerChoices = new String[3];

        answerChoices[(int) (Math.random() * 3)] = answer;

        for(int i = 0; i < answerChoices.length; i++){
            if(answerChoices[i] != null){
                continue;
            }
            do{
                isUnique = true;
                answerChoices[i] = continents[(int) (Math.random() * 7)];
                for(int j = 0; j < answerChoices.length; j++){
                    if(answerChoices == null){
                        continue;
                    }
                    else if(answerChoices[i].equals(answerChoices[j]) && i != j){
                        isUnique = false;
                    }
                }
            }while(!isUnique);
        }

        r1.setText("A. " + answerChoices[0]);
        r2.setText("B. " + answerChoices[1]);
        r3.setText("C. " + answerChoices[2]);
    }

    /**
     * A class that helps utilize the ViewPager
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter{
        //this class helps us use the view pager
        private final int mSize;

        public SectionsPagerAdapter(FragmentManager fm, int size){
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position){
            return QuizFragment.newInstance(position+1);
        }

        @Override
        public int getCount() { return mSize; }

        @Override
        public CharSequence getPageTitle(int position){
            int questionNum = position + 1;
            return String.valueOf("Question" + questionNum);
        }
    }

    /**
     * This is an AsyncTask class to perform DB writing of a new quiz
     */
    private class QuizDBWriterTask extends AsyncTask<Quiz, Void, Quiz> {

        @Override
        protected Quiz doInBackground(Quiz... quiz) {
            Log.d(DEBUG_TAG, Boolean.toString(quiz[0]==null));
            geographyQuizData.storeQuiz(quiz[0]);
            return quiz[0];
        }

        @Override
        protected void onPostExecute(Quiz quiz) {
            super.onPostExecute(quiz);
            Log.d(DEBUG_TAG, "quiz saved: " + quiz.toString());
        }
    }
    //Just like other classes, these methods are overridden to ensure
    //the app saves answers if it is interrupted
    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "QuizActivity.onResume()" );
        if(geographyQuizData!= null )
            geographyQuizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "QuizActivity.onPause()" );
        if(geographyQuizData != null )
            geographyQuizData.close();
        super.onPause();
    }

}


