package dataIO.consoleOutput;

import dataIO.outputModule.Message;
import dataIO.outputModule.OutputModule;

public class ConsoleOutput implements OutputModule {

    @Override
    public void sendMessage(Message message, String userId) {
        for (int i = 0; i < 20; i++)
            System.out.print('-');
        System.out.printf("\n>> USERID: %s\n>> MESSAGE:\n%s\n", userId, message.message());
        for (int i = 0; i < 20; i++)
            System.out.print('-');
        System.out.println('\n');
    }
}
