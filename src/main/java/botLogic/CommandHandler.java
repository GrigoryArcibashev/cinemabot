package botLogic;

import botLogic.commands.*;
import botLogic.commands.adviseCommand.AdviseCommand;
import botLogic.exceptions.CommandException;
import botLogic.exceptions.IllegalCommandArgumentsException;
import botLogic.exceptions.UnknownCommandException;
import dataIO.outputModule.Message;
import tokenizer.Token;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandHandler {
    private final Map<String, Command> commands;

    public CommandHandler() {
        commands = registerCommands();
    }

    public Message handleCommand(Token token) {
        try {
            checkCommandCall(token.command(), token.arguments());
            return executeCommand(token);
        } catch (CommandException exception) {
            return notifyAboutUnsuccessfulResult(exception.getMessage(), token.userId());
        } catch (Exception exception) {
            exception.printStackTrace();
            return notifyAboutUnsuccessfulResult(token.userId());
        }
    }

    private Message executeCommand(Token token) throws Exception {
        String[] arguments = token.arguments();
        String userId = token.userId();
        return switch (token.command()) {
            case "/help" -> help(arguments, userId);
            case "/advise" -> advise(userId);
            case "/type" -> type(arguments, userId);
            case "/year" -> year(arguments, userId);
            case "/rating" -> rating(arguments, userId);
            case "/genre" -> genre(arguments, userId);
            case "/country" -> country(arguments, userId);
            default -> throw new IllegalStateException("Unexpected value of token.command: " + token.command());
        };
    }

    @Command(
            name = "/help",
            arguments = "[command]",
            maxArgs = 1,
            description = """
                    Выводит краткую информацию о боте, список команд и их описание
                    Если указан аргумент [command], выводит справку по указанной команде""")
    private Message help(String[] arguments, String userId) throws CommandException {
        String result;
        if (arguments.length > 0)
            result = HelpCommand.getHelpForCommand(getAllCommands(), arguments[0]);
        else
            result = HelpCommand.getHelpForAllCommands(getAllCommands());
        return new Message(result, userId);
    }

    @Command(
            name = "/advise",
            arguments = "",
            maxArgs = 0,
            description = """
                    Выдает случайный фильм, удовлетворяющий фильтрам поиска""")
    private Message advise(String userId) throws Exception {
        return new Message(AdviseCommand.advise(userId), userId);
    }

    @Command(
            name = "/type",
            arguments = "serial | film",
            maxArgs = 1,
            description = """ 
                    /type serial : искать только сериалы
                    /type film : искать только фильмы
                    Вызов команды без указания типа контента сбрасывает фильтр по типу""")
    private Message type(String[] arguments, String userId) throws Exception {
        TypeCommand.setType(arguments, userId);
        return notifyAboutSuccessfulResult(userId);
    }

    @Command(
            name = "/year",
            arguments = "y | y y | >y | <y",
            maxArgs = 2,
            description = """
                    Сортировка по году(ам) выхода фильма
                        /year y : поиск по указанному году
                        /year y y : поиск по указанному временному промежутку
                        /year >y : поиск фильмов, выпущенных позже указанного года
                        /year <y : поиск фильмов, выпущенных ранее указанного года
                    Вызов команды без указания года(ов) сбрасывает фильтр по году""")
    private Message year(String[] arguments, String userId) throws Exception {
        YearCommand.setYear(arguments, userId);
        return notifyAboutSuccessfulResult(userId);
    }

    @Command(
            name = "/rating",
            arguments = "r | r r | >r | <r",
            maxArgs = 2,
            description = """
                    Сортировка по рейтингу фильмов.
                        /rating r : поиск фильмов с указанным рейтингом
                        /rating r r : поиск фильмов с рейтингом в указанном диапазоне
                        /rating >r : поиск фильмов с рейтингом выше указанного
                        /rating <r : поиск фильмов с рейтингом ниже указанного
                    Вызов команды без указания рейтинга сбрасывает фильтр по рейтингу""")
    private Message rating(String[] arguments, String userId) throws Exception {
        RatingCommand.setRating(arguments, userId);
        return notifyAboutSuccessfulResult(userId);
    }

    @Command(
            name = "/genre",
            arguments = "'название жанра/жанров'",
            description = """
                    Сортировка по жанру/жанрам фильма (жанры указываются через пробел)
                    Вызов команды без указания жанра сбрасывает фильтр по жанрам"""
    )
    private Message genre(String[] arguments, String userId) throws Exception {
        GenreCommand.setGenre(arguments, userId);
        return notifyAboutSuccessfulResult(userId);
    }

    @Command(
            name = "/country",
            arguments = "'название страны/стран'",
            description = """
                    Сортировка по странам. Список стран указывается через пробел
                    Вызов команды без указания страны(ан) сбрасывает фильтр по странам""")
    private Message country(String[] arguments, String userId) throws Exception {
        CountryCommand.setCountry(arguments, userId);
        return notifyAboutSuccessfulResult(userId);
    }

    public List<Command> getAllCommands() {
        return Arrays
                .stream(this.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Command.class))
                .map(m -> m.getAnnotation(Command.class))
                .collect(Collectors.toList());
    }

    private Message notifyAboutSuccessfulResult(String userId) {
        return new Message("Команда успешно выполнена", userId);
    }

    private Message notifyAboutUnsuccessfulResult(String userId) {
        return new Message("Ошибка в процессе выполнения команды.\nДля справки используйте /help", userId);
    }

    private Message notifyAboutUnsuccessfulResult(String errorMessage, String userId) {
        return new Message(
                String.format("Ошибка в процессе выполнения команды.\n%s\nДля справки используйте /help", errorMessage),
                userId);
    }

    private void checkCommandCall(String commandName, String[] arguments)
            throws UnknownCommandException, IllegalCommandArgumentsException {
        Command cmd = commands.get(commandName);
        if (cmd == null)
            throw new UnknownCommandException(commandName);
        if (arguments.length >= cmd.minArgs() && arguments.length <= cmd.maxArgs())
            return;
        throw new IllegalCommandArgumentsException(
                String.format(
                        "Команда %s принимает %d - %d аргументов",
                        cmd.name(),
                        cmd.minArgs(),
                        cmd.maxArgs()));
    }

    private Map<String, Command> registerCommands() {
        Map<String, Command> commands = new HashMap<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command cmd = method.getAnnotation(Command.class);
                commands.put(cmd.name(), cmd);
            }
        }
        return commands;
    }
}
