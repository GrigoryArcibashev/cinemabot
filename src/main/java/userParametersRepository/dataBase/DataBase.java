package userParametersRepository.dataBase;

public interface DataBase {
    void Set(String userId, String data);
    String Get(String userId);
}
