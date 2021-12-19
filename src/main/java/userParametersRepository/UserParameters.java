package userParametersRepository;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.Gson;
import kinopoiskAPI.API;
import kinopoiskAPI.Filter;
import parser.Parser;

import java.util.Collection;

public class  UserParameters {
    private JsonObject searchResult;
    private Filter filter;
    private int numberOfCurrentFilm;
    private int countOfFilmsOnCurrentPage;
    private int pagesCount;

    public UserParameters() {
        this.searchResult = API.getEmptySearchResult();
        this.filter = new Filter();
        this.pagesCount = 0;
        this.countOfFilmsOnCurrentPage = getCountOfFilmsOnCurrentPage(searchResult);
        this.numberOfCurrentFilm = 1;
    }

    public UserParameters(JsonObject searchResult, Filter filter, int numberOfCurrentFilm) throws Exception {
        setSearchResult(searchResult);
        setFilter(filter);
        setPagesCount(getPageCount(searchResult));
        setCountOfFilmsOnCurrentPage(getCountOfFilmsOnCurrentPage(searchResult));
        setNumberOfCurrentFilm(numberOfCurrentFilm);
    }

    public Filter getFilter() {
        return filter;
    }

    private void setFilter(Filter filter) {
        this.filter = filter;
    }

    public int getNumberOfCurrentFilm() {
        return numberOfCurrentFilm;
    }

    private void setNumberOfCurrentFilm(int numberOfCurrentFilm) throws Exception {
        if (numberOfCurrentFilm < 1)
            throw new Exception(String.format(
                    "numberOfCurrentFilm: %d, but numberOfCurrentFilm cannot be less than 10",
                    numberOfCurrentFilm));
        if (numberOfCurrentFilm - 1 > countOfFilmsOnCurrentPage)
            throw new Exception(String.format(
                    "numberOfCurrentFilm: %d, countOfFilmsOnCurrentPage: %d, " +
                            "but numberOfCurrentFilm cannot be more than count of films on current page",
                    numberOfCurrentFilm,
                    countOfFilmsOnCurrentPage));
        this.numberOfCurrentFilm = numberOfCurrentFilm;
    }

    private void setCountOfFilmsOnCurrentPage(int countOfFilmsOnCurrentPage) throws Exception {
        if (countOfFilmsOnCurrentPage < 0)
            throw new Exception(String.format(
                    "countOfFilmsOnCurrentPage: %d, but countOfFilmsOnCurrentPage cannot be less than 0",
                    countOfFilmsOnCurrentPage));
        this.countOfFilmsOnCurrentPage = countOfFilmsOnCurrentPage;
    }

    public int getNumberOfCurrentPage() {
        return filter.getPage();
    }

    private void setPagesCount(int pagesCount) throws Exception {
        if (pagesCount < 0)
            throw new Exception(String.format("pagesCount: %d, but pagesCount cannot be less than 0", pagesCount));
        this.pagesCount = pagesCount;
    }

    public JsonObject getSearchResult() {
        return searchResult;
    }

    private void setSearchResult(JsonObject searchResult) {
        this.searchResult = searchResult;
    }

    public JsonObject getCurrentFilm() {
        JsonArray films = new JsonArray((Collection<?>) searchResult.get("films"));
        var gson = new Gson();
        return !films.isEmpty()
                ? gson.fromJson(gson.toJson(films.get(numberOfCurrentFilm - 1)), JsonObject.class)
                : null;
    }

    public boolean nextFilm() {
        if (numberOfCurrentFilm == countOfFilmsOnCurrentPage)
            return false;
        numberOfCurrentFilm++;
        return true;
    }

    public boolean isLastPageOpen() {
        return getNumberOfCurrentPage() == pagesCount;
    }

    private int getPageCount(JsonObject searchResult) {
        return Parser.parseObjectToInt(searchResult.get("pagesCount"));
    }

    private int getCountOfFilmsOnCurrentPage(JsonObject searchResult) {
        var films = searchResult.get("films");
        if (films == null)
            return 0;
        return (new JsonArray((Collection<?>) films).size());
    }
}
