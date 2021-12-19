package commandTests;

import botLogic.Repository;
import botLogic.commands.GenreCommand;
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
public class GenreCommandTests {
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
    public void setGenresTestIncorrectGenres() {
        String[] genreNames = new String[]{"комедия", "боевик"};
        int[] genresId = new int[]{0, 1};
        Map<String, Integer> genresIdMap = new HashMap<>();
        genresIdMap.put(genreNames[0], genresId[0]);
        genresIdMap.put(genreNames[1], genresId[1]);

        Filter defaultFilter = new Filter();
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(API::getGenresId).thenReturn(genresIdMap);
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            UserParameters actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());
            assertThrows(
                    UnknownGenreException.class,
                    () -> GenreCommand.setGenre(new String[]{"драма"}, userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setGenresTestCorrectGenres() {
        String[] genreNames = new String[]{"комедия", "боевик"};
        int[] genresId = new int[]{0, 1};
        Map<String, Integer> genresIdMap = new HashMap<>();
        genresIdMap.put(genreNames[0], genresId[0]);
        genresIdMap.put(genreNames[1], genresId[1]);

        Filter expectedFilter = new Filter();
        expectedFilter.setGenresId(genresId);
        Filter defaultFilter = new Filter();

        mockAPI.when(API::getGenresId).thenReturn(genresIdMap);
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(defaultFilter)).thenReturn(getFakeSearchResult());
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(expectedFilter)).thenReturn(getFakeSearchResult());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(defaultFilter, userId);
            actual = Repository.getUserData(userId);
            assertNotNull(actual);
            assertEquals(defaultFilter, actual.getFilter());

            GenreCommand.setGenre(genreNames, userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertArrayEquals(genresId, actual.getFilter().getGenresId());
        }
    }

    @Test
    public void resetGenresTest() {
        Filter filter = new Filter();
        filter.setGenresId(new int[]{1, 2, 3});
        mockAPI.when(() -> API.getInformationAboutFilmsByFilter(filter)).thenReturn(getFakeSearchResult());
        mockAPI.when(API::getGenresId).thenReturn(new HashMap<String, Integer>());
        UserParameters actual = null;
        try {
            Repository.updateSearchResult(filter, userId);
            GenreCommand.setGenre(new String[0], userId);
            actual = Repository.getUserData(userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertNotNull(actual);
            assertArrayEquals(new int[0], actual.getFilter().getGenresId());
        }
    }

    private JsonObject getFakeSearchResult() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("pagesCount", 1);
        jsonObject.put("films", new JsonArray());
        return jsonObject;
    }
}
