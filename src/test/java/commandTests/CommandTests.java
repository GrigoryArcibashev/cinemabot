package commandTests;

import botLogic.CommandHandler;
import botLogic.commands.Command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
* Тут есть сложность: чтобы протестировать работу команд, придется задействовать API,
* а его использование ограничено 10 запросами в минуту и 100 запросами в день (используется бесплатный токен).
* Как быть?
* */

public class CommandTests {
    private static final Map<String, Command> commands;

    static {
        commands = getDescriptionsOfCommands();
    }

    private static Map<String, Command> getDescriptionsOfCommands() {
        Map<String, Command> commands = new HashMap<>();
        for (Method method : CommandHandler.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                Command cmd = method.getAnnotation(Command.class);
                commands.put(cmd.name(), cmd);
            }
        }
        return commands;
    }
}
