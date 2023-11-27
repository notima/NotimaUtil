package org.notima.util;

import java.util.ArrayList;
import java.util.List;

public class TrxExpressionParser {

	private String trxExpression;
	
	public TrxExpressionParser(String trxExpression) {
		
		this.trxExpression = trxExpression;
		
	}
	
    /**
     * Function that parses a TrxExpression and converts it into a range of integers.
     * 
     * @param trxExpression
     * @return
     */
    public List<Integer> parseTrxExpression() throws Exception {
    	
    	List<Integer> result = new ArrayList<Integer>();
    	if (trxExpression==null)
    		return result;
    	
    	String[] segments = trxExpression.split(",");

    	Integer ifrom, ito;
    	for(String seg : segments) {
    		if (seg.contains("-")) {
    			String[] fromTo = seg.split("-");
    			ifrom = Integer.parseInt(fromTo[0]);
    			ito = Integer.parseInt(fromTo[1]);
    			for (int i = ifrom; i<(ito+1); i++) {
    				result.add(i);
    			}
    		} else {
    			result.add(Integer.parseInt(seg));
    		}
    	}
    	
    	return result;
    	
    }
	
	
}
