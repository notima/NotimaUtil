package org.notima.test.util;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.util.TrxExpressionParser;

public class TestTrxExpressionParser {

	public TrxExpressionParser testParser;
	
	public String[] testExamples = {
		"1",
		"2,4-6",
		"56-45",
		"1-160"
	};
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		for (String s : testExamples) {
			testParser = new TrxExpressionParser(s);
			try {
				testParser.parseTrxExpression();
				System.out.print(s + " => ");
				printResult();
			} catch (Exception ee) {
				ee.printStackTrace();
				fail(ee.getMessage());
			}
		}
		
	}

	
	private void printResult() {
		
		if (testParser==null) {
			System.out.println("Result was null");
		} else {
			System.out.println(testParser.toString());
 		}
		
	}
	
}
