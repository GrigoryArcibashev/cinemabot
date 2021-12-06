package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.Repository;
import kinopoiskAPI.Filter;

public class TypeCommand {
    public static void setType(String[] arguments, String userId) throws Exception {
        String typeOfMovie = arguments.length == 0 ? "all" : arguments[0];
        setType(typeOfMovie, userId);
    }

    private static void setType(String typeOfMovie, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        switch (typeOfMovie) {
            case "film" -> filter.setType("FILM");
            case "serial" -> filter.setType("TV_SHOW");
            case "all" -> filter.setType("ALL");
            default -> throw new CommandException("Указан некорректный тип фильма");
        }
        Repository.updateSearchResult(filter, userId);
    }
}