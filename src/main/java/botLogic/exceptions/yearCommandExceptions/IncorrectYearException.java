package botLogic.exceptions.yearCommandExceptions;

import botLogic.exceptions.CommandException;

public class IncorrectYearException extends CommandException {
    public IncorrectYearException(String message) {
        super(message);
    }
}
