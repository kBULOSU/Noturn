package com.noturn.gems.misc.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "BR"));

    private final static String[] CHARS = {
            "K",
            "M",
            "B",
            "T",
            "Q",
            "QQ",
            "S",
            "SS",
            "OC",
            "N",
            "D",
            "UN",
            "DD",
            "TR",
            "QT",
            "QN",
            "SD",
            "SPD",
            "OD",
            "ND",
            "VG",
            "UVG"
    };

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String toK(double count) {
        if (count < 1000D) {
            return String.format("%.2f", count);
        }

        int exp = (int) (Math.log(count) / Math.log(1000D));

        return String.format(
                "%.0f%s",
                count / Math.pow(1000D, exp),
                CHARS[exp - 1 >= CHARS.length ? CHARS.length - 1 : exp - 1]
        );
    }
}
