package botLogic.exceptions.countryCommandExceptions;

import botLogic.exceptions.CommandException;

public class UnknownCountryException extends CommandException {
    public UnknownCountryException(String message) {
        super(message);
    }
}
