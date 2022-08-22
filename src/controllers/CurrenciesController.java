package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import pojo.Currency;

import java.io.IOException;

public class CurrenciesController {

    private static final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ;

    public static String getCurrencyRate(String currency) {
        try {
            JsonNode currenciesListJson = getCurrencyJsonNode();
            JsonNode currencyRateJson = currenciesListJson.get("Valute").get(currency.toUpperCase());
            Currency currencyRate = MAPPER.convertValue(currencyRateJson, Currency.class);
            return currencyRate.toString();
        } catch (IOException e) {
            return "Cannot find such currency :(";
        }
    }

    public static String getCurrenciesList() {
        try {
            JsonNode currenciesListJson = getCurrencyJsonNode();
            StringBuilder currenciesList = new StringBuilder();
            currenciesListJson.get("Valute").elements().forEachRemaining(
                    n -> currenciesList.append(n.get("CharCode"))
                            .append(" - ")
                            .append(n.get("Name"))
                            .append("\n"));
            return currenciesList.toString().replaceAll("\"", "");
        } catch (IOException e) {
            return "Something goes wrong :(";
        }
    }

    public static JsonNode getCurrencyJsonNode() throws IOException {
        String jsonString = Jsoup.connect(URL)
                .ignoreContentType(true)
                .execute()
                .body();
        return MAPPER.readTree(jsonString);
    }
}
