import botLogic.Repository;
import bots.consoleBot.ConsoleBot;
import userParametersRepository.jsonUserParametersRepository.JSONUserParametersRepository;

public class Main {
    public static void main(String[] args) {
        try {
            Repository.initializeRepository(
                    new JSONUserParametersRepository(
                            "src/main/java/userParametersRepository/jsonUserParametersRepository/database.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ConsoleBot().start();
    }
}