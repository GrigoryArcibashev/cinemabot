package userParametersRepository.redisUserParametersRepository;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.google.gson.Gson;
import kinopoiskAPI.Filter;
import redis.clients.jedis.Jedis;
import userParametersRepository.UserParameters;
import userParametersRepository.UserParametersRepository;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class REDISUserParametersRepository implements UserParametersRepository {
    private final Jedis repository;
    private String host;
    private Integer port;
    private final Gson gson;

    public REDISUserParametersRepository() {
        uploadHostAndPort();
        this.repository = new Jedis(this.host, this.port);
        this.gson = new Gson();
    }

    @Override
    public void saveUserData(String userId, UserParameters userData) {
        this.repository.set(userId, gson.toJson(userData));
    }

    @Override
    public UserParameters getUserData(String userId) throws Exception {
        String userData = "";
        UserParameters userParameters;
        try {
            userData = repository.get(userId);
        }
        finally {
            if (userData == null){
                userParameters = new UserParameters();
                this.repository.set(userId, gson.toJson(userParameters));
            }
            else {
                userParameters = getUserParamFromString(userData);
            }
        }
        return userParameters;

    }

    private UserParameters getUserParamFromString(String data) throws Exception {
        Parameters userParamJson = gson.fromJson(data, Parameters.class);
        Filter filter = getFilter(userParamJson);
        return new UserParameters(userParamJson.searchResult, filter,
                userParamJson.numberOfCurrentFilm);

    }

    private Filter getFilter(Parameters userParamJson) {
        Filter filter = new Filter();
        filter.setGenresId(userParamJson.filter.genresId);
        filter.setCountriesId(userParamJson.filter.countriesId);
        filter.setType(userParamJson.filter.type);
        filter.setRatingFrom(userParamJson.filter.ratingFrom);
        filter.setRatingTo(userParamJson.filter.ratingTo);
        filter.setYearFrom(userParamJson.filter.yearFrom);
        filter.setYearTo(userParamJson.filter.yearTo);
        filter.setPage(userParamJson.filter.page);
        return filter;
    }

    private void uploadHostAndPort() {
        try {
            String fullPath = "../cinemabot/config.json";
            Reader reader = Files.newBufferedReader(Paths.get(fullPath));
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            host = (String) parser.get("redis-host");
            port = ((BigDecimal) parser.get("redis-port")).intValueExact();
        } catch (IOException | JsonException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
