package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.userData.UsersData;
import kinopoiskAPI.Filter;

public class TypeCommand {
    public static void setType(String[] arguments, String userId) throws Exception {
        String typeOfMovie = arguments.length == 0 ? "all" : arguments[0];
        setType(typeOfMovie);
    }

    private static void setType(String typeOfMovie) throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        switch (typeOfMovie) {
            case "film" -> filter.setType("FILM");
            case "serial" -> filter.setType("TV_SHOW");
            case "all" -> filter.setType("ALL");
            default -> throw new CommandException("Указан некорректный тип фильма");
        }
        UsersData.saveSearchResultOfCurrentUser(filter);
    }
}