package botLogic.exceptions;

public class IllegalCommandArgumentsException extends CommandException {
    public IllegalCommandArgumentsException(String errorMessage) {
        super(errorMessage);
    }
}
