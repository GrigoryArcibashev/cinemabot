package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.Repository;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import parser.Parser;

import java.util.HashSet;
import java.util.Map;

public class CountryCommand {
    private static final Map<String, Integer> countriesIdMap;

    static {
        countriesIdMap = API.getCountriesId();
    }

    public static void setCountry(String[] arguments, String userId) throws Exception {
        if (arguments.length > 0)
            setCountries(arguments, userId);
        else
            resetCountries(userId);
    }

    private static void setCountries(String[] countries, String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setCountriesId(getCountriesId(countries));
        Repository.updateSearchResult(filter, userId);
    }

    private static int[] getCountriesId(String[] countries) throws CommandException {
        HashSet<Integer> addingCountries = new HashSet<>();
        for (String country : countries) {
            try {
                addingCountries.add(countriesIdMap.get(country));
            } catch (NullPointerException exception) {
                throw new CommandException(String.format("Неизвестная страна: %s", country));
            }
        }
        return Parser.parseArrayToArrayOfInt(addingCountries.toArray(Integer[]::new));
    }

    private static void resetCountries(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setCountriesId(new int[0]);
        Repository.updateSearchResult(filter, userId);
    }
}
