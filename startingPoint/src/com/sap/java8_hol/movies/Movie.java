package com.sap.java8_hol.movies;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String title;
    private int year;
    private List<Actor> actors;

    public Movie(String title, int year) {
        this.title = title;
        this.year = year;
        actors = new ArrayList<>();
    }

    public Movie(String title, int year, List<Actor> actors) {
        this.title = title;
        this.year = year;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    @Override
    public String toString() {
        return "Movie [title=" + title + ", year=" + year + "]";
    }
}
