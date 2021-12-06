package botLogic.commands.adviseCommand;

import botLogic.exceptions.CommandException;
import botLogic.commands.adviseCommand.formatter.Formatter;
import botLogic.Repository;
import com.github.cliftonlabs.json_simple.JsonObject;
import kinopoiskAPI.Filter;
import parser.Parser;
import userParametersRepository.UserParameters;

public class AdviseCommand {
    public static String advise(String userId) throws Exception {
        UserParameters userParameters = Repository.getUserData(userId);
        if (userParameters == null) {
            registerUser(userId);
            userParameters = Repository.getUserData(userId);
        }
        return getNextFilm(userParameters, userId);
    }

    private static String getNextFilm(UserParameters userParameters, String userId) throws Exception {
        JsonObject filmInfo = userParameters.getCurrentFilm();
        if (filmInfo == null)
            throw new CommandException("Фильмы не найдены");
        int filmId = Parser.parseObjectToInt(filmInfo.get("filmId"));
        String descriptionOfFilm = Formatter.getInformationAboutFilm(filmId);
        goToNextFilm(userParameters, userId);
        return descriptionOfFilm;
    }

    private static void goToNextFilm(UserParameters userParameters, String userId) throws Exception {
        if (userParameters.nextFilm())
            Repository.saveUserData(userParameters, userId);
        else if (userParameters.isLastPageOpen())
            resetSearch(userParameters, userId);
        else
            goToNextPage(userParameters, userId);
    }

    private static void goToNextPage(UserParameters userParameters, String userId) throws Exception {
        Filter filter = userParameters.getFilter();
        filter.setPage(filter.getPage() + 1);
        Repository.updateSearchResult(filter, userId);
    }

    private static void resetSearch(UserParameters userParameters, String userId) throws Exception {
        Filter filter = userParameters.getFilter();
        filter.setPage(1);
        Repository.updateSearchResult(filter, userId);
    }

    private static void registerUser(String userId) throws Exception {
        Repository.updateSearchResult(new Filter(), userId);
    }
}
