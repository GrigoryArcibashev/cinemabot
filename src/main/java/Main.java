import botLogic.Repository;
import bots.consoleBot.ConsoleBot;
import userParametersRepository.redisUserParametersRepository.REDISUserParametersRepository;

public class Main {
    public static void main(String[] args) {
        try {
            Repository.initializeRepository(new REDISUserParametersRepository());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ConsoleBot().start();
    }
}