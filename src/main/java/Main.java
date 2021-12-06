import botLogic.userData.UsersData;
import bots.consoleBot.ConsoleBot;
import userParametersRepository.Repository;
import userParametersRepository.jsonUserParametersRepository.JSONUserParametersRepository;

public class Main {
    public static void main(String[] args) {
        UsersData.initializeRepository(
                Repository.getInstance(new JSONUserParametersRepository(
                        "src/main/java/userParametersRepository/jsonUserParametersRepository/database.json")));
        new ConsoleBot().start();
    }
}