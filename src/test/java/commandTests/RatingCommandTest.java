package commandTests;

import botLogic.Repository;
import botLogic.commands.RatingCommand;
import botLogic.exceptions.ratingCommandExceptions.IncorrectRatingException;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import userParametersRepository.UserParameters;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RatingCommandTest {
    private final String userId = "user";
    private MockedStatic<API> mockAPI;

    @Before
    public void setUp() {
        mockAPI = Mockito.mockStatic(API.class);
        if (Repository.isInitialized())
            return;
        try {
            Repository.initializeRepository(new RepositoryForTests());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @After
    public void close() {
        mockAPI.close();
    }

    @Test
    public void setRatingTestRatingIsSpecifiedIncorrectly(){
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectRatingException.class,
                    () -> RatingCommand.setRating(new String[]{"4"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setRatingsTestRatingIsNotNumber() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectRatingException.class,
                    () -> RatingCommand.setRating(new String[]{"two", "9"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setRatingsTestOutOfRange() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectRatingException.class,
                    () -> RatingCommand.setRating(new String[]{"0", "12"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setRatingsTestFirstRatingIsHigherThanSecond() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectRatingException.class,
                    () -> RatingCommand.setRating(new String[]{"6", "2"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setRatingsTestCorrectRatings() {
        Filter defaultFilter = new Filter();
        Filter expectedFilter = new Filter();
        int from = 2;
        int to = 6;
        expectedFilter.setRatingFrom(from);
        expectedFilter.setRatingTo(to);
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());

            RatingCommand.setRating(new String[]{String.valueOf(from), String.valueOf(to)}, userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertEquals(expectedFilter.getRatingFrom(), actual.getFilter().getRatingFrom());
            assertEquals(expectedFilter.getRatingTo(), actual.getFilter().getRatingTo());
        }
    }

    @Test
    public void resetRatingsTest() {
        Filter expectedFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(expectedFilter, userId);
            RatingCommand.setRating(new String[0], userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertEquals(expectedFilter.getRatingFrom(), actual.getFilter().getRatingFrom());
            assertEquals(expectedFilter.getRatingTo(), actual.getFilter().getRatingTo());
        }
    }

    private JsonObject getFakeSearchResult() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pagesCount", 1);
        jsonObject.put("films", new JsonArray());
        return jsonObject;
    }
}
