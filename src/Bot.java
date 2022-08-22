import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import controllers.CatsController;
import controllers.CurrenciesController;
import controllers.MovieController;
import pojo.Movie;

import java.util.List;

public class Bot {

    private static final TelegramBot BOT = new TelegramBot("PUT_TOKEN_HERE");
    private static long chatId;
    private static String incomeMessage;
    private static String textMessage;
    private static SendPhoto photo;

    public static void start() {
        BOT.setUpdatesListener(updates -> {
            updates.forEach(upd -> {
                System.out.println(upd);
                chatId = upd.message().chat().id();
                incomeMessage = upd.message().text().toLowerCase();

                if (incomeMessage.equals("/start") || incomeMessage.equals("/help")) {
                    textMessage = help();
                    sendMessage();

                } else if (incomeMessage.equals("/currencies") || incomeMessage.equals("currencies")) {
                    textMessage = CurrenciesController.getCurrenciesList();
                    sendMessage();

                } else if (incomeMessage.startsWith("rate")) {
                    try {
                        String currency = incomeMessage.split(" ")[1];
                        textMessage = CurrenciesController.getCurrencyRate(currency);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        textMessage = "To get the valid exchange rate, enter the message in chat by format:\n" +
                                "'rate <currency code>'";
                    }
                    sendMessage();

                } else if (incomeMessage.startsWith("movie")) {
                    try {
                        String genre = incomeMessage.split(" ")[1];
                        int rating = Integer.parseInt(incomeMessage.split(" ")[2]);
                        List<Movie> movies = MovieController.getMovies(genre, rating);
                        if (movies.isEmpty()) {
                            textMessage = "Cannot find anything. Maybe.. need to check your request?";
                            sendMessage();
                        } else {
                            movies.stream()
                                    .limit(10)
                                    .forEach(movie -> {
                                        SendPhoto photo = new SendPhoto(chatId, movie.getImage());
                                        BOT.execute(photo);
                                        SendMessage response = new SendMessage(chatId, movie.toString());
                                        BOT.execute(response);
                                    });
                        }
                    } catch (Exception e) {
                        textMessage = "Please enter the message by specified format";
                        sendMessage();
                    }

                } else if (incomeMessage.equals("/genres") || incomeMessage.equals("genres")) {
                    textMessage = MovieController.getGenres();
                    sendMessage();

                } else if (incomeMessage.equals("/cat") || incomeMessage.equals("cat")) {
                    String cat = CatsController.showCats();
                    photo = new SendPhoto(chatId, cat);
                    BOT.execute(photo);

                } else {
                    textMessage = help();
                    sendMessage();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void sendMessage() {
        SendMessage response = new SendMessage(chatId, textMessage);
        BOT.execute(response);
    }

    private static String help() {
        return """
                Available commands:
                                
                Currency exchange rate:
                - /currencies (or 'currencies' in chat) - to get the list of available currencies and its codes
                - to get the valid exchange rate, enter the message in chat by format:
                'rate <currency code>'
                Example: 'rate usd'
                                
                Movies:
                - /genres (or 'genres' in chat) - to get the list of movie genres
                - to get the list of movies, enter the message in chat by format:
                'movie <genre> <rating>'
                Example: 'movie thriller 8'
                                
                Random:
                /cat (or 'cat' in chat) - to get the random cat photo
                """;
    }
}
