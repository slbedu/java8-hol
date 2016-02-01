package com.sap.java8_hol.movies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoviesExplorer {

    public static void main(String[] args) throws Exception {
        // 1) Load the movies
        List<Movie> movies = new ArrayList<>();
//        loadMoviesOld(Paths.get("resources", "movies-mpaa.txt"), movies);
        loadMovies(Paths.get("resources", "movies-mpaa.txt"), movies);

        // 2) Find the number of movies released in 2003
//        long movies2003 = countMovies2003Old(movies);
        long movies2003 = countMovies2003(movies);
        System.out.println("Number of movies released in 2003: " + movies2003);

        // 3) Find the first movie that contains Lord of the Rings
//        Movie firstLOTR = findFirstLordOfTheRingsMovieOld(movies);
//        if (firstLOTR != null) {
//            System.out.println("The year of the first Lord of the Rings movie is: " + firstLOTR.getYear());
//        } else {
//            throw new IllegalArgumentException();
//        }
        Optional<Movie> firstLOTR = findFirstLordOfTheRingsMovie(movies);
        firstLOTR.ifPresent(
                movie -> System.out.println("The year of the first Lord of the Rings movie is: " + movie.getYear()));
        firstLOTR.orElseThrow(IllegalArgumentException::new);

        // 4) Display the movies sorted by the release year
//        printMoviesSortedByReleaseOld(movies);
        printMoviesSortedByRelease(movies);

        // 5) Find the first and the last year in the statistics
//        int firstYear = getMinYearOld(movies);
        int firstYear = getMinYear(movies);
        System.out.println("First year in the statistics: " + firstYear);
//        int lastYear = getMaxYearOld(movies);
        int lastYear = getMaxYear(movies);
        System.out.println("First year in the statistics: " + lastYear);

        // 6) Print the movies grouped by year
//        Map<Integer, List<Movie>> collect = getMoviesByYearOld(movies);
        Map<Integer, List<Movie>> collect = getMoviesByYear(movies);
        System.out.println(collect);

        // 7) Extract all the actors
//        Set<Actor> actorsSet = getAllActorsOld(movies);
        Set<Actor> actorsSet = getAllActors(movies);
        System.out.println("Number of actors: " + actorsSet.size());

        // 8) Find all the movies with Kevin Spacey
//        List<Movie> kevinSpaceyMovies = getAllKevinSpaceyMoviesOld(movies);
        List<Movie> kevinSpaceyMovies = getAllKevinSpaceyMovies(movies);
        System.out.println(kevinSpaceyMovies);
        
        // 9) Print movies projections
        printMovieProjectionData(movies);
    }

    private static void addMovie(List<Movie> movies, String movieInfo) {
        String elements[] = movieInfo.split("/");
        String title = parseMovieTitle(elements);
        String releaseYear = parseMovieReleaseYear(elements);
        String duration = parseMovieDuration(elements);

        Movie movie = new Movie(title, Integer.valueOf(releaseYear) , Integer.valueOf(duration));

        for (int i = 2; i < elements.length; i++) {
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
    
    private static String parseMovieDuration(String[] elements) {
        String duration = elements[1];
        return duration;
    }
    
    
    private static void loadMoviesOld(Path filePath, List<Movie> movies) throws IOException {
        try (BufferedReader moviesFileReader = new BufferedReader(new FileReader(filePath.toString()))) {
            String movieLine;
            while ((movieLine = moviesFileReader.readLine()) != null) {
                addMovie(movies, movieLine);
            }
        }
    }
    
    private static void loadMovies(Path filePath, List<Movie> movies) throws IOException {
        // От документацията на java.util.Stream:
        // Streams have a BaseStream.close() method and implement AutoCloseable,
        // but nearly all stream instances do not actually need to be closed after use.
        // Generally, only streams whose source is an IO channel (such as those returned by Files.lines(Path, Charset))
        // will require closing. Most streams are backed by collections, arrays, or generating functions,
        // which require no special resource management.
        // (If a stream does require closing, it can be declared as a resource in a try-with-resources statement.) 
        try (Stream<String> lines = Files.lines(filePath , Charset.defaultCharset())) {
            lines.forEach(line -> addMovie(movies, line));
        }catch( Exception e){
        	e.printStackTrace();
        }
    }
    
    
    private static long countMovies2003Old(List<Movie> movies) {
        long count = 0;
        for (Movie movie : movies) {
            if (movie.getYear() == 2003) {
                count++;
            }
        }
        return count;
    }
    
    private static long countMovies2003(List<Movie> movies) {
        return movies.stream()
                .filter(movie -> movie.getYear() == 2003)
                .count();
    }
    
    
    private static Movie findFirstLordOfTheRingsMovieOld(List<Movie> movies) {
        for (Movie movie : movies) {
            if (movie.getTitle().contains("Lord of the Rings")) {
                return movie;
            }
        }
        return null;
    }
    
    private static Optional<Movie> findFirstLordOfTheRingsMovie(List<Movie> movies) {
        return movies.stream()
                .filter(movie -> movie.getTitle().contains("Lord of the Rings"))
                .findFirst();
    }


    private static void printMoviesSortedByReleaseOld(List<Movie> movies) {
        List<Movie> sortedMovies = new ArrayList<Movie>(movies);
        Collections.sort(sortedMovies, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return o1.getYear() - o2.getYear();
                }
            });
        for (Movie movie : sortedMovies) {
            System.out.println(movie);
        }
    }
    
    private static void printMoviesSortedByRelease(List<Movie> movies) {
        movies.stream()
                .sorted((m1, m2) -> m1.getYear() - m2.getYear())
                .forEach(System.out::println);
    }

    
    private static int getMinYearOld(List<Movie> movies) {
        int minYear = Integer.MAX_VALUE;
        for (Movie movie : movies) {
            if (movie.getYear() < minYear) {
                minYear = movie.getYear();
            }
        }
        return minYear;
    }
    
    private static int getMinYear(List<Movie> movies) {
        return movies.stream()
                .mapToInt(Movie::getYear)
                .min()
                .getAsInt();
    }
    
    private static int getMaxYearOld(List<Movie> movies) {
        int maxYear = 0;
        for (Movie movie : movies) {
            if (movie.getYear() > maxYear) {
                maxYear = movie.getYear();
            }
        }
        return maxYear;
    }
    
    private static int getMaxYear(List<Movie> movies) {
        return movies.stream()
                .mapToInt(Movie::getYear)
                .max()
                .getAsInt();
    }

    
    private static Map<Integer, List<Movie>> getMoviesByYearOld(List<Movie> movies) {
        Map<Integer, List<Movie>> result = new HashMap<>();
        for (Movie movie : movies) {
            List<Movie> moviesInSameYear = result.get(movie.getYear());
            if (moviesInSameYear == null) {
                moviesInSameYear = new LinkedList<>();
                result.put(movie.getYear(), moviesInSameYear);
            }
            moviesInSameYear.add(movie);
        }
        return result;
    }
    
    private static Map<Integer, List<Movie>> getMoviesByYear(List<Movie> movies) {
        return movies.stream()
                .collect(Collectors.groupingBy(Movie::getYear));
    }

    
    private static Set<Actor> getAllActorsOld(List<Movie> movies) {
        Set<Actor> actors = new HashSet<>();
        for (Movie movie : movies) {
            actors.addAll(movie.getActors());
        }
        return actors;
    }
    
    private static Set<Actor> getAllActors(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getActors().stream())
                .collect(Collectors.toSet());
    }
    
    
    private static List<Movie> getAllKevinSpaceyMoviesOld(List<Movie> movies) {
        List<Movie> result = new ArrayList<>();
        Actor kevinSpacey = new Actor("Kevin", "Spacey");
        for (Movie movie : movies) {
            if (movie.getActors().contains(kevinSpacey)) {
                result.add(movie);
            }
        }
        return result;
    }
    
    private static List<Movie> getAllKevinSpaceyMovies(List<Movie> movies) {
        Actor kevinSpacey = new Actor("Kevin", "Spacey");
        return movies.stream()
                .filter(movie -> movie.getActors().stream()
                        .anyMatch(actor -> actor.equals(kevinSpacey)))
                .collect(Collectors.toList());
    }
    
    
    private static void printMovieProjectionDataOld(List<Movie>movies){
    	try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a");	
			String dateInString = "01.01.2017 5:00:00 PM";
			Date startDateTime = formatter.parse(dateInString);				
			System.out.println("Date : " + formatter.format(startDateTime));
	
			// To TimeZone Europe/Berlin
			SimpleDateFormat sdfBerlin = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
			TimeZone tzInBerlin = TimeZone.getTimeZone("Europe/Berlin");
			sdfBerlin.setTimeZone(tzInBerlin);			
			
			for (Movie movie : movies) {			
		         		          
			int duration = movie.getDuration();
		          Date endDateTime = new Date( startDateTime.getTime() + (duration * 60 * 1000) );
		          
		          String startDateTimeInBerlin = sdfBerlin.format(startDateTime); 
		          String endDateTimeInBerlin = sdfBerlin.format(endDateTime);
		          
		          System.out.println( movie);
		          System.out.println("Sofia Start Date and Time :"+ formatter.format(startDateTime)  + " End Date and Time : "+ formatter.format(endDateTime));
		          System.out.println("Berlin Start Date and Time :"+ startDateTimeInBerlin  + " Date and End Time : "+ endDateTimeInBerlin);
		          
		         // calculate the next day
		          startDateTime = new Date( startDateTime.getTime() + 1 * 24 * 60 * 60 * 1000); // add 24 hours
		        }
			
		
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
    private static void printMovieProjectionData(List<Movie>movies){
    	ZoneId zoneIDBerlin = ZoneId.of("Europe/Berlin");
    	LocalDateTime  startDateTime = LocalDateTime.of(2017, 1, 1, 17, 0) ;
    	DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss a"); 
    	
    	for (Movie movie : movies) {
          int duration = movie.getDuration();
          LocalDateTime  endDateTime = startDateTime.plusMinutes(duration);
          
          System.out.println( movie);
          System.out.println("Sofia Start Date and Time :"+ startDateTime.format(formater)  + " End Date and Time : "+ endDateTime.format(formater));
          System.out.println("Berlin Start Date and Time :"+ ZonedDateTime.of(startDateTime, zoneIDBerlin).format(DateTimeFormatter.ISO_INSTANT)+ 
        		             " Date and End Time : "+  ZonedDateTime.of(endDateTime, zoneIDBerlin).format(DateTimeFormatter.ISO_INSTANT));
          
          startDateTime = startDateTime.plusDays(1);          
          
          System.out.println();
          
        }
    }
}
