package bw.co.pulabank.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {
    private static final NumberFormat BWP;

    static {
        Locale locale = Locale.forLanguageTag("en-BW");
        BWP = NumberFormat.getCurrencyInstance(locale);
        BWP.setCurrency(Currency.getInstance("BWP"));

        // Configure the DecimalFormat explicitly instead of applying a raw pattern
        DecimalFormat df = (DecimalFormat) BWP;
        df.setGroupingUsed(true);
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setPositivePrefix("BWP ");
        df.setNegativePrefix("-BWP ");
    }

    public static String format(double amount) {
        return BWP.format(amount);
    }

    public static String formatWithSign(double amount) {
        return (amount >= 0 ? "+" : "-") + format(Math.abs(amount));
    }
}