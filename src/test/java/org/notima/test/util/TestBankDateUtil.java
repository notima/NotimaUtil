package org.notima.test.util;

import org.junit.Test;
import org.notima.util.BankDateUtil;
import static org.junit.Assert.*;

public class TestBankDateUtil {

    @Test
    public void testBankDateCalculatorForSE() {

        String[] args = {"SE", "2023-01-01", "70"};
        String resultDate = BankDateUtil.bankDateCalculator(args);
        assertNotNull(resultDate);
        
        String expectedDate = "2023-04-12";
        assertEquals(expectedDate, resultDate);

    }
}

