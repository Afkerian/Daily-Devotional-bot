import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.Instant;

import static java.lang.Thread.sleep;


public class Devotional extends TelegramLongPollingBot {

    public String getBotUsername() {
        return TokenReader.readUserName();
    }

    public String getBotToken() {
        return TokenReader.readToken();
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().isCommand()) {
            switch (update.getMessage().getText()) {
                case "/start": {
                    String idUser = update.getMessage().getChatId().toString();
                    String userName = update.getMessage().getFrom().getUserName();
                    String state = "enable";
                    String languages = update.getMessage().getFrom().getLanguageCode();
                    if(SQLite.getCheck(SQLite.connection, idUser)){

                    }else {
                        SQLite.saveRegister(SQLite.connection,idUser,userName,state,languages);
                    }
                    break;
                }
                case "/language": {
                    break;
                }
                case "/information": {
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}

