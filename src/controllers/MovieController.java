package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import pojo.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieController {

    public static List<Movie> getMovies(String genre, int rating) {
        if (!getGenres().contains(genre)) {
            return new ArrayList<>();
        }
        List<Movie> filteredMovies = new ArrayList<>();
        try {
            String jsonString =
                    Jsoup.connect("https://imdb-api.com/API/AdvancedSearch/k_6k2d961u/?genres=" + genre)
                            .ignoreContentType(true)
                            .execute()
                            .body();
            ObjectMapper objectMapper = new ObjectMapper();

            // конфиг, который позволяет не парсить ненужные поля (если их нет у нашего объекта - они пропускаются)
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JsonNode jsonNode = objectMapper.readTree(jsonString);
            List<Movie> movies = new ArrayList<>();
            jsonNode.get("results").elements()
                    .forEachRemaining(jsNode -> {
                        Movie movie = objectMapper.convertValue(jsNode, Movie.class); // преобразование JsonNode в  pojo.Movie
                        movies.add(movie);
                    });
            filteredMovies = movies.stream()
                    .filter(movie -> {
                        if (movie.getImDbRating() == null) {
                            return false;
                        } else {
                            return Double.parseDouble(movie.getImDbRating()) >= rating;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Smth goes wrong :(");
        }
        return filteredMovies;
    }

    public static String getGenres() {
        return "action adventure animation biography comedy crime documentary drama " +
                "family fantasy film_noir game_show history horror music musical mystery " +
                "news reality_tv romance sci_fi sport talk_show thriller war western";
    }
}
