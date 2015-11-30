
public class ReverseString {

	public static void main(String args[]) {
		ReverseString rs = new ReverseString();
		System.out.println(rs.reverseString("StringToReverse"));
	}

	public String reverseString(String s) {

		int i = 0;
		char[] chars = null;
		chars = new char[s.length()];

		while (i < s.length()) {
			chars[i] = s.charAt(i);
			i++;
		}

		int j = s.length() - 1;
		StringBuilder sb = new StringBuilder(j);

		while (j >= 0) {
			sb.append(chars[j]);
			j--;
		}

		String sr = sb.toString();
		return sr;

	}
}
