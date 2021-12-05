package botLogic.commands.adviseCommand;

import botLogic.exceptions.CommandException;
import botLogic.commands.adviseCommand.formatter.Formatter;
import botLogic.userData.UsersData;
import com.github.cliftonlabs.json_simple.JsonObject;
import kinopoiskAPI.Filter;
import parser.Parser;
import userParametersRepository.UserParameters;

public class AdviseCommand {
    public static String advise(String userId) throws Exception {
        UserParameters userParameters = UsersData.getParametersOfCurrentUser();
        if (userParameters == null) {
            registerUser();
            userParameters = UsersData.getParametersOfCurrentUser();
        }
        return getNextFilm(userParameters);
    }

    private static String getNextFilm(UserParameters userParameters) throws Exception {
        JsonObject filmInfo = userParameters.getCurrentFilm();
        if (filmInfo == null)
            throw new CommandException("Фильмы не найдены");
        int filmId = Parser.parseObjectToInt(filmInfo.get("filmId"));
        String descriptionOfFilm = Formatter.getInformationAboutFilm(filmId);
        goToNextFilm(userParameters);
        return descriptionOfFilm;
    }

    private static void goToNextFilm(UserParameters userParameters) throws Exception {
        if (userParameters.nextFilm())
            UsersData.saveParametersOfCurrentUser(userParameters);
        else if (userParameters.isLastPageOpen())
            resetSearch(userParameters);
        else
            goToNextPage(userParameters);
    }

    private static void goToNextPage(UserParameters userParameters) throws Exception {
        Filter filter = userParameters.getFilter();
        filter.setPage(filter.getPage() + 1);
        UsersData.saveSearchResultOfCurrentUser(filter);
    }

    private static void resetSearch(UserParameters userParameters) throws Exception {
        Filter filter = userParameters.getFilter();
        filter.setPage(1);
        UsersData.saveSearchResultOfCurrentUser(filter);
    }

    private static void registerUser() throws Exception {
        UsersData.saveSearchResultOfCurrentUser(new Filter());
    }
}
