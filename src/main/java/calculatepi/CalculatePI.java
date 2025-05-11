package calculatepi;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalculatePI {
    public static void main(String[] args) {
		//System.out.println(calculatePI(1000000000)); //Billion terms
		
		//System.out.println(chudnovskysFormula(500, 10000)); //Much much better!
		//(chudnovsky's (terms: 50, MathContext maxPrecision: 10000), about 700 digits are correct)
		//(chudnovsky's (500, 10000), all the digits were correct in the output, ran out of system.out space, cause line too long. Accurate up to 7076 digits.):
		
		/*PiPrecision pi = ChudnovskyFormula.getPiByTermCount(900, 15000);
		pi.printResult();*/
		
		int digits = 10000;
		/*PiPrecision pi = ChudnovskyFormula.getPi(digits);
		pi.printResult(digits);*/
		
		//pi.saveToFile("accuratePi.txt", digits);
		
		/*for (int i = 2; i <= 100; i++) {
			PiPrecision pi = chudnovskysFormula(i, 5000);
			System.out.println("(i = " + i + "): " + pi.getCorrectDigitCount());
			//System.out.println(pi.getAccuratePi());
		}*/
		
		
		PiPrecision pi = ArctanFormula.getPi(digits); //This is somehow way faster
		pi.printResult(digits);
		pi.saveToFile("test.txt", digits);
    }
	
	public static PiPrecision calculatePI(long terms) { //Slow way to do it.
		double pi = 0;
		double lowerLimit = 0;
		double upperLimit = 0;
		
		for (long i = 0; i < terms; i++) {
			double denominator = i * 2 + 1;
			double term = 4 / denominator;
			
			if (i % 2 == 0) {
				pi += term;
				upperLimit = pi;
			} else {
				pi -= term;
				lowerLimit = pi;
			}
		}
		
		return new PiPrecision(BigDecimal.valueOf(pi), BigDecimal.valueOf(lowerLimit), BigDecimal.valueOf(upperLimit), 15); //About 15 is max precision for double
	}
}
