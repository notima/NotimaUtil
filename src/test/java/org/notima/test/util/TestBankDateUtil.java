package org.notima.test.util;
import org.junit.Test;
import org.notima.util.BankDateUtil;

import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestBankDateUtil {

    @Test
    public void testBankDateCalculatorForSE() {

        String[] args = {"SE", "2023-01-01", "70"};
        LocalDate resultDate = BankDateUtil.bankDateCalculator(args);
        assertNotNull(resultDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedDate = LocalDate.parse("2023-04-12", formatter);

        assertEquals(expectedDate, resultDate);

    }
}

