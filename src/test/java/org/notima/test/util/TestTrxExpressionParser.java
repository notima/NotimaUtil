package org.notima.test.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.util.TrxExpressionParser;

public class TestTrxExpressionParser {

	public String[] testExamples = {
		"1",
		"2,4-6",
		"56-45"
	};
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		List<Integer> result;
		
		for (String s : testExamples) {
			TrxExpressionParser test = new TrxExpressionParser(s);
			try {
				result = test.parseTrxExpression();
				System.out.print(s + " => ");
				printResult(result);
			} catch (Exception ee) {
				ee.printStackTrace();
				fail(ee.getMessage());
			}
		}
		
	}

	
	private void printResult(List<Integer> r) {
		
		if (r==null) {
			System.out.println("Result was null");
		} else {
			if (r.size()==0) {
				System.out.println("Result was empty");
			} else {
				for (Integer ii : r) {
					System.out.print(ii.toString() + " ");
				}
				System.out.println();
			}
 		}
		
	}
	
}
