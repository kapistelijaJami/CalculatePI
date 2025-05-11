package calculatepi;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Pi is calculated by adding terms from infinite series.
 * This keeps track of the differing k values that's needed to calculate the term.
 */
public class TermInfo {
	public int k;
	public BigDecimal kFactorial;
	public BigDecimal k3Factorial;
	public BigDecimal k6Factorial;
	public MathContext mc;
	
	public TermInfo(int k, BigDecimal kFactorial, BigDecimal k3Factorial, BigDecimal k6Factorial, MathContext mc) {
		this.k = k;
		this.kFactorial = kFactorial;
		this.k3Factorial = k3Factorial;
		this.k6Factorial = k6Factorial;
		this.mc = mc;
	}
}
