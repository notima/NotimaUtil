package org.notima.util;

import java.util.ArrayList;
import java.util.List;

public class TrxExpressionParser {

	private String trxExpression;
	private List<Integer> result;
	/**
	 * 
	 * @param trxExpression		The expression.
	 */
	public TrxExpressionParser(String trxExpression) {
		
		this.trxExpression = trxExpression;
		
	}
	
    /**
     * Function that parses a TrxExpression and converts it into a range of integers.
     * 
     * @return
     */
    public List<Integer> parseTrxExpression() throws Exception {
    	
    	result = new ArrayList<Integer>();
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
	
    public String toString() {
    	
    	StringBuffer buf = new StringBuffer();
    	
		if (result==null) {
			buf.append("<null>");
		} else {
			if (result.size()==0) {
				buf.append("Empty");
			} else {
				Integer lastInteger = null;
				boolean skipped = false;
				for (int i = 0; i<result.size(); i++) {
					Integer ii = result.get(i);
					if (lastInteger==null || (ii.intValue() > lastInteger+1) || i == (result.size()-1) ) {
						if (skipped) {
							buf.append("..");
							skipped = false;
						}
						buf.append(ii.toString() + " ");
					} else {
						skipped = true;
					}
					lastInteger = ii;
				}
			}
 		}
		return buf.toString();
    	
    }
	
}
