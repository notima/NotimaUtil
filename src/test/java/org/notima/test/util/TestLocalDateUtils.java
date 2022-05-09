package org.notima.test.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.notima.util.LocalDateUtils;

public class TestLocalDateUtils {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetDateRangeStr() {
		String emptyDates = LocalDateUtils.getDateRangeStr(null, null, null, null);
		assertEquals("", emptyDates);
	}

}
