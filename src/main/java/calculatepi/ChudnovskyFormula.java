package calculatepi;

import java.math.BigDecimal;
import java.math.MathContext;

public class ChudnovskyFormula {
	public static PiPrecision getPiByTermCount(int terms, int maxPrecision) { //At least 2 terms needed.
		MathContext mc = new MathContext(maxPrecision);
		
		BigDecimal result = BigDecimal.valueOf(13591409); //Starting term when k = 0.
		
		BigDecimal lowerLimit = BigDecimal.ZERO;
		BigDecimal upperLimit = result;
		
		BigDecimal kFactorial = BigDecimal.ONE;
		BigDecimal k3Factorial = BigDecimal.ONE;
		BigDecimal k6Factorial = BigDecimal.ONE;
		
		for (int k = 1; k < terms; k++) {
			TermInfo termInfo = new TermInfo(k, kFactorial, k3Factorial, k6Factorial, mc);
			BigDecimal term = getNextTerm(termInfo);
			kFactorial = termInfo.kFactorial;
			k3Factorial = termInfo.k3Factorial;
			k6Factorial = termInfo.k6Factorial;
			
			if (k % 2 == 0) {
				result = result.add(term);
				upperLimit = result;
			} else {
				result = result.subtract(term);
				lowerLimit = result;
			}
		}
		
		BigDecimal endingTerm = BigDecimal.valueOf(10005).sqrt(mc).divide(BigDecimal.valueOf(4270934400L), mc);
		
		upperLimit = BigDecimal.ONE.divide(upperLimit.multiply(endingTerm), mc);
		lowerLimit = BigDecimal.ONE.divide(lowerLimit.multiply(endingTerm), mc);
		
		BigDecimal pi = BigDecimal.ONE.divide(result.multiply(endingTerm), mc);
		
		return new PiPrecision(pi, lowerLimit, upperLimit, maxPrecision);
	}
	
	public static PiPrecision getPi(int precision) { //precision = How many digits must be correct
		int maxPrecision = precision + 10;
		MathContext mc = new MathContext(maxPrecision);
		
		BigDecimal result = BigDecimal.valueOf(13591409); //Starting term when k = 0.
		
		BigDecimal lowerLimit = BigDecimal.ZERO;
		BigDecimal upperLimit = result;
		
		TermInfo termInfo = new TermInfo(1, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, mc);
		while (true) {
			BigDecimal term = getNextTerm(termInfo);
			
			
			if (termInfo.k % 2 == 0) {
				result = result.add(term);
				upperLimit = result;
			} else {
				result = result.subtract(term);
				lowerLimit = result;
			}
			termInfo.k++;
			if (termInfo.k <= 2) {
				continue;
			}
			
			if (termInfo.k % 50 == 0) { //Only check every 50th term if it's accurate enough.
				BigDecimal endingTerm = BigDecimal.valueOf(10005).sqrt(mc).divide(BigDecimal.valueOf(4270934400L), mc);

				BigDecimal upperLimitRes = BigDecimal.ONE.divide(upperLimit.multiply(endingTerm), mc);
				BigDecimal lowerLimitRes = BigDecimal.ONE.divide(lowerLimit.multiply(endingTerm), mc);

				BigDecimal pi = BigDecimal.ONE.divide(result.multiply(endingTerm), mc);

				PiPrecision piPrecision = new PiPrecision(pi, lowerLimitRes, upperLimitRes, maxPrecision);

				if (piPrecision.getCorrectDigitCount() >= precision) { //TODO: check if it's faster to stop by checking the precision of the term like in arctan formula.
					return piPrecision;
				}
			}
		}
	}
	
	private static BigDecimal getNextTerm(TermInfo termInfo) {
		termInfo.kFactorial = termInfo.kFactorial.multiply(BigDecimal.valueOf(termInfo.k));
		termInfo.k3Factorial = termInfo.k3Factorial.multiply(getFactorialFactor(termInfo.k, false));
		termInfo.k6Factorial = termInfo.k6Factorial.multiply(getFactorialFactor(termInfo.k, true));

		BigDecimal term = termInfo.k6Factorial.divide(termInfo.kFactorial.pow(3).multiply(termInfo.k3Factorial), termInfo.mc);

		BigDecimal rightTermNumerator = BigDecimal.valueOf(13591409).add(BigDecimal.valueOf(545140134).multiply(BigDecimal.valueOf(termInfo.k)));
		BigDecimal rightTermDenominator = BigDecimal.valueOf(640320).pow(3 * termInfo.k);

		BigDecimal rightTerm = rightTermNumerator.divide(rightTermDenominator, termInfo.mc);

		term = term.multiply(rightTerm);
		return term;
	}
	
	private static BigDecimal getFactorialFactor(long k, boolean sixMore) { //What to multiply to advance the factor for the next k.
		BigDecimal res = BigDecimal.ONE;
		int howManyMore = sixMore ? 6 : 3;
		
		for (int i = 1; i <= howManyMore; i++) {
			res = res.multiply(BigDecimal.valueOf(k - 1).multiply(BigDecimal.valueOf(howManyMore)).add(BigDecimal.valueOf(i)));
		}
		
		return res;
	}
}
