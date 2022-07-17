import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Load Bot");

        SQLite.connection= SQLite.connect();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Devotional());
            System.out.println("Hello, World!");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}