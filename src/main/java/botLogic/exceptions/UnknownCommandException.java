package botLogic.exceptions;

public class UnknownCommandException extends CommandException {
    public UnknownCommandException(String commandName) {
        super(String.format("Неизвестная команда: %s", commandName));
    }
}
