package org.notima.test.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.notima.util.NumberUtils;


public class TestNumberUtils {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		double roundMe = 10000.100291;
		double rounded = NumberUtils.roundToPrecision(roundMe, 2);
		assertEquals(10000.1, rounded, 0);
		
	}

}
