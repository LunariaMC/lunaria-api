package net.lunaria.api.plugins.bukkit.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    String arg() default "help";
    int position() default 0;
}
