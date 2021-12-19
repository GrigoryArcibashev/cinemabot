package botLogic.commands;

import botLogic.Repository;
import botLogic.exceptions.genreCommandExceptions.UnknownGenreException;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import parser.Parser;

import java.util.HashSet;
import java.util.Map;

public class GenreCommand {
    private static Map<String, Integer> genresIdMap;

    public static void setGenre(String[] arguments, String userId) throws Exception {
        if (arguments.length > 0)
            setGenres(arguments, userId);
        else
            resetGenres(userId);
    }

    private static void setGenres(String[] genres, String userId) throws Exception {
        if (genresIdMap == null)
            genresIdMap = API.getGenresId();
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setGenresId(getGenresId(genres));
        Repository.updateSearchResult(filter, userId);
    }

    private static void resetGenres(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setGenresId(new int[0]);
        Repository.updateSearchResult(filter, userId);
    }

    private static int[] getGenresId(String[] genres) throws UnknownGenreException {
        HashSet<Integer> addingGenres = new HashSet<>();
        for (String genre : genres) {
            Integer genreId = genresIdMap.get(genre);
            if (genreId == null)
                throw new UnknownGenreException(String.format("Неизвестный жанр: %s", genre));
            else
                addingGenres.add(genreId);
        }
        return Parser.parseArrayToArrayOfInt(addingGenres.toArray(Integer[]::new));
    }
}