package botLogic.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String name();

    String arguments();

    int minArgs() default 0;

    int maxArgs() default Integer.MAX_VALUE;

    String description();
}
