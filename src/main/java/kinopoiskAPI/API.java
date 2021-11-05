package kinopoiskAPI;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import kinopoiskAPI.httpRequest.HTTPRequest;
import parser.Parser;

import java.util.HashMap;
import java.util.Map;

public class API {
    private static final String domain;

    static {
        domain = "https://kinopoiskapiunofficial.tech/api/";
    }

    public static Map<String, Integer> getCountriesId() {
        return makeMapByKey(
                (JsonArray) getIdOfCountriesAndGenres().get("countries"),
                "country");
    }

    public static Map<String, Integer> getGenresId() {
        return makeMapByKey(
                (JsonArray) getIdOfCountriesAndGenres().get("genres"),
                "genre");
    }

    public static JsonObject getInformationAboutFilmById(int filmId) {
        String url = String.format("%sv2.2/films/%d/", domain, filmId);
        return getRequestResult(url);
    }

    public static JsonObject getInformationAboutFilmsByFilter(Filter filter) {
        StringBuilder filtersInRequest = new StringBuilder();
        for (var country :
                filter.getCountries())
            filtersInRequest.append(String.format("country=%d&", country));
        for (var genre : filter.getGenres())
            filtersInRequest.append(String.format("genre=%d&", genre));
        filtersInRequest.append(String.format(
                "order=%s&type=%s&ratingFrom=%d&ratingTo=%d&yearFrom=%d&yearTo=%d&page=%d",
                filter.getOrder(),
                filter.getType(),
                filter.getRatingFrom(),
                filter.getRatingTo(),
                filter.getYearFrom(),
                filter.getYearTo(),
                filter.getPage()));
        String url = String.format("%sv2.1/films/search-by-filters?%s", domain, filtersInRequest);
        return getRequestResult(url);
    }

    public static JsonObject getEmptySearchResult() {
        JsonObject result = new JsonObject();
        result.put("pagesCount", 0);
        result.put("films", new JsonArray());
        return result;
    }

    private static JsonObject getIdOfCountriesAndGenres() {
        String url = String.format("%sv2.1/films/filters", domain);
        return getRequestResult(url);
    }

    private static Map<String, Integer> makeMapByKey(JsonArray array, String key) {
        Map<String, Integer> result = new HashMap<>();
        for (var item : array) {
            JsonObject itemAsJsonObject = (JsonObject) item;
            result.put(
                    (String) itemAsJsonObject.get(key),
                    Parser.parseObjectToInt(itemAsJsonObject.get("id")));
        }
        return result;
    }

    private static JsonObject getRequestResult(String url) {
        try {
            return parseJsonObjectFromString(HTTPRequest.request(url));
        } catch (Exception e) {
            return getEmptySearchResult();
        }
    }

    private static JsonObject parseJsonObjectFromString(String jsonObjectAsString) {
        return jsonObjectAsString != null
                ? Jsoner.deserialize(jsonObjectAsString, new JsonObject())
                : null;
    }
}
