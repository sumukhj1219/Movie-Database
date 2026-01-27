//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
  static List<Map<String, String>> movies;
  static List<Map<String, String>> actors;
  static List<Map<String, String>> directors;

  static List<Map<String, String>> readCSV(String path) throws IOException {
    List<Map<String, String>> rows = new ArrayList();

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String[] headers = br.readLine().split(",");

      String line;
      while((line = br.readLine()) != null) {
        String[] values = line.split(",");
        Map<String, String> row = new HashMap();

        for(int i = 0; i < headers.length && i < values.length; ++i) {
          row.put(headers[i].trim(), values[i].trim());
        }

        rows.add(row);
      }
    }

    return rows;
  }

  /**
   * Utilites
   */
  static void menu() {
  }

  static void print(List<Map<String, String>> list) {
    list.forEach((map) -> System.out.println(map));
  }

  /**
   * Get Movie Information
   */
  static void getMovieInformation(int movieId, String genre) {
    try {
      String movieIdStr = String.valueOf(movieId);
      List<Map<String, String>> result = movies.stream()
              .filter(m -> movieIdStr.equals(m.get("movieId")))
              .filter(m -> genre == null || genre.isEmpty() || (m.get("genre") != null && m.get("genre").contains(genre)))
              .collect(Collectors.toList());
      print(result);
    } catch (Exception e) {
      System.out.println("Error in getMovieInformation: " + e.getMessage());
    }
  }

  /**
   * Get Top 10 Rated Movies
   */
  static void getTop10RatedMovies() {
    try {
      List<Map<String, String>> result = movies.stream()
              .sorted((m1, m2) -> {
                try {
                  double r1 = Double.parseDouble(m1.getOrDefault("rating", "0"));
                  double r2 = Double.parseDouble(m2.getOrDefault("rating", "0"));
                  return Double.compare(r2, r1);
                } catch (NumberFormatException e) {
                  return 0; // treat invalid rating as 0
                }
              })
              .limit(10)
              .collect(Collectors.toList());
      print(result);
    } catch (Exception e) {
      System.out.println("Error in getTop10RatedMovies: " + e.getMessage());
    }
  }

  /**
   * Get Movies By Director
   */
  static void getMoviesByDirector(String directorName) {
    try {
      if (directorName == null || directorName.isEmpty()) {
        System.out.println("Director name is empty");
        return;
      }

      String directorId = directors.stream()
              .filter(d -> directorName.equalsIgnoreCase(d.get("name")))
              .map(d -> d.get("directorId"))
              .findFirst()
              .orElse(null);

      if (directorId == null) {
        System.out.println("Director not found: " + directorName);
        return;
      }

      List<Map<String, String>> result = movies.stream()
              .filter(m -> directorId.equals(m.get("directorId")))
              .collect(Collectors.toList());

      if (result.isEmpty()) {
        System.out.println("No movies found for director: " + directorName);
      } else {
        print(result);
      }
    } catch (Exception e) {
      System.out.println("Error in getMoviesByDirector: " + e.getMessage());
    }
  }

  /**
   * Add New Movie
   */
  static void addNewMovie(String movieId, String title, String releaseYear,
                          String genre, String rating, String duration,
                          String directorId, String actorIds) {
    try {
      if (movieId == null || title == null) {
        System.out.println("Movie ID and title are required");
        return;
      }

      Map<String, String> movie = new HashMap<>();
      movie.put("id", movieId);
      movie.put("title", title);
      movie.put("releaseYear", releaseYear != null ? releaseYear : "Unknown");
      movie.put("genre", genre != null ? genre : "Unknown");
      movie.put("rating", rating != null ? rating : "0");
      movie.put("duration", duration != null ? duration : "0");
      movie.put("directorId", directorId != null ? directorId : "0");
      movie.put("actorIds", actorIds != null ? actorIds : "");

      movies.add(movie);
      System.out.println("New movie added: " + title);
    } catch (Exception e) {
      System.out.println("Error in addNewMovie: " + e.getMessage());
    }
  }

  /**
   * Get Directors With Most Movies
   */
  static void getDirectorsWithMostMovies() {
    try {
      Map<String, Long> movieCountByDirector = movies.stream()
              .collect(Collectors.groupingBy(
                      m -> m.getOrDefault("directorId", "0"),
                      Collectors.counting()
              ));

      long maxMovies = movieCountByDirector.values().stream()
              .max(Long::compare)
              .orElse(0L);

      directors.stream()
              .filter(d -> movieCountByDirector.getOrDefault(d.get("directorId"), 0L) == maxMovies)
              .forEach(d -> System.out.println(d.get("name") + " (" + maxMovies + " movies)"));
    } catch (Exception e) {
      System.out.println("Error in getDirectorsWithMostMovies: " + e.getMessage());
    }
  }

  /**
   * Get Actors Who Worked In Multiple Movies
   */
  static void getActorsWhoWorkedInMultipleMovies() {
    try {
      Map<String, Long> movieCountByActor = movies.stream()
              .flatMap(m -> {
                String ids = m.getOrDefault("actorIds", "");
                return Arrays.stream(ids.split(",")).map(String::trim);
              })
              .collect(Collectors.groupingBy(a -> a, Collectors.counting()));

      actors.stream()
              .filter(a -> movieCountByActor.getOrDefault(a.get("actorId"), 0L) > 1)
              .forEach(a -> System.out.println(a.get("name") + " (" +
                      movieCountByActor.get(a.get("actorId")) + " movies)"));
    } catch (Exception e) {
      System.out.println("Error in getActorsWhoWorkedInMultipleMovies: " + e.getMessage());
    }
  }

  /**
   * Get Movies of Youngest Actor
   */
  static void getMoviesOfYoungestActor() {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date referenceDate = sdf.parse("2025-02-10");

      Map<String, String> youngestActor = actors.stream()
              .min((a1, a2) -> {
                try {
                  Date dob1 = sdf.parse(a1.get("dateOfBirth"));
                  Date dob2 = sdf.parse(a2.get("dateOfBirth"));
                  return dob2.compareTo(dob1); // later DOB = younger
                } catch (ParseException e) {
                  return 0;
                }
              })
              .orElse(null);

      if (youngestActor == null) {
        System.out.println("No actors found.");
        return;
      }

      String actorId = youngestActor.get("actorId");
      String actorName = youngestActor.get("name");
      System.out.println("Youngest actor: " + actorName);

      List<Map<String, String>> result = movies.stream()
              .filter(m -> Arrays.asList(m.getOrDefault("actorIds", "").split(","))
                      .contains(actorId))
              .collect(Collectors.toList());

      if (result.isEmpty()) {
        System.out.println("No movies found for actor: " + actorName);
      } else {
        print(result);
      }

    } catch (ParseException e) {
      System.out.println("Date parsing error: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Error in getMoviesOfYoungestActor: " + e.getMessage());
    }
  }



  public static void main(String[] args) {
    try {
      movies = readCSV("/home/sumukh/Downloads/movies_large.csv");
      actors = readCSV("/home/sumukh/Downloads/actors_large.csv");
      directors = readCSV("/home/sumukh/Downloads/directors_large.csv");

    } catch (IOException e) {
      System.out.println("CSV file not found: " + e.getMessage());
    }
  }
}
