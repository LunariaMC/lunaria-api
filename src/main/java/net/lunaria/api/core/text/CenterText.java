package net.lunaria.api.core.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CenterText {
    private static final Pattern PATTERN = Pattern.compile("&#[a-f0-9]{6}|&[a-f0-9k-o]|&r", Pattern.CASE_INSENSITIVE);

    public static String centerText(String message1, int px) {
        if (message1 != null && !message1.equals("")) {
            String message = strip(message1);
            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;
            char[] var5 = message.toCharArray();
            int toCompensate = var5.length;
            int spaceLength;
            for (spaceLength = 0; spaceLength < toCompensate; ++spaceLength) {
                char c = var5[spaceLength];
                if (c == 167) {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    if (c != 'l' && c != 'L') {
                        isBold = false;
                    } else {
                        isBold = true;
                    }
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                    ++messagePxSize;
                }
            }

            int halvedMessageSize = messagePxSize / 2;
            toCompensate = px - halvedMessageSize;
            spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;

            StringBuilder sb;
            for (sb = new StringBuilder(); compensated < toCompensate; compensated += spaceLength) {
                sb.append(" ");
            }
            return sb + message1;
        } else {
            return "";
        }
    }

    private static String strip(String s) {
        Matcher match = PATTERN.matcher(s);
        while (match.find()) {
            String color = s.substring(match.start(), match.end());
            s = s.replace(color, "");
            match = PATTERN.matcher(s);
        }
        return s;
    }
}
