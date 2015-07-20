package SimHash;

public class SimHash{
	
	public static int ACCURACY = 1;
	
	public static int hash(String path){
		String wordscutted[];
		wordscutted = CutWords.getWords(path);
		int tempHash;
		int[] bitsCount = new int[32];
		long ans =0;
		for (int j = 0; j < 32; j++){
			bitsCount[j] = 0;
		}
		int stringArrayLength = wordscutted.length;
//		for (String s : wordscutted)
//			System.out.print(s + " ");
//		System.out.println();
		for (int j = 0; j < stringArrayLength; j++){
			tempHash = Times33Hash.Times33HashForChinese(wordscutted[j]);
			for (int k = 0; k < 32; k++)
				if (((tempHash >> k) & 0x1) != 0)
					bitsCount[k]++;
				else
					bitsCount[k]--;
			
		}
		
		for (int k = 31; k >= 0; k--){
			if (bitsCount[k] >= 0)
				ans++;
			ans = ans * 2;
		}
		ans = ans / 2;
		return (int)ans;
	}
	
	public static boolean isSim(int code1, int code2){
		if (HaminDistence(code1, code2) <= SimHash.ACCURACY )
			return true;
		else
			return false;
	}
	
	public static int HaminDistence(int code1, int code2){
		
		int bit = code1 ^ code2;
		bit = round(bit,0);
		bit = round(bit,1);
		bit = round(bit,2);
		bit = round(bit,3);
		bit = round(bit,4);
		return bit;
	}
	
	private static int pow(int c){
		return 1<<c;
	}
	
	private static int mask(int c){
		switch(c){
		case 0:return 0x55555555;
		case 1:return 0x33333333;
		case 2:return 0x0f0f0f0f;
		case 3:return 0x00ff00ff;
		case 4:return 0x0000ffff;
		default:return 0;
		}
	}
	
	private static int round(int n, int c){
		return  (((n) & mask(c)) + ((n) >> pow(c) & mask(c)));
	}
	
//	public static void main(String[] args) {
//		System.out.println(hash("新一直都是中国股市的一个传统习惯，而今年更是迎来新股炒作的甜蜜时光。于是新股上市后的连续涨停就成了A股市场一道独特的风景。到时下的兰石重装，截至10月31日，该股票自10月9日上市以来，17个交易日均连续涨停。在一级市场上中签兰石重装的投资者如果一直持有该股票的话，自然是赚得心花怒放了。兰石重装连续17涨停也有其客观的原因。比如，该股发行价格低，只有1.68元/股，创下A股2002年以来IPO最低值等。尽管如此，兰石重装上市以来的17个连续涨停，并不是A股市场的一个光彩纪录，它更是A股市场问题的集中井喷。新股上市连续涨停的数量越多，它表明A股市场存在的问题就越大。而兰石重装的17连板，至少暴露出股市的三大问题。首先暴露出来的是目前A股市场对新股上市首日涨跌幅限制制度的不合理。根据目前的'限炒令'，新股上市首日的涨幅基本上是控制在44%左右。由于涨幅受到了控制，新股上市后很快就'秒停'，新股在上市首日难以换手，这就为后续的连续涨停埋下了伏笔。实际上，这个问题是完全可以解决的。比如在新股上市首日引进熔断机制。新股开盘价可以控制在发行价涨幅的50%之内，开盘后当股价涨幅达到100%时盘中停牌30分钟，随后将涨幅放大到200%，达到这一涨幅时再停牌30分钟，然后放开涨幅限制。这样新股可以在上市首日充分换手，后续继续涨停的可能性就大为减少了。第二个问题，是中国股市投机炒作盛行、投资者风险意识缺乏。虽然兰石重装的发行价低，但其发行市盈率并不低，达到了21.73倍。但由于A股市场不成熟，兰石重装的低价就成了该股遭到暴炒的最好理由，至10月31日第17个涨停，兰石重装的股价达到了11.12元，较发行价上涨了561.91%。股价对应的市盈率达到140倍左右，动态市盈率也超过百倍，远远高于同行业上市公司股票二级市场的平均市盈率。投资者争相杀进这样的股票里搏取差价，无异于是一种'搏傻'行为。第三个问题是市场监管乏力。对于炒新，也包括市场上各种股票的炒作行为，管理层在一定程度上予以包容，这是很有必要的。但这种炒作不能涉及股价操纵或内幕交易。面对新股上市后的连续涨停，尤其是兰石重装上市以来17个连续涨停，就直观判断来说，没有资金操纵是很难办到的。但到底有没有股价操纵行为的存在，这需要监管机构核查了才能知道。但面对新股的这种连续涨停，今年几乎没有听闻有监管机构介入调查的，当然也就更加没有查处结果了。这其实也是对炒新的一种纵容。(编辑:罗懿)"));
//	}
//	
}