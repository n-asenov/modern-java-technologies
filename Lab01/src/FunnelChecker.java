
public class FunnelChecker {
	public static boolean isFunnel(String str1, String str2) {
		if (str1.length() != str2.length() + 1) {
			return false;
		}

		int differences = 0;

		for (int index = 0; index < str2.length(); index++) {
			if (str2.charAt(index) != str1.charAt(index + differences)) {
				if (differences == 1) {
					return false;
				}
				differences = 1;
				index--;
			}
		}

		return true;
	}
}