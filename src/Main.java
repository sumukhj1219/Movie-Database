//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
   * @param movieId
   * @param genre
   */
  static void getMovieInformation(int movieId, String genre) {
    String movieIdStr = String.valueOf(movieId);
    new ArrayList();
    List<Map<String, String>> result = (List)movies.stream().filter((map) -> movieIdStr.equals(map.get("movieId"))).filter((map) -> genre == null || genre.isEmpty() || ((String)map.get("genre")).contains(genre)).collect(Collectors.toList());
    print(result);
  }

  /**
   * Get Top 10 Rated Movies
   */
  static void getTop10RatedMovies() {
    new ArrayList();
    List<Map<String, String>> result = (List)movies.stream().sorted((map1, map2) ->
                    Double.compare(
                            Double.parseDouble((String)map2.get("rating")),
                            Double.parseDouble((String)map1.get("rating"))
                    ))
            .limit(10L).collect(Collectors.toList());
    print(result);
  }

  /**
   * Get Movies By Genre
   * @param genre
   */
  static void getMoviesByGenre(String genre) {
    new ArrayList();
    List<Map<String, String>> result = (List)movies.stream().filter((map) -> genre == null || genre.isEmpty() || ((String)map.get("genre")).contains(genre)).collect(Collectors.toList());
    print(result);
  }

  /**
   * Get Movies By Director
   * @param directorName
   */
  static void getMoviesByDirector(String directorName) {
    String directorId = directors.stream()
            .filter(d -> d.get("name").equalsIgnoreCase(directorName))
            .map(d->d.get("directorId"))
            .findFirst()
            .orElse(null);

    List<Map<String, String>> result = movies.stream()
            .filter(m->m.get("directorId").equals(directorId))
            .collect(Collectors.toList());

    print(result);
  }

  /**
   * Get Movie By Release Year
   * @param year
   */
  static void getMovieByReleaseYear(String year) {
    List<Map<String, String>> result = movies.stream()
            .filter(m->m.get("releaseYear").equals(year))
            .collect(Collectors.toList());

    print(result);
  }

  /**
   * Get Movie By Release Year Range
   * @param s
   * @param y
   */
  static void getMovieByReleaseYearRange(String s, String y) {
    List<Map<String, String>> result = movies.stream()
            .filter(m->{
              int startYear = Integer.parseInt(s);
              int endYear = Integer.parseInt(y);
              int movieYear = Integer.parseInt(m.get("releaseYear"));
              return movieYear >= startYear && movieYear <= endYear;
            })
            .collect(Collectors.toList());
    print(result);
  }

  /**
   * Add New Movie
   * @param movieId
   * @param title
   * @param releaseYear
   * @param genre
   * @param rating
   * @param duration
   * @param directorId
   * @param actorIds
   */
  static void addNewMovie(String movieId, String title, String releaseYear,
                          String genre, String rating, String duration,
                          String directorId, String actorIds) {

    Map<String, String> movie = new HashMap<>();
    movie.put("id", movieId);
    movie.put("title", title);
    movie.put("releaseYear", releaseYear);
    movie.put("genre", genre);
    movie.put("rating", rating);
    movie.put("duration", duration);
    movie.put("directorId", directorId);
    movie.put("actorIds", actorIds);

    movies.add(movie);

    System.out.println("New movie added");
  }

  /**
   * Sorting Top 15 Movies By Release Year
   */
  static void sortMoviesByReleaseYear() {
    List<Map<String, String>> result = movies.stream()
            .sorted((map1, map2)->
                    Double.compare(
                            Double.parseDouble((String)map2.get("releaseYear")),
                            Double.parseDouble((String)map1.get("releaseYear"))
                    )
            )
            .limit(15)
            .collect(Collectors.toList());

    print(result);
  }

  /**
   * Get Directors With Most Movies
   */
  static void getDirectorsWithMostMovies() {
    Map<String, Long> movieCountByDirector =
            movies.stream()
                    .collect(Collectors.groupingBy(
                            m -> m.get("directorId"),
                            Collectors.counting()
                    ));

    long maxMovies = movieCountByDirector.values()
            .stream()
            .max(Long::compare)
            .orElse(0L);

    directors.stream()
            .filter(d ->
                    movieCountByDirector.getOrDefault(d.get("directorId"), 0L) == maxMovies
            )
            .forEach(d ->
                    System.out.println(
                            d.get("name") + " (" + maxMovies + " movies)"
                    )
            );
  }

  /**
   * Get Actors Who Worked In Multiple Movies
   */
  static void getActorsWhoWorkedInMultipleMovies() {
    Map<String, Long> movieCountByActor =
            movies.stream()
                    .flatMap(m -> Arrays.stream(m.get("actorIds").split(",")))
                    .map(String::trim)
                    .collect(Collectors.groupingBy(
                            actorId -> actorId,
                            Collectors.counting()
                    ));

    actors.stream()
            .filter(a ->
                    movieCountByActor.getOrDefault(a.get("actorId"), 0L) > 1
            )
            .forEach(a ->
                    System.out.println(
                            a.get("name") + " (" +
                                    movieCountByActor.get(a.get("actorId")) + " movies)"
                    )
            );
  }



  public static void main(String[] args) {
    try {
      movies = readCSV("/home/sumukh/Downloads/movies_large.csv");
      actors = readCSV("/home/sumukh/Downloads/actors_large.csv");
      directors = readCSV("/home/sumukh/Downloads/directors_large.csv");

      getActorsWhoWorkedInMultipleMovies();
    } catch (IOException e) {
      System.out.println("CSV file not found: " + e.getMessage());
    }
  }
}
