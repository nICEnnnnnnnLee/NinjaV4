package nicelee.common.util;

public class RandomUtil {

	
	/**
	 * 从字典获取i位随机字符
	 * @param alphabet
	 * @param i
	 * @return
	 */
	public static String getRandom(String alphabet, int i) {
		StringBuilder sb = new StringBuilder(i);
		for (int j = 0; j < i; j++) {
			int m = (int) (Math.random() * alphabet.length());
			sb.append(alphabet.charAt(m));
		}
		return sb.toString();
	}
	
	/**
	 * 从字典获取i位随机字符
	 * @param i
	 * @return
	 */
	static String alphabetNumbers = "0123456789";
	public static String getRandomNumber(int i) {
		StringBuilder sb = new StringBuilder(i);
		for (int j = 0; j < i; j++) {
			int m = (int) (Math.random() * alphabetNumbers.length());
			sb.append(alphabetNumbers.charAt(m));
		}
		return sb.toString();
	}
}
