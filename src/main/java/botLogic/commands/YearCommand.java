package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.userData.UsersData;
import kinopoiskAPI.Filter;

public class YearCommand {
    public static void setYear(String[] arguments, String userId) throws Exception {
        switch (arguments.length) {
            case 0 -> resetYears();
            case 1 -> setYear(arguments[0]);
            default -> setYears(arguments[0], arguments[1]);
        }
    }

    private static void resetYears() throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        filter.resetYears();
        UsersData.saveSearchResultOfCurrentUser(filter);
    }

    private static void setYears(String yearFrom, String yearTo) throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        filter.setYearFrom(tryParseYearToInt(yearFrom));
        filter.setYearTo(tryParseYearToInt(yearTo));
        checkCorrectnessOfYears(filter);
        UsersData.saveSearchResultOfCurrentUser(filter);
    }

    private static void setYear(String year) throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        switch (year.charAt(0)) {
            case '>' -> filter.setYearFrom(tryParseYearToInt(year.substring(1)));
            case '<' -> filter.setYearTo(tryParseYearToInt(year.substring(1)));
            default -> throw new CommandException("Год указан некорректно");
        }
        checkCorrectnessOfYears(filter);
        UsersData.saveSearchResultOfCurrentUser(filter);
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