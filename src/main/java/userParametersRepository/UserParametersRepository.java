package userParametersRepository;

public interface UserParametersRepository {
    void saveUserData(String userId, UserParameters userData);

    UserParameters getUserData(String userId) throws Exception;
}
