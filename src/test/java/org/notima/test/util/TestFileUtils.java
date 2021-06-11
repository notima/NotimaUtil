package org.notima.test.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.notima.util.FileUtils;


public class TestFileUtils {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		String badFileName = "ABad&notsoGood,file/n ame.txt";
		String goodFileName = FileUtils.fileNameSafe(badFileName);
		System.out.println(goodFileName);
		assertEquals("ABadnotsoGoodfile-n_ame.txt", goodFileName);
		
	}

}
