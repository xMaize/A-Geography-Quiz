package edu.uga.cs.ageographyquiz;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private static final String DEBUG_TAG = "QuizFragment";
    private static final String Current_Question = "Current Question";
    private int mQuestionNum;
    private TextView question;
    private RadioButton firstChoice;
    private RadioButton secondChoice;
    private RadioButton thirdChoice;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mQuestionNum = getArguments().getInt(Current_Question);
        } else {
            mQuestionNum = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        question = view.findViewById(R.id.question);
        firstChoice = view.findViewById(R.id.firstChoice);
        secondChoice = view.findViewById(R.id.secondChoice);
        thirdChoice = view.findViewById(R.id.thirdChoice);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        if(QuizActivity.class.isInstance(getActivity())){
            final String question = "What continent does " + QuizActivity.questions[mQuestionNum - 1] + " reside in?";
            final String answer = QuizActivity.answers[mQuestionNum - 1];
            ((QuizActivity) getActivity()).loadView(this.question, question, answer, firstChoice, secondChoice, thirdChoice);
        }
    }

    public static QuizFragment newInstance(int currentQuestion) {
        Log.d(DEBUG_TAG, "New Instance created.");
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(Current_Question, currentQuestion);
        fragment.setArguments(args);
        return fragment;
    }
    public QuizFragment(){
    }

}
