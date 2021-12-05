package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.exceptions.UnknownCommandException;

import java.util.List;
import java.util.function.Predicate;

public class HelpCommand {
    public static String getHelpForCommand(List<Command> commands, String commandName) throws UnknownCommandException {
        String descriptions;
        try {
            descriptions = getHelpForCommandsSatisfyingCondition(
                    commands,
                    command -> command.name().equals(commandName));
            return descriptions;
        } catch (CommandException exception) {
            throw new UnknownCommandException(commandName);
        }
    }

    public static String getHelpForAllCommands(List<Command> commands) throws CommandException {
        return getHelpForCommandsSatisfyingCondition(commands, command -> true);
    }

    private static String getHelpForCommandsSatisfyingCondition(List<Command> commands, Predicate<Command> condition)
            throws CommandException {
        StringBuilder descriptions = new StringBuilder();
        for (Command command : commands) {
            if (!condition.test(command))
                continue;
            descriptions
                    .append(command.name()).append(" ")
                    .append(command.arguments()).append("\n")
                    .append(command.description()).append("\n\n");
        }
        if (descriptions.isEmpty())
            throw new CommandException();
        return descriptions.substring(0, descriptions.length() - 2);
    }
}
