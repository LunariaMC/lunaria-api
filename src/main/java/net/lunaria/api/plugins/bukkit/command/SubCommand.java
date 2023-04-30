package net.lunaria.api.plugins.bukkit.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    // Accepts several arguments such as 'add %player% %string%'.
    String arg() default "help";

    String description() default "Aucune information fournie";
}
