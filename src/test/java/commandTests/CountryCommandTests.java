package commandTests;

import botLogic.Repository;
import botLogic.commands.CountryCommand;
import botLogic.commands.GenreCommand;
import botLogic.exceptions.countryCommandExceptions.UnknownCountryException;
import botLogic.exceptions.genreCommandExceptions.UnknownGenreException;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CountryCommandTests {
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
    public void setCountriesTestIncorrectCountries() {
        String[] countriesNames = new String[]{"Россия", "США"};
        int[] countriesId = new int[]{0, 1};
        Map<String, Integer> countriesIdMap = new HashMap<>();
        countriesIdMap.put(countriesNames[0], countriesId[0]);
        countriesIdMap.put(countriesNames[1], countriesId[1]);

        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(API::getCountriesId).thenReturn(countriesIdMap);
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    UnknownCountryException.class,
                    () -> CountryCommand.setCountry(new String[]{"Франция"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setCountriesTestCorrectCountries() {
        String[] countriesNames = new String[]{"Россия", "США"};
        int[] countriesId = new int[]{0, 1};
        Map<String, Integer> countriesIdMap = new HashMap<>();
        countriesIdMap.put(countriesNames[0], countriesId[0]);
        countriesIdMap.put(countriesNames[1], countriesId[1]);

        Filter expectedFilter = new Filter();
        expectedFilter.setCountriesId(countriesId);
        Filter defaultFilter = new Filter();

        mockAPI.when(API::getCountriesId).thenReturn(countriesIdMap);
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());

            CountryCommand.setCountry(countriesNames, userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertArrayEquals(countriesId, actual.getFilter().getCountriesId());
        }
    }

    @Test
    public void resetCountriesTest() {
        Filter filter = new Filter();
        filter.setCountriesId(new int[]{1, 2, 3});
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(filter)).thenReturn(getFakeSearchResult());
        mockAPI.when(API::getCountriesId).thenReturn(new HashMap<String, Integer>());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(filter, userId);
            CountryCommand.setCountry(new String[0], userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertArrayEquals(new int[0], actual.getFilter().getCountriesId());
        }
    }

    private JsonObject getFakeSearchResult() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pagesCount", 1);
        jsonObject.put("films", new JsonArray());
        return jsonObject;
    }
}
