package net.lunaria.api.core.text;

public class MoneyFormatUtil {
    public static String formatMoneyValue(float value) {
        String suffix = "";
        if (value >= 1000000) {
            value /= 1000000;
            suffix = "m";
        } else if (value >= 1000) {
            value /= 1000;
            suffix = "k";
        }
        return String.format("%.2f%s", value, suffix);
    }
}
