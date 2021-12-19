package botLogic.exceptions.genreCommandExceptions;

import botLogic.exceptions.CommandException;

public class UnknownGenreException extends CommandException {
    public UnknownGenreException(String message) {
        super(message);
    }
}
