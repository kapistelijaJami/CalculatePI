package calculatepi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class PiPrecision {
	private final BigDecimal pi;
	private final BigDecimal lowerLimit;
	private final BigDecimal upperLimit;
	private final int maxPrecision;
	private int correctDigitCount = -1;
	
	public PiPrecision(BigDecimal pi, BigDecimal lowerLimit, BigDecimal upperLimit, int maxPrecision) {
		this.pi = pi;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.maxPrecision = maxPrecision;
	}
	
	private int calculateCorrectDigitCount() { //Correct digits to the right of the decimal point.
		BigDecimal tempLow = lowerLimit;
		BigDecimal tempHigh = upperLimit;
		
		BigInteger lowerInteger = tempLow.toBigInteger();
		BigInteger upperInteger = tempHigh.toBigInteger();
		
		int precision = 0;
		while (lowerInteger.equals(upperInteger) && precision < maxPrecision) {
			tempLow = tempLow.movePointRight(1);
			tempHigh = tempHigh.movePointRight(1);
			
			lowerInteger = tempLow.toBigInteger();
			upperInteger = tempHigh.toBigInteger();
			
			precision++;
		}
		
		correctDigitCount = Math.min(maxPrecision - 1, precision - 1); //One fewer since we don't count the number 3 left of decimal point. (Assumes the first digit (3) is correct.)
		return correctDigitCount;
	}
	
	@Deprecated //Seems slower
	private int calculateCorrectDigitCount2() { //Correct digits to the right of the decimal point.
		BigDecimal diff = upperLimit.subtract(lowerLimit);
		
		String text = diff.toPlainString();
        int decimalIndex = text.indexOf('.');
		
		if (decimalIndex == -1 || decimalIndex == text.length() - 1) {
            correctDigitCount = 0;
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
		
		correctDigitCount = Math.min(maxPrecision - 1, zerosCount);
		return correctDigitCount;
	}
	
	public int getCorrectDigitCount() {
		int accurateDigits = correctDigitCount;
		if (accurateDigits == -1) {
			accurateDigits = calculateCorrectDigitCount();
		}
		return accurateDigits;
	}
	
	public String getAccuratePi() {
		return getAccuratePi(-1);
	}
	
	public String getAccuratePi(int digits) {
		int accurateDigits = getCorrectDigitCount();
		if (digits <= 0) {
			digits = accurateDigits;
		}
		
		BigDecimal tempPi = pi.setScale(Math.min(digits, accurateDigits), RoundingMode.FLOOR);
		return tempPi.toString();
	}
	
	public String getLowerBound() {
		return lowerLimit.toString();
	}
	
	public String getUpperBound() {
		return upperLimit.toString();
	}
	
	@Override
	public String toString() {
		return pi.toString();
	}
	
	public void printResult(int digitCount) {
		int accurateDigits = getCorrectDigitCount();
		if (digitCount <= 0) {
			digitCount = accurateDigits;
		}
		
		System.out.println("Lower bound: " + lowerLimit);
		System.out.println("Upper bound: " + upperLimit);
		
		System.out.println("Accurate digit count: " + accurateDigits);
		if (digitCount != accurateDigits) {
			System.out.println("Printing " + digitCount + " digits");
		}
		System.out.println("Accurate pi: " + getAccuratePi(digitCount));
	}
	
	public void printResult() {
		printResult(-1);
	}
	
	public void saveToFile(String fileName) {
		saveToFile(fileName, -1);
	}
	
	public void saveToFile(String fileName, int digitCount) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write(getAccuratePi(digitCount));
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
