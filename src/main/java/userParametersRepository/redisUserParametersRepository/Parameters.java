package userParametersRepository.redisUserParametersRepository;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Parameters {
    JsonObject searchResult;
    FilterFields filter;
    int numberOfCurrentFilm;
    int countOfFilmsOnCurrentPage;
    int pagesCount;
}
