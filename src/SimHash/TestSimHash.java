//package SimHash;
//
//
//public class TestSimHash {
//	public static void main(String[] args){
//		String path1 = "E:\\testData\\1.txt";
//		String path2 = "E:\\testData\\2.txt";
//		/*String[] rslt;
//		rslt = CutWords.getWords(p);
//		int i = 0;
//		int temp;
//		while(rslt[i] != null && rslt[i].length() > 0){
//			temp = Times33Hash.Times33HashForChinese(rslt[i]);
//			i++;
//		}*/
//		int hashValue1 = 0;
//		hashValue1 = SimHash.hash(path1);
//		int hashValue2 = 0;
//		hashValue2 = SimHash.hash(path2);
//		int[] bitSet1 = new int[32];
//		int[] bitSet2 = new int[32];
//		for (int k = 0; k < 32; k++){
//			bitSet1[k] = 0;
//			bitSet2[k] = 0;
//		}
//		System.out.print("The hash value for article_1 is ");
//		for (int k = 0; k < 32; k++)
//			if (((hashValue1 >> (31 - k)) & 0x1) != 0)
//				{
//				System.out.print(1);
//				bitSet1[k] = 1;
//				}
//			else
//				{
//				System.out.print(0);
//				bitSet1[k] = 0;
//				}
//		System.out.println();
//		System.out.print("The hash value for article_2 is ");
//		for (int k = 0; k < 32; k++)
//			if (((hashValue2 >> (31 - k)) & 0x1) != 0)
//	
//				{
//				System.out.print(1);
//				bitSet2[k] = 1;
//				}
//			else
//				{
//				System.out.print(0);
//				bitSet2[k] = 0;
//				}
//		System.out.println();
//		int diffBitNum = 0;
//		for (int k = 0; k < 32; k++)
//			if (bitSet1[k] != bitSet2[k])
//				diffBitNum++;
//		System.out.println("Different character number is " + diffBitNum);
//	}
//}
