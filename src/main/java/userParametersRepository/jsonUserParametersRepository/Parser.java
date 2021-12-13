package userParametersRepository.jsonUserParametersRepository;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.google.gson.Gson;
import kinopoiskAPI.Filter;
import org.json.JSONObject;
import userParametersRepository.UserParameters;

import static parser.Parser.parseObjectToInt;

public class Parser {
    public static JsonObject parseUserParametersToJsonObject(UserParameters parameters) {
        JsonObject parametersAsJson = new JsonObject();
        parametersAsJson.put("searchResult", parameters.getSearchResult());
        parametersAsJson.put("filter", parseFilterToJsonObject(parameters.getFilter()));
        parametersAsJson.put("numberOfCurrentFilm", parameters.getNumberOfCurrentFilm());
        parametersAsJson.put("numberOfCurrentPage", parameters.getNumberOfCurrentPage());
        return parametersAsJson;
    }

    public static JsonObject parseFilterToJsonObject(Filter filter) {
        return Jsoner.deserialize(new Gson().toJson(filter), new JsonObject());
    }

    public static UserParameters parseJsonObjectToUserParameters(JsonObject jsonObject) {
        try {
            return new UserParameters(
                    (JsonObject) jsonObject.get("searchResult"),
                    parseJsonObjectToFilter((JsonObject) jsonObject.get("filter")),
                    parseObjectToInt(jsonObject.get("numberOfCurrentFilm")));
        } catch (Exception e) {
            e.printStackTrace();
            return new UserParameters();
        }
    }

    private static Filter parseJsonObjectToFilter(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject.toJson(), Filter.class);
    }
}
