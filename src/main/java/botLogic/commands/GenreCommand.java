package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.Repository;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import parser.Parser;

import java.util.HashSet;
import java.util.Map;

public class GenreCommand {
    private static final Map<String, Integer> genresIdMap;

    static {
        genresIdMap = API.getGenresId();
    }

    public static void setGenre(String[] arguments, String userId) throws Exception {
        if (arguments.length > 0)
            setGenres(arguments, userId);
        else
            resetGenres(userId);
    }

    private static void setGenres(String[] genres, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setGenresId(getGenresId(genres));
        Repository.updateSearchResult(filter, userId);
    }

    private static int[] getGenresId(String[] genres) throws CommandException {
        HashSet<Integer> addingGenres = new HashSet<>();
        for (String genre : genres) {
            try {
                addingGenres.add(genresIdMap.get(genre));
            } catch (NullPointerException exception) {
                throw new CommandException(String.format("Неизвестный жанр: %s", genre));
            }
        }
        return Parser.parseArrayToArrayOfInt(addingGenres.toArray(Integer[]::new));
    }

    private static void resetGenres(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setGenresId(new int[0]);
        Repository.updateSearchResult(filter, userId);
    }
}