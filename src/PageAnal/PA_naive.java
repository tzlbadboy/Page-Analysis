package PageAnal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Dao.Dao;
import Model.DT_CLEAR_DATA;
import Model.DT_RAW_DATA;
import SimHash.SimHash;


public class PA_naive {

	public static String PageAnal(String html){
		Document doc = Jsoup.parse(html);
		Elements es = doc.getAllElements();
		int temp_length = 0;
		Element longest = es.first();
		for (int i = 0; i < es.size(); i++){
			Element temp = es.get(i);
			if (temp.tagName().equals("p") && temp.text().length() > temp_length){
				temp_length = temp.text().length();
				longest = temp;
			}
			else if (!temp.tagName().equals("div") && !temp.ownText().contains("='") && !temp.ownText().contains("=\"") && temp.ownText().length() > temp_length){
				temp_length = temp.ownText().length();
				longest = temp;
			}
		}
		
		String content_tag_name = longest.tagName();
		String all = "";
		if (longest.parent() == null)
			all = longest.text();
		else{
			all = Text(longest.parent().parent(), content_tag_name);
		}
		all += "|&|" + longest.parent().parent().toString();
		return all;
	}
	
	public static String Text(Element e, String content_tag_name){
		String result = "";
		for (Element ee :e.children()){
			if (ee.tagName().equals("p"))
				result += ee.text();
			else if (ee.tagName().equals(content_tag_name))
				result += ee.ownText();
			else
				result += Text(ee, content_tag_name);
		}
		return result;
	}
	
	public static void runWithBloom(){
		Dao dao = new Dao();
		int page = 0;
		int offset = dao.getStart("DT_CLEAR_DATA");

//		BloomFilter<Integer>	bf_title;
//		BloomFilter<Integer>	bf_context;
//		File file1 = new File("bf_title");
//		File file2 = new File("bf_context");
//		if(file1.exists())
//			bf_title = new BloomFilter<Integer>(file1);
//		else
//			bf_title = new BloomFilter<Integer>(1,900000000);
//		if(file2.exists())
//			bf_context = new BloomFilter<Integer>(file2);
//		else
//			bf_context = new BloomFilter<Integer>(1,900000000);
//		
//		System.out.println("读取布隆过滤器成功");
		
		while (true){
			ArrayList<DT_RAW_DATA> rawData = dao.getRAWDATA(offset, page);					//url,spider_date,text,herfText,source
			if (rawData.isEmpty())
				break;
			System.out.println("----获取第" + page + "页成功----");
			ArrayList<DT_CLEAR_DATA> result = new ArrayList<DT_CLEAR_DATA>();	//url,spider_date,text,source
			for (DT_RAW_DATA data : rawData){
				DT_CLEAR_DATA temp = new DT_CLEAR_DATA(data);
				String contextHtml = PageAnal(data.getText());
				String[] split = contextHtml.split("|&|");
				temp.setText(split[0]);
				temp.setHtmlText(split[1]);
//				temp.setTitleCode(SimHash.hash(data.getTitle()));
				temp.setTextCode(String.valueOf(SimHash.hash(temp.getText())));
//				if (bf_title.contains(temp.getTitleCode())){
//					System.out.println("标题重复:" + temp.getTitleCode() + " " + temp.getTitle());
//					break;
//				}
//				if (bf_context.contains(temp.getContextCode())){
//					System.out.println("内容重复:" + temp.getContextCode() + " " + temp.getContext());
//					break;
//				}
				result.add(temp);
			}
			System.out.println("----分析第" + page + "页成功----");
			dao.saveCLEARDATA(result);
			System.out.println("----写入第" + page + "页成功----");
			result.clear();
			page++;
		}
		System.out.println("------共分析------" + page + "页");
		
//		bf_title.save("bf_title");
//		bf_context.save("bf_context");
//		System.out.println("保存布隆过滤器成功");
	}
	
//	public static void runWithoutBloom(){
//		Dao dao = new Dao();
//		int page = 0;
//		int offset = dao.getStart("DT_CLEAR_DATA");
//		
//		while (true){
//			ArrayList<DT_RAW_DATA> rawData = dao.getRAWDATA(offset, page);					//url,spider_date,text,herfText,source
//			if (rawData.isEmpty())
//				break;
//			System.out.println("----获取第" + page + "页成功----");
//			DT_CLEAR_DATA result;	//url,spider_date,text,source
//			for (DT_RAW_DATA data : rawData){
//				result = new DT_CLEAR_DATA(data);
//				String contextHtml = PageAnal(data.getContext());
//				String[] split = contextHtml.split("|&|");
//				result.setContext(split[0]);
//				result.setContextHTML(split[1]);
//				result.setTitleCode(SimHash.hash(data.getTitle()));
//				result.setContextCode(SimHash.hash(data.getContext()));
//				dao.saveCLEARDATA(result);
//			}
//			System.out.println("----分析写入第" + page + "页成功----");
//			page++;
//		}
//		System.out.println("------共分析" + page + "页------");
//		
//	}
	
	public static void main(String args[]) throws IOException{
		String test = "";
		File file = new File("test.htm");
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
        	test += lineTxt;
        }
        read.close();
		System.out.println(PageAnal(test));
	}
}
