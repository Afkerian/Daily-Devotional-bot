import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
            String idUser = update.getMessage().getChatId().toString();
            switch (update.getMessage().getText()) {
                case "/start": {
                    String userName = update.getMessage().getFrom().getUserName();
                    String state = "enable";
                    String languages = update.getMessage().getFrom().getLanguageCode();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    if (SQLite.getCheck(SQLite.connection, idUser)) {
                        if(languages.equals("es")){
                            SQLite.updateState(SQLite.connection,idUser);
                            sendMessage.setText("Bienvenido, tu idioma por defecto se configuro en espanol, si deseas cambiar tu preferencia de idioma /language , " +
                                    "para obtener un versiculo usa /verse");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            SQLite.updateState(SQLite.connection,idUser);
                            sendMessage.setText("Welcome, your default language is set to Spanish, if you want to change your language preference /language , " +
                                    "to get a verse use /verse");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } else {

                        if(languages.equals("es")){
                            SQLite.saveRegister(SQLite.connection, idUser, userName, state, "bibleES");
                            sendMessage.setText("Bienvenido, tu idioma por defecto se configuro en espanol, si deseas cambiar tu preferencia de idioma /language , " +
                                    "para obtener un versiculo usa /verse");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            SQLite.saveRegister(SQLite.connection, idUser, userName, state, "bibleEN");
                            sendMessage.setText("Welcome, your default language is set to Spanish, if you want to change your language preference /language , " +
                                    "to get a verse use /verse");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                }
                case "/language": {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    // Create ReplyKeyboardMarkup object
                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                    // Create the keyboard (list of keyboard rows)
                    List<KeyboardRow> keyboard = new ArrayList<>();
                    // Create a keyboard row
                    KeyboardRow row = new KeyboardRow();
                    // Set each button, you can also use KeyboardButton objects if you need something else than text
                    row.add("/ENGLISH");
                    row.add("/ESPANOL");
                    // Add the first row to the keyboard
                    keyboard.add(row);
                    // Set the keyboard to the markup
                    keyboardMarkup.setKeyboard(keyboard);
                    keyboardMarkup.setResizeKeyboard(true);
                    keyboardMarkup.setOneTimeKeyboard(true);
                    sendMessage.setReplyMarkup(keyboardMarkup);

                    if(update.getMessage().getFrom().getLanguageCode().equals("ES")){
                        sendMessage.setText("Selecciona el idioma en el que te gustaria recibir los versiculos a continuacion, " +
                                "pronto agregaremos nuevos idiomas. ");
                    }else {
                        sendMessage.setText("Select the language in which you would like to receive the verses below, " +
                                "we will add new languages soon.");
                    }

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                }
                case "/information": {

                    break;
                }
                case "/ENGLISH": {
                    SQLite.updateLanguages(SQLite.connection, "bibleEN", idUser);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("The language was successfully updated to English");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/ESPANOL": {
                    SQLite.updateLanguages(SQLite.connection, "bibleES", idUser);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("El idioma se actualizó correctamente a Español");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case "/verse":{
                    String language = "";
                    System.out.println("Ingresamos");
                    try {
                        language = SQLite.getLanguage(SQLite.connection,idUser).getString(1);
                        System.out.println("Realizamos la consulta del lenguaje");
                    } catch (SQLException e) {
                        System.out.println("No se obtiene el lenguaje");
                        throw new RuntimeException(e);
                    }
                    String salida = "HERE";
                    System.out.println("Obtenemos el Verso");
                    try {
                        ResultSet resultSet = SQLite.getVerse(SQLite.connection,language);
                        if(language.equals("bibleEN")){
                            salida = resultSet.getString(4);
                            ResultSet resultSet1 = SQLite.getBook(SQLite.connection,"enBooks",resultSet.getString(1));
                            salida+="\n\n**"+resultSet1.getString(1)+"** "+resultSet.getString(2)+":"+resultSet.getString(3);
                        }else {
                            salida = resultSet.getString(4);
                            ResultSet resultSet1 = SQLite.getBook(SQLite.connection,"esBooks",resultSet.getString(1));
                            salida+="\n\n"+resultSet1.getString(1)+"\t"+resultSet.getString(2)+":"+resultSet.getString(3);;
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText(salida);
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                default: {

                    break;
                }
            }
        }
    }

    private void cronJob () throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        // Clase en la que está el código a ejecutar
        TimerTask timerTask = new TimerTask()
        {
            public void run()
            {
                // Aquí el código que queremos ejecutar.
                try {

                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        // Aquí se pone en marcha el timer cada segundo.
        Timer timer = new Timer();
        // Dentro de 0 milisegundos avísame cada 1000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 500, 1000);
    }
}

