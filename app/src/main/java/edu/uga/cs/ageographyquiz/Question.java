package edu.uga.cs.ageographyquiz;

public class Question {

    private long id;
    private String country;
    private String continent;

    public Question(){
        this.id = -1;
        this.country = null;
        this.continent = null;
    }

    public Question(String country, String continent){
        this.country = country;
        this.continent = continent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String toString(){
        return id + ": " + country + " " + continent;
    }

}
