package bots.consoleBot;

import botLogic.CommandHandler;
import dataIO.consoleInput.ConsoleInput;
import dataIO.consoleOutput.ConsoleOutput;
import dataIO.outputModule.OutputModule;
import tokenizer.Token;
import tokenizer.Tokenizer;

public class ConsoleBot {
    private final OutputModule outputModule;
    private final Tokenizer tokenizer;
    private final CommandHandler commandHandler;

    public ConsoleBot() {
        this.outputModule = new ConsoleOutput();
        this.tokenizer = new Tokenizer(new ConsoleInput());
        this.commandHandler = new CommandHandler();
    }

    public void start() {
        while (true) {
            Token token = null;
            while (token == null)
                token = tokenizer.getNextToken();
            outputModule.sendMessage(commandHandler.handleToken(token), token.userId());
        }
    }
}
