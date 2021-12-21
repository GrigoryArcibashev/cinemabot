package userParametersRepository.redisUserParametersRepository;

import com.google.gson.Gson;
import kinopoiskAPI.Filter;
import userParametersRepository.UserParameters;
import userParametersRepository.UserParametersRepository;
import userParametersRepository.dataBase.DataBase;


public class REDISUserParametersRepository implements UserParametersRepository {
    private final Gson gson;
    private final DataBase repository;

    public REDISUserParametersRepository(DataBase dataBase) {
        this.repository = dataBase;
        this.gson = new Gson();
    }

    @Override
    public void saveUserData(String userId, UserParameters userData) {
        this.repository.Set(userId, gson.toJson(userData));
    }

    @Override
    public UserParameters getUserData(String userId) throws Exception {
        String userData = "";
        UserParameters userParameters;
        try {
            userData = this.repository.Get(userId);
        }
        finally {
            if (userData == null){
                userParameters = new UserParameters();
                this.repository.Set(userId, gson.toJson(userParameters));
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
}
