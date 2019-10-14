
public class FractionSimplifier {
	public static String simplify(String fraction) {
		String[] splittedFraction = fraction.split("/");
		int numerator = getNumber(splittedFraction[0]);
		int denominator = getNumber(splittedFraction[1]);
		int gcd = gcd(numerator, denominator);

		if (numerator % denominator == 0) {
			return new Integer(numerator / denominator).toString();
		}
		
		return (numerator / gcd) + "/" + (denominator / gcd);
	}

	private static int getNumber(String number) {
		return Integer.parseInt(number);
	}

	private static int gcd(int a, int b) {
		if (a == 0) {
			return b;
		}

		return gcd(b % a, a);
	}
}
