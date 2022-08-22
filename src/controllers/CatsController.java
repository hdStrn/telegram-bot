package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;

public class CatsController {

    public static String showCats() {
        String url = "";
        try {
            String jsonString = Jsoup.connect("https://api.thecatapi.com/v1/images/search")
                    .ignoreContentType(true)
                    .execute()
                    .body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            url = jsonNode.get(0).get("url").asText();
        } catch (Exception e) {

        }
        return url;
    }

}
