package SimHash;


public class Times33Hash {
	private static final int seed = 33;
	private static final int cap = 0x7FFFFFFF;
	
	public static int Times33HashForChinese(String chineseChar){
		int rslt = 1;
		int len = chineseChar.length();
		for (int i = 0; i < len; i++){
			rslt = rslt * rslt + chineseChar.charAt(i);
		}
		return cap & rslt;
	}

}
