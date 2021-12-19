package botLogic.commands;

import botLogic.Repository;
import botLogic.exceptions.countryCommandExceptions.UnknownCountryException;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import parser.Parser;

import java.util.HashSet;
import java.util.Map;

public class CountryCommand {
    private static Map<String, Integer> countriesIdMap;

    public static void setCountry(String[] arguments, String userId) throws Exception {
        if (arguments.length > 0)
            setCountries(arguments, userId);
        else
            resetCountries(userId);
    }

    private static void setCountries(String[] countries, String userId) throws Exception {
        if (countriesIdMap == null)
            countriesIdMap = API.getCountriesId();
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setCountriesId(getCountriesId(countries));
        Repository.updateSearchResult(filter, userId);
    }

    private static void resetCountries(String userId) throws Exception {
        Filter filter = Repository.getUserData(userId).getFilter();
        filter.setCountriesId(new int[0]);
        Repository.updateSearchResult(filter, userId);
    }

    private static int[] getCountriesId(String[] countries) throws UnknownCountryException {
        HashSet<Integer> addingCountries = new HashSet<>();
        for (String country : countries) {
            Integer countryId = countriesIdMap.get(country);
            if (countryId == null)
                throw new UnknownCountryException(String.format("Неизвестная страна: %s", country));
            else
                addingCountries.add(countryId);
        }
        return Parser.parseArrayToArrayOfInt(addingCountries.toArray(Integer[]::new));
    }
}
