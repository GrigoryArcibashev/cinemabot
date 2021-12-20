package bots.consoleBot;

import botLogic.CommandHandler;
import dataIO.telegramInput.TelegramInput;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.Collections;

public class TelegramBot extends TelegramLongPollingBot {
    private final Tokenizer tokenizer;
    private final CommandHandler commandHandler;
    private final String botUsername;
    private final String botToken;
    private final TelegramInput telegramInput;
    private final ReplyKeyboardMarkup replykeyboardMarkup;
    private Update message;

    public TelegramBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        telegramInput = new TelegramInput();
        tokenizer = new Tokenizer(telegramInput);
        commandHandler = new CommandHandler();
        replykeyboardMarkup = new ReplyKeyboardMarkup();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update;
        }

        var userMessage = outputMessage();

        if (userMessage != null) {
            telegramInput.message = userMessage.getMessage().getText();
            var token = tokenizer.getNextToken();
            var message = commandHandler.handleCommand(token);
            initializeKeyboard();
            inputMessage(initializeSendMessage(userMessage.getMessage().getChatId().toString(), message.message()));
        }
    }

    @Override
    public String getBotUsername() { return botUsername; }

    @Override
    public String getBotToken() { return botToken; }

    public void inputMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Update outputMessage() { return message; }

    private void initializeKeyboard()
    {
        var keyboard = new ArrayList<KeyboardRow>();
        var firstKeyboardRow = new KeyboardRow();
        var secondKeyboardRow = new KeyboardRow();
        var thirdKeyboardRow = new KeyboardRow();

        addKeyboardButtonsInARow(
                firstKeyboardRow,
                new KeyboardButton("/advise"),
                new KeyboardButton("/rating"),
                new KeyboardButton("/year"));

        addKeyboardButtonsInARow(
                secondKeyboardRow,
                new KeyboardButton("/year"),
                new KeyboardButton("/type"),
                new KeyboardButton("/country"));

        thirdKeyboardRow.add(new KeyboardButton("/help"));
        addRowsInKeyboard(keyboard, firstKeyboardRow, secondKeyboardRow, thirdKeyboardRow);
        initializeReplyKeyboardMarkup(keyboard);
    }

    private void initializeReplyKeyboardMarkup(ArrayList<KeyboardRow> keyboard)
    {
        replykeyboardMarkup.setSelective(true);
        replykeyboardMarkup.setResizeKeyboard(true);
        replykeyboardMarkup.setOneTimeKeyboard(false);
        replykeyboardMarkup.setKeyboard(keyboard);
    }

    private void addKeyboardButtonsInARow(KeyboardRow keyboardRow, KeyboardButton ... buttons)
    {
        Collections.addAll(keyboardRow, buttons);
    }

    private void addRowsInKeyboard(ArrayList<KeyboardRow> keyboard, KeyboardRow ... rows) {
        Collections.addAll(keyboard, rows);
    }

    private SendMessage initializeSendMessage(String chatId, String message)
    {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replykeyboardMarkup);
        return sendMessage;
    }
}
