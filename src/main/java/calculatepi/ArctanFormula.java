package calculatepi;

import java.math.BigDecimal;
import java.math.MathContext;

public class ArctanFormula {
	//Implemented this: https://www.youtube.com/watch?v=LIg-6glbLkU&t=2m46s and more info: https://www.youtube.com/watch?v=LIg-6glbLkU&t=5m36s
	
	//This seems to be a lot faster, but can't quarantee that it's exactly accurate to some specific precision, since it doesn't go up and down,
	//except in the arctan calculation. But it's not the actual pi, so can't get lower and upper bounds. But it's probably accurate still.
	public static PiPrecision getPi(int precision) {
		int maxPrecision = precision + 10;
		MathContext mc = new MathContext(maxPrecision);
		
		//do the seven arctans
		BigDecimal ans = BigDecimal.valueOf(6348).multiply(getArcTan(BigDecimal.valueOf(2852), mc, maxPrecision));
		ans = ans.add(BigDecimal.valueOf(1180).multiply(getArcTan(BigDecimal.valueOf(4193), mc, maxPrecision)));
		ans = ans.add(BigDecimal.valueOf(2372).multiply(getArcTan(BigDecimal.valueOf(4246), mc, maxPrecision)));
		ans = ans.add(BigDecimal.valueOf(1436).multiply(getArcTan(BigDecimal.valueOf(39307), mc, maxPrecision)));
		ans = ans.add(BigDecimal.valueOf(1924).multiply(getArcTan(BigDecimal.valueOf(55603), mc, maxPrecision)));
		ans = ans.add(BigDecimal.valueOf(2500).multiply(getArcTan(BigDecimal.valueOf(211050), mc, maxPrecision)));
		ans = ans.subtract(BigDecimal.valueOf(2832).multiply(getArcTan(BigDecimal.valueOf(390112), mc, maxPrecision)));
		
		//TODO: Not sure how to increase term count after checking how accurate the pi is though. I think I need to increase the arctan term count to make it more accurate.
		//I think it works if I just check that the term is in the correct precision.
		
		PiPrecision piPrecision = new PiPrecision(ans, ans, ans, maxPrecision);
		
		return piPrecision;
	}
	
	private static BigDecimal getArcTan(BigDecimal x, MathContext mc, int precision) { //Calculates arctan(1/x) with taylor series.
		BigDecimal oneOverX = BigDecimal.ONE.divide(x, mc);
		BigDecimal xSquared = x.multiply(x);
		
		BigDecimal result = BigDecimal.ZERO;
		
		int counter = 0;
		while (true) {
			BigDecimal term = oneOverX.divide(BigDecimal.valueOf(counter * 2 + 1), mc);
			
			if (counter % 2 == 0) {
				result = result.add(term);
			} else {
				result = result.subtract(term);
			}
			
			if (countPrecision(term) >= precision) { //Made it stop based on precision of the term
				break;
			}
			
			oneOverX = oneOverX.divide(xSquared, mc);
			counter++;
		}
		
		return result;
	}
	
	private static int countPrecision(BigDecimal num) {
		String text = num.toPlainString();
        int decimalIndex = text.indexOf('.');
		
		if (decimalIndex == -1 || decimalIndex == text.length() - 1) {
            return 0;
        }
		
		int zerosCount = 0;
        for (int i = decimalIndex + 1; i < text.length(); i++) {
            if (text.charAt(i) == '0') {
                zerosCount++;
            } else {
                break; // Exit loop when nonzero digit encountered
            }
        }
		
		return zerosCount;
	}
}
