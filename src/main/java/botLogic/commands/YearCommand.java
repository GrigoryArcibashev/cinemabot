package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.Repository;
import kinopoiskAPI.Filter;

public class YearCommand {
    public static void setYear(String[] arguments, String userId) throws Exception {
        switch (arguments.length) {
            case 0 -> resetYears(userId);
            case 1 -> setYear(arguments[0], userId);
            default -> setYears(arguments[0], arguments[1], userId);
        }
    }

    private static void resetYears(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.resetYears();
        Repository.updateSearchResult(filter, userId);
    }

    private static void setYears(String yearFrom, String yearTo, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setYearFrom(tryParseYearToInt(yearFrom));
        filter.setYearTo(tryParseYearToInt(yearTo));
        checkCorrectnessOfYears(filter);
        Repository.updateSearchResult(filter, userId);
    }

    private static void setYear(String year, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        switch (year.charAt(0)) {
            case '>' -> filter.setYearFrom(tryParseYearToInt(year.substring(1)));
            case '<' -> filter.setYearTo(tryParseYearToInt(year.substring(1)));
            default -> throw new CommandException("Год указан некорректно");
        }
        checkCorrectnessOfYears(filter);
        Repository.updateSearchResult(filter, userId);
    }

    private static void checkCorrectnessOfYears(Filter filter) throws CommandException {
        if (filter.getYearFrom() > filter.getYearTo())
            throw new CommandException("Указанный минимальный год больше указанного максимального");
    }

    private static int tryParseYearToInt(String year) throws CommandException {
        try {
            int result = Integer.parseInt(year);
            if (result < 0)
                throw new CommandException("Год не может быть отрицательным");
            return result;
        } catch (NumberFormatException exception) {
            throw new CommandException("Год должен быть указан числом");
        }
    }
}