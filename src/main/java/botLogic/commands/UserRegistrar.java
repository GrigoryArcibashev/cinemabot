package botLogic.commands;

import botLogic.Repository;
import kinopoiskAPI.Filter;
import userParametersRepository.UserParameters;

public class UserRegistrar {
    public static UserParameters registerUser(String userId) throws Exception {
        Repository.updateSearchResult(new Filter(), userId);
        return Repository.getUserData(userId);
    }
}
