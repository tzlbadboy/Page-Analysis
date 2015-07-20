package SimHash;

import jeasy.analysis.MMAnalyzer;

public class CutWords {
//	public static String[] getWords(String path)
//	{
//		try
//		{
//			BufferedReader reader = new BufferedReader(new FileReader(path));
//			String content = "";
//			String line;
//			while ((line = reader.readLine()) != null)
//			{
//				content += line;
//			}
//
//			MMAnalyzer analyzer = new MMAnalyzer();
//
//			String result = analyzer.segment(content, ",");
//			return result.split(",");
//		} catch (IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		return null;
//	}
	
	public static String[] getWords(String line)
	{
		try
		{
			MMAnalyzer analyzer = new MMAnalyzer();

			String result = analyzer.segment(line, ",");
			return result.split(",");
		} catch (Exception ex)
		{
//			System.out.println(line);
//			ex.printStackTrace();
			return line.split("。");
		}
	}
	
}
