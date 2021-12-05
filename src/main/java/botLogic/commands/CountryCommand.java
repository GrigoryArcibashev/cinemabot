package botLogic.commands;

import botLogic.exceptions.CommandException;
import botLogic.userData.UsersData;
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
            setCountries(arguments);
        else
            resetCountries();
    }

    private static void setCountries(String[] countries) throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        filter.setCountriesId(getCountriesId(countries));
        UsersData.saveSearchResultOfCurrentUser(filter);
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

    private static void resetCountries() throws Exception {
        Filter filter = UsersData.getParametersOfCurrentUser().getFilter();
        filter.setCountriesId(new int[0]);
        UsersData.saveSearchResultOfCurrentUser(filter);
    }
}
