package commandTests;

import botLogic.Repository;
import botLogic.commands.RatingCommand;
import botLogic.commands.YearCommand;
import botLogic.exceptions.ratingCommandExceptions.IncorrectRatingException;
import botLogic.exceptions.yearCommandExceptions.IncorrectYearException;
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
public class YearCommandTests {
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
    public void setRatingTestRatingIsSpecifiedIncorrectly() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectYearException.class,
                    () -> YearCommand.setYear(new String[]{"2015"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setYearsTestYearIsNotNumber() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectYearException.class,
                    () -> YearCommand.setYear(new String[]{"2000", "?"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setYearsTestNegativeYear() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectYearException.class,
                    () -> YearCommand.setYear(new String[]{"-1", "2000"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setYearsTestFirstYearIsHigherThanSecond() {
        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    IncorrectYearException.class,
                    () -> YearCommand.setYear(new String[]{"2020", "2000"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setYearsTestCorrectYears() {
        Filter defaultFilter = new Filter();
        Filter expectedFilter = new Filter();
        int from = 1980;
        int to = 2020;
        expectedFilter.setYearFrom(from);
        expectedFilter.setYearTo(to);
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());

            YearCommand.setYear(new String[]{String.valueOf(from), String.valueOf(to)}, userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertEquals(expectedFilter.getYearFrom(), actual.getFilter().getYearFrom());
            assertEquals(expectedFilter.getYearTo(), actual.getFilter().getYearTo());
        }
    }

    @Test
    public void resetYearsTest() {
        Filter expectedFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(expectedFilter, userId);
            YearCommand.setYear(new String[0], userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertEquals(expectedFilter.getYearFrom(), actual.getFilter().getYearFrom());
            assertEquals(expectedFilter.getYearTo(), actual.getFilter().getYearTo());
        }
    }

    private JsonObject getFakeSearchResult() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pagesCount", 1);
        jsonObject.put("films", new JsonArray());
        return jsonObject;
    }
}
