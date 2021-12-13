package userParametersRepository;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import kinopoiskAPI.Filter;
import parser.Parser;

public class UserParameters {
    // todo хранить не JsonObject а конкретные модельки
    private JsonObject searchResult;
    private Filter filter;
    private int numberOfCurrentFilm;
    private int countOfFilmsOnCurrentPage;
    private int pagesCount;

    public UserParameters() {
        this.searchResult = null;//API.getEmptySearchResult();
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
        if (searchResult == null)
            return null;
        JsonArray films = (JsonArray) searchResult.get("films");
        return !films.isEmpty()
                ? (JsonObject) (films).get(numberOfCurrentFilm - 1)
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
        return searchResult == null ? 0 : Parser.parseObjectToInt(searchResult.get("pagesCount"));
    }

    private int getCountOfFilmsOnCurrentPage(JsonObject searchResult) {
        if (searchResult == null)
            return 0;
        var films = searchResult.get("films");
        if (films == null)
            return 0;
        return ((JsonArray) films).size();
    }
}
