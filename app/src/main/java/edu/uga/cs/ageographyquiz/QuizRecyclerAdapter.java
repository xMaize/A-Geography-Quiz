package edu.uga.cs.ageographyquiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all of the previous quizzes
 */
public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.QuizHolder> {

    public static final String DEBUG_TAG = "QuizRecyclerAdapter";

    private List<Quiz> quizList;

    public QuizRecyclerAdapter(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    /**
     * The adapter must have a ViewHolder class to "hold" one item to show.
     * The holder displays the quizId, date, and number of correct answers
     */
    class QuizHolder extends RecyclerView.ViewHolder {

        TextView quizId;
        TextView date;
        TextView numCorrect;

        public QuizHolder(View itemView ) {
            super(itemView);

            quizId = itemView.findViewById(R.id.quizID);
            date = itemView.findViewById(R.id.date);
            numCorrect = itemView.findViewById(R.id.numCorrect);
        }
    }

    /**
     * Creates a QuizHolder to hold our quiz data
     * @param parent
     * @param viewType
     * @return QuizHolder a QuizHolder
     */
    @Override
    public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType ) {

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.quiz, parent, false);
        return new QuizHolder(view);
    }

    /**
     * This method fills in the values of a holder to show a quiz.
     * The position parameter indicates the position on the list of quiz list.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(QuizHolder holder, int position ) {
        Quiz quiz = quizList.get( position );

        Log.d(DEBUG_TAG, "onBindViewHolder: " + quiz);

        holder.quizId.setText("Quiz ID: " + quiz.getId());
        holder.date.setText("Quiz Date: " + quiz.getDate());
        holder.numCorrect.setText("Correct Answers: " + quiz.getCorrect());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

}
