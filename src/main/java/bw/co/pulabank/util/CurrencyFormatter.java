package bw.co.pulabank.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private static final NumberFormat BWP = NumberFormat.getCurrencyInstance(new Locale("en", "BW"));

    static {
        DecimalFormat df = (DecimalFormat) BWP;
        df.applyPattern("BWP #,##0.00");
    }

    public static String format(double amount) {
        return BWP.format(amount);
    }

    public static String formatWithSign(double amount) {
        return (amount >= 0 ? "+" : "-") + format(Math.abs(amount));
    }
}