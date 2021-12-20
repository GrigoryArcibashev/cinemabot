import botLogic.Repository;
import bots.consoleBot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import userParametersRepository.redisUserParametersRepository.REDISUserParametersRepository;

public class Main {
    public static void main(String[] args) {
        try {
            Repository.initializeRepository(new REDISUserParametersRepository());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  new ConsoleBot().start();

        try{
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        var bot = new TelegramBot("HelpCinemaBot", "5072222334:AAHsjJYCs3QaeaOubnstVlQH597U1Jrpbdk");
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}