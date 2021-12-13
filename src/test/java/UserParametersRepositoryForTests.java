import userParametersRepository.UserParameters;
import userParametersRepository.UserParametersRepository;

import java.util.HashMap;
import java.util.Map;

public class UserParametersRepositoryForTests implements UserParametersRepository {
    private final Map<String, UserParameters> repository;

    public UserParametersRepositoryForTests() {
        repository = new HashMap<>();
    }

    @Override
    public void saveUserData(String userId, UserParameters userData) {
        this.repository.put(userId, userData);
    }

    @Override
    public UserParameters getUserData(String userId) {
        return this.repository.get(userId);
    }
}
