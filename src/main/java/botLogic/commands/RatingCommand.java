package botLogic.commands;

import botLogic.Repository;
import botLogic.exceptions.ratingCommandExceptions.IncorrectRatingException;
import kinopoiskAPI.Filter;

public class RatingCommand {
    public static void setRating(String[] arguments, String userId) throws Exception {
        switch (arguments.length) {
            case 0 -> resetRatings(userId);
            case 1 -> setRating(arguments[0], userId);
            default -> setRatings(arguments[0], arguments[1], userId);
        }
    }

    private static void resetRatings(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.resetRatings();
        Repository.updateSearchResult(filter, userId);
    }

    private static void setRatings(String ratingFrom, String ratingTo, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setRatingFrom(tryParseRatingToInt(ratingFrom));
        filter.setRatingTo(tryParseRatingToInt(ratingTo));
        checkCorrectnessOfRatings(filter);
        Repository.updateSearchResult(filter, userId);
    }

    private static void setRating(String rating, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        switch (rating.charAt(0)) {
            case '>' -> filter.setRatingFrom(tryParseRatingToInt(rating.substring(1)));
            case '<' -> filter.setRatingTo(tryParseRatingToInt(rating.substring(1)));
            default -> throw new IncorrectRatingException("Рейтинг указан некорректно");
        }
        checkCorrectnessOfRatings(filter);
        Repository.updateSearchResult(filter, userId);
    }

    private static void checkCorrectnessOfRatings(Filter filter) throws IncorrectRatingException {
        if (filter.getRatingFrom() > filter.getRatingTo())
            throw new IncorrectRatingException("Указанный минимальный рейтинг больше указанного максимального");
    }

    private static int tryParseRatingToInt(String rating) throws IncorrectRatingException {
        try {
            int result = Integer.parseInt(rating);
            if (result < 0 || result > 10)
                throw new IncorrectRatingException("Рейтинг должен находиться в пределах 0-10");
            return result;
        } catch (NumberFormatException exception) {
            throw new IncorrectRatingException("Рейтинг должен быть указан числом");
        }
    }
}
