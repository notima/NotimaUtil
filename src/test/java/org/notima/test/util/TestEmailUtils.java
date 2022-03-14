package org.notima.test.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.notima.util.EmailUtils;

public class TestEmailUtils {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		boolean valid = EmailUtils.isValidEmail("daniel@notima.se");
		boolean invalid = EmailUtils.isValidEmail("daniel @notima.se");
		boolean invalid2 = EmailUtils.isValidEmail(null);
		
		assertTrue(valid);
		assertFalse(invalid);
		assertFalse(invalid2);
	}

}
