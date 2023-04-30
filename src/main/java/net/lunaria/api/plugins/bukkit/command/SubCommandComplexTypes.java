package net.lunaria.api.plugins.bukkit.command;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum SubCommandComplexTypes {
    PLAYER(((args, complexArgsList, index) -> {
        Player target = Bukkit.getPlayer(args[index]);

        if (target == null) return false;
        complexArgsList.add(target);
        return true;
    }), "<joueur>"),

    STRING((args, complexArgsList, index) -> {
        complexArgsList.add(args[index]);
        return true;
    }, "<mot>"),

    INT((args, complexArgsList, index) -> {
        try {
            complexArgsList.add(Integer.parseInt(args[index]));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }, "<nombre>"),

    FLOAT((args, complexArgsList, index) -> {
        try {
            complexArgsList.add(Float.parseFloat(args[index]));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }, "<nombre à virgule>"),

    BOOLEAN((args, complexArgsList, index) -> {
        if (args[index].equalsIgnoreCase("true") || args[index].equalsIgnoreCase("false")) complexArgsList.add(Boolean.parseBoolean(args[index]));
        else {
            return false;
        }
        return true;
    }, "<true/false>"),

    PLAYER_NAME((args, complexArgsList, index) -> {
        complexArgsList.add(args[index]);
        return true;
    }, "<joueur>"),

    STRING_BUILDER((args, complexArgsList, index) -> {
        complexArgsList.add(String.join(" ", (String[]) Arrays.copyOfRange( args, index, args.length)));
        return true;
    }, "<texte>");

    public final CommandComplexType commandComplexType;
    public final String helpArg;

    SubCommandComplexTypes(CommandComplexType commandComplexType, String helpArg) {
        this.commandComplexType = commandComplexType;
        this.helpArg = helpArg;
    }
}
