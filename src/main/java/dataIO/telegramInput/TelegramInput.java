package dataIO.telegramInput;

import dataIO.inputModule.InputModule;
import dataIO.inputModule.Lexeme;

public class TelegramInput implements InputModule {
    public String message;
    @Override
    public Lexeme getNextLexeme() {
        return new Lexeme(message, "Bob");
    }
}
