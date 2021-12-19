package commandTests;

import userParametersRepository.UserParameters;
import userParametersRepository.UserParametersRepository;

import java.util.HashMap;
import java.util.Map;

public class RepositoryForTests implements UserParametersRepository {
    private final Map<String, UserParameters> data;

    public RepositoryForTests() {
        this.data = new HashMap<>();
    }

    @Override
    public void saveUserData(String userId, UserParameters userData) {
        data.put(userId, userData);
    }

    @Override
    public UserParameters getUserData(String userId) {
        return data.get(userId);
    }
}
