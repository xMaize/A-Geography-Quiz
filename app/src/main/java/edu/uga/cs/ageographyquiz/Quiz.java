package edu.uga.cs.ageographyquiz;

public class Quiz {

    private long id;
    private String date;
    private long correct;

    public Quiz(){
        this.id = -1;
        this.date = null;
        this.correct = 0;
    }

    public Quiz(String date, long correct){
        this.id = -1;
        this.date = date;
        this.correct = correct;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCorrect() {
        return correct;
    }

    public void setCorrect(long correct) {
        this.correct = correct;
    }

    public String toString(){
        return id + ": " + date + " " + correct;
    }
}
