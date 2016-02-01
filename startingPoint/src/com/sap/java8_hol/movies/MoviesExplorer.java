package com.sap.java8_hol.movies;

import java.util.ArrayList;
import java.util.List;

public class MoviesExplorer {

    public static void main(String[] args) throws Exception {
        // 1) Load the movies
        List<Movie> movies = new ArrayList<>();

        // 2) Find the number of movies released in 2003

        // 3) Find the first movie that contains Lord of the Rings

        // 4) Display the films sorted by the release year

        // 5) Find the first and the last year in the statistics

        // 6) Print the movies grouped by year

        // 7) Extract all the actors

        // 8) Find all the movies with Kevin Spacey
        
        // 9) Print movies projections

    }

    private static void addMovie(List<Movie> movies, String movieInfo) {
        String elements[] = movieInfo.split("/");
        String title = parseMovieTitle(elements);
        String releaseYear = parseMovieReleaseYear(elements);

        Movie movie = new Movie(title, Integer.valueOf(releaseYear));

        for (int i = 1; i < elements.length; i++) {
            String[] name = elements[i].split(", ");
            String lastName = name[0].trim();
            String firstName = "";
            if (name.length > 1) {
                firstName = name[1].trim();
            }

            Actor actor = new Actor(firstName, lastName);
            movie.addActor(actor);
        }

        movies.add(movie);
    }

    private static String parseMovieTitle(String[] elements) {
        return elements[0].substring(0, elements[0].toString().lastIndexOf("(")).trim();
    }

    private static String parseMovieReleaseYear(String[] elements) {
        String releaseYear = elements[0].substring(elements[0].toString().lastIndexOf("(") + 1,
                elements[0].toString().lastIndexOf(")"));
        if (releaseYear.contains(",")) {
            releaseYear = releaseYear.substring(0, releaseYear.indexOf(","));
        }
        return releaseYear;
    }
}
