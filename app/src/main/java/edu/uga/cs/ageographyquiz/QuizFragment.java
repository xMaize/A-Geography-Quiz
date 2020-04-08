package edu.uga.cs.ageographyquiz;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private static final String DEBUG_TAG = "QuizFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);


        return view;
    }

    public long getQuizId(){

        long id = -1;

        Bundle args = getArguments();

        if(args != null){
            id = args.getLong("id", -1);
        }
        if(id == -1){
            Log.e(DEBUG_TAG, "Not an id");
        }

        return id;
    }

}
