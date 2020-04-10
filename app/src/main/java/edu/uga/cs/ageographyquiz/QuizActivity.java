package edu.uga.cs.ageographyquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "QuizActivity";
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static String[] questions;
    public static String[] answers;
    public static final String[] continents = {"Asia", "Africa", "Europe", "South America"
            , "North America", "Oceania", "Antartica"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        questions = intent.getStringArrayExtra("questions");
        answers = intent.getStringArrayExtra("answers");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), questions.length);
        mViewPager = findViewById(R.id.Swiper);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 1) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.Swiper + ":" + mViewPager.getCurrentItem());
                    View view = fragment.getView();
                    RadioGroup rg = view.findViewById(R.id.choices);
                    RadioButton selected = view.findViewById(rg.getCheckedRadioButtonId());
//                    Intent intent = getIntent();
//                    int currentQuestion = fragment.getArguments().getInt("Current Question") - 1;
//                    Log.d(DEBUG_TAG, Integer.toString(currentQuestion));
//                    String[] questions = intent.getStringArrayExtra("questions");
//                    Log.d(DEBUG_TAG, questions[currentQuestion]);
                    if (selected != null) Log.d(DEBUG_TAG, selected.toString());
                    if (selected != null) Log.d(DEBUG_TAG, selected.getText().toString());

                    int lastIndex = mSectionsPagerAdapter.getCount() - 1;
                    if(lastIndex == fragment.getArguments().getInt("Current Question") - 1){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }
        });

        // During initial setup, plug in the country fragment.
        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setArguments(getIntent().getExtras());
    }

    public void loadView(TextView textView, String question, String answer, RadioButton r1, RadioButton r2, RadioButton r3){
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

        r1.setText(answerChoices[0]);
        r2.setText(answerChoices[1]);
        r3.setText(answerChoices[2]);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter{
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
}


