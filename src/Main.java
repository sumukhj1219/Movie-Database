//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                            Double.parseDouble((String)map1.get("rating"))))
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
