package com.noturn.gems.misc.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("pt", "BR"));

    private static final String[] MONEY_FORMATS = {"M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD",
            "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG"};

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String toK(double value) {
        if (value <= 999999.0D) {
            return format(value);
        }

        int zeros = (int) Math.log10(value);
        int thou = zeros / 3;
        int arrayIndex = Math.min(thou - 2, MONEY_FORMATS.length - 1);

        return format(value / Math.pow(1000.0D, (double) arrayIndex + 2.0D)) + MONEY_FORMATS[arrayIndex];
    }
}
