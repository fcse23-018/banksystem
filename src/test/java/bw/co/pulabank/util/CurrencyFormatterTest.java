package bw.co.pulabank.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyFormatterTest {

    @Test
    public void formatPositiveAmount() {
        String out = CurrencyFormatter.format(1234.5);
        assertEquals("BWP 1,234.50", out);
    }

    @Test
    public void formatWithSignPositive() {
        String out = CurrencyFormatter.formatWithSign(50);
        assertEquals("+BWP 50.00", out);
    }

    @Test
    public void formatWithSignNegative() {
        String out = CurrencyFormatter.formatWithSign(-75.25);
        assertEquals("-BWP 75.25", out);
    }
}
