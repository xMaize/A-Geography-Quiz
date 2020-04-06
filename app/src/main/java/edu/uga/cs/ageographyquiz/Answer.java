package edu.uga.cs.ageographyquiz;

public class Answer {

    private long questionId;
    private long quizId;
    private String answer;

    public Answer(long questionId, long quizId, String answer){
        this.questionId = questionId;
        this.quizId = quizId;
        this.answer = answer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString(){
        return "Question Id: " + questionId + ", Quiz Id: " + quizId + ", Answer: " + answer;
    }
}
