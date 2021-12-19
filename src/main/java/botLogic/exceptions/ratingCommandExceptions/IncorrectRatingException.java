package botLogic.exceptions.ratingCommandExceptions;

import botLogic.exceptions.CommandException;

public class IncorrectRatingException extends CommandException {
    public IncorrectRatingException(String message) {
        super(message);
    }
}
