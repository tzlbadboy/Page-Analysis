package PageAnal;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Config.Config;
import Dao.Dao;
import Model.DT_CLEAR_DATA;
import Model.DT_RAW_DATA;
import Model.Time;
import SimHash.SimHash;


public class PA {

	long dbTime = 0, timeTime = 0, paTime = 0, hashTime = 0, saveTime = 0;
	
	public static String PageAnal(String html){
		Document doc = Jsoup.parse(html);
//		Document doc = null;
//		try {
//			doc = Jsoup.connect(html).get();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		Elements es = doc.getAllElements();
		int temp_length = 0;
		Element longest = es.first();
		for (int i = 0; i < es.size(); i++){
			Element temp = es.get(i);
			temp.removeAttr("href");
			temp.removeAttr("src");
			if (temp.tagName().equals("img")){
				temp.remove();
				continue;
			}
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
//		System.out.println(longest.tagName());
		String all = "";
		if (longest.parent() == null){
			all = longest.text();
			all += "|&|" + longest.toString();
		}
		else{
			String temp = HTMLText(longest.parent().parent(), content_tag_name);
			all = Text(longest.parent().parent(), content_tag_name);
			all += "|&|" + temp;
		}
		
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
	
	public static String HTMLText(Element e, String content_tag_name){
		String result = "";
		for (Element ee : e.children()){
			int flag = 0;
			if (ee.tagName().equals("p")){
				result += ee;
				flag = 1;
			}
			else if (ee.tagName().equals(content_tag_name)){
				result += ee;
				flag = 1;
			}
			else {
				if (ee.children() == null){
//					System.out.println("叶节点"+ee.tagName());
				}
				else{
					String temp = HTMLText(ee, content_tag_name);
					if (!temp.equals("")){
						result += temp;
						flag = 1;
					}
				}
			}
			if (flag == 0){
				ee.remove();
			}
		}
		return result;
		
	}
	
	public static String PageAnalWithVote(String html){
		class tagVote{
			public tagVote(String name, int vote, int max, Element flag, int deep) {
				this.name = name;
				this.vote = vote;
				this.max = max;
				this.flag = flag;
				this.deep = deep;
			}
			String name;
			int vote;
			int max;
			Element flag;
			int deep;
		};
		String all = "";
		
		Document doc = Jsoup.parse(html);
		Elements es = doc.getAllElements();
		int tempLength = 0;
		ArrayList<tagVote> tagList = new ArrayList<tagVote>();

		for (Element e : es){
			int length = e.ownText().length();
			if (length > 0){
				int flag = 0;
				for (tagVote tagFlag : tagList){
					if (tagFlag.name.equals(e.tagName())){
						tagFlag.vote += (int)(Math.log(tempLength)/Math.log(5));
						if (tagFlag.max < length)
							tagFlag.max = length;
						flag = 1;
						break;
					}
				}
				if (flag == 0)
					tagList.add(new tagVote(e.tagName(), (int)(Math.log(tempLength)/Math.log(5)), length, e, e.parents().size()));
			}
		}
		try{
			tagVote temp = tagList.get(0);
			
			for (tagVote tagFlag : tagList){
				if (tagFlag.vote > temp.vote)
					temp = tagFlag;
			}
			for (Element e : temp.flag.parent().parent().parent().getAllElements()){
				if (e.tagName().equals(temp.name) && e.parents().size() == temp.deep)
					all += e.ownText();
			}
			
		}catch(Exception e){
			System.out.println(html);
		}
		return all;
	}
	
	public void run() throws Exception{
		Dao dao = new Dao();
		int page = 0;
		int offset = dao.getStart("DT_CLEAR_DATA");
		System.out.println("偏移量: " + offset);
		Long batchNo = System.currentTimeMillis();
		System.out.println(" 批次号: " + batchNo);
		
		
		ResultSet rawData;
		ArrayList<DT_CLEAR_DATA> result;
		DT_CLEAR_DATA temp;
		String contextHtml;
		String[] split;
		long startMill = 0, endMill = 0;
		Time time = null;
		
//		BloomFilter<Integer>	bf_title;
//		BloomFilter<Integer>	bf_context;
//		File file1 = new File("bf_title");
//		File file2 = new File("bf_context");
//		if(file1.exists())
//			bf_title = new BloomFilter<Integer>(file1);
//		else
//			bf_title = new BloomFilter<Integer>(1,9000000);
//		if(file2.exists())
//			bf_context = new BloomFilter<Integer>(file2);
//		else
//			bf_context = new BloomFilter<Integer>(1,9000000);
//		
//		System.out.println("读取布隆过滤器成功");
	
		long click = System.currentTimeMillis();
		while (true){
			//-----获取数据-----
			startMill = System.currentTimeMillis();
			List rawDataList = dao.getRAWDATA(offset, page);	
			Connection conn = (Connection) rawDataList.get(1) ;
			rawData = (ResultSet) rawDataList.get(0) ;					//url,spider_date,text,herfText,source
			endMill = System.currentTimeMillis();
			dbTime += endMill - startMill;
			if (!rawData.next()){
				System.out.println("------此次分析" + page + "页------");
				System.out.println("ZZZzzz进入睡眠zzzZZZ");
				try {
					Thread.sleep(Integer.parseInt(Config.getValue("SLEEPTIME")));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				page = 0;
				offset = dao.getStart("DT_CLEAR_DATA");
				System.out.println("偏移量 " + offset);
				batchNo = System.currentTimeMillis();
				continue;
			}
			System.out.println("----获取第" + page + "页成功----");
			result = new ArrayList<DT_CLEAR_DATA>();	//url,spider_date,text,source
			rawData.previous();
			while (rawData.next()){
				temp = new DT_CLEAR_DATA(rawData);
//				System.out.println(DateAnal.getHtmlTime(data.getText()));
				//-----获取时间-----
				startMill = System.currentTimeMillis();
				time = DateAnal.getHtmlTime(temp.getText());
				endMill = System.currentTimeMillis();
				
				timeTime += endMill - startMill;
				if (time != null)
					temp.setPubDate(time.toString());
//				else{
//					FileWriter fw = new FileWriter("log.txt",true);
//					fw.write(data.getUrl() + "\r\n");
//					fw.close();
//				}
//				System.out.println(data.getID());
				//-----正文提取-----
				startMill = System.currentTimeMillis();
				contextHtml = PageAnal(temp.getText());
				endMill = System.currentTimeMillis();
				paTime += endMill - startMill;
				split = contextHtml.split("\\|\\&\\|");
				temp.setText(split[0]);
				temp.setHtmlText(split[1]);
//				temp.setTitleCode(SimHash.hash(temp.getTitle()));
//				System.out.println(temp.getText());
				//-----计算哈希值-----
				startMill = System.currentTimeMillis();
				temp.setTextCode(String.valueOf(SimHash.hash(temp.getText())));
				endMill = System.currentTimeMillis();
				hashTime += endMill - startMill;
				temp.setBatchNo(String.valueOf(batchNo));
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
			conn.close();
			System.out.println("----分析第" + page + "页成功----");
			//-----保存数据-----
			startMill = System.currentTimeMillis();
			
			dao.saveCLEARDATA(result);
			
			endMill = System.currentTimeMillis();
			saveTime += endMill - startMill;
			System.out.println("----写入第" + page + "页成功----");
			result.clear();
			page++;
			
			if (System.currentTimeMillis() - click > 100000){
				click = System.currentTimeMillis();
				System.out.println("取数耗时：" + dbTime + " 时间耗时：" + timeTime + " 正文耗时：" + paTime + " hash耗时：" + hashTime + " 存数耗时：" + saveTime);
			}
			
		}
		
		
//		bf_title.save("bf_title");
//		bf_context.save("bf_context");
//		System.out.println("保存布隆过滤器成功");
	}
	/*public static void main(String[] args){
		
		
	}*/
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
//				String[] split = contextHtml.split("\\|\\&\\|");
//				result.setContext(split[0]);
//				result.setContextHTML(split[1]);
//				result.setTitleCode(SimHash.hash(result.getTitle()));
//				result.setContextCode(SimHash.hash(result.getContext()));
//				dao.saveCLEARDATA(result);
//			}
//			System.out.println("----分析写入第" + page + "页成功----");
//			page++;
//		}
//		System.out.println("------共分析" + page + "页------");
//	}
	
	public static void main(String args[]) throws Exception{
//		String test = "";
//		File file = new File("test.htm");
//		InputStreamReader read = new InputStreamReader(new FileInputStream(file));
//        BufferedReader bufferedReader = new BufferedReader(read);
//        String lineTxt = null;
//        while((lineTxt = bufferedReader.readLine()) != null){
//        	test += lineTxt;
//        }
//        read.close();
//		System.out.println(PageAnal(test));
//		System.out.println(PageAnalWithVote(test));
		
		
		PA pa = new PA();
		pa.run();
		
//		String html = "http://bjdxfy.chinacourt.org/public/paperview.php?id=1264553";
//        html = PageAnal(html);
//        String split[] = html.split("\\|\\&\\|");
//        System.out.println(split[0]);
//        System.out.println(split[1]);
	}
}
