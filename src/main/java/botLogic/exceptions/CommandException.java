package botLogic.exceptions;

public class CommandException extends Exception {
    public CommandException() {
        super("");
    }

    public CommandException(String errorMessage) {
        super(errorMessage);
    }
}