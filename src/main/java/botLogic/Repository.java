package botLogic;

import botLogic.commands.UserRegistrar;
import com.github.cliftonlabs.json_simple.JsonObject;
import kinopoiskAPI.Filter;
import userParametersRepository.UserParameters;
import userParametersRepository.UserParametersRepository;

public class Repository {
    private static UserParametersRepository userParametersRepository;

    private Repository() {
    }

    public static void initializeRepository(UserParametersRepository userParametersRepository) throws Exception {
        if (Repository.userParametersRepository == null)
            Repository.userParametersRepository = userParametersRepository;
        else
            throw new Exception("Attempt to modify a UserParametersRepository instance");
    }

    public static void saveUserData(UserParameters userParameters, String userId) {
        check();
        userParametersRepository.saveUserData(userId, userParameters);
    }

    public static UserParameters getUserData(String userId) throws Exception {
        check();
        UserParameters userParameters = userParametersRepository.getUserData(userId);
        if (userParameters == null
                || userParameters.getSearchResult() == null
                || userParameters.getFilter() == null) {
            userParameters = UserRegistrar.registerUser(userId);
        }
        return userParameters;
    }

    public static void updateSearchResult(Filter filter, String userId) throws Exception {
        JsonObject searchResult = kinopoiskAPI.API.getInformationAboutFilmsByFilter(filter);
        UserParameters userParameters = new UserParameters(searchResult, filter, 1);
        saveUserData(userParameters, userId);
    }

    private static void check() {
        if (userParametersRepository == null)
            throw new NullPointerException(
                    "Attempt to access the user parameters repository, but the repository was not initialized");
    }
}