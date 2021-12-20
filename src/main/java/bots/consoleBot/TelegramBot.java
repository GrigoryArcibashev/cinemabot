package bots.consoleBot;

import botLogic.CommandHandler;
import dataIO.telegramInput.TelegramInput;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tokenizer.Tokenizer;

public class TelegramBot extends TelegramLongPollingBot {
    private final Tokenizer tokenizer;
    private final CommandHandler commandHandler;
    private final String botUsername;
    private final String botToken;
    private final TelegramInput telegramInput;
    private Update message;

    public TelegramBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        telegramInput = new TelegramInput();
        this.tokenizer = new Tokenizer(telegramInput);
        this.commandHandler = new CommandHandler();
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
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(userMessage.getMessage().getChatId().toString());
            sendMessage.setText(message.message());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public Update outputMessage() {
        return message;
    }
}
