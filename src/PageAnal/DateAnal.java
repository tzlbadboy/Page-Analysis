package PageAnal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Time;

public class DateAnal {

	public static Time getHtmlTime(String html)
	{
		Time time=new Time();
		String date=null;
		//Pattern accurateTimePattern = Pattern.compile("<[^>]+>([^:<]+([:][0-9]{2})+)<",Pattern.CASE_INSENSITIVE);
		//Pattern anotherAccuratePattern=Pattern.compile("<[^>]*>[^[0-9]]*([^<]+([:][^<]{2}){1,2})[^<]*<",Pattern.CASE_INSENSITIVE);
		Pattern testPattern=Pattern.compile("\\d{4}(\\-|\\/|\\.|年|月|日)\\d{1,2}(\\-|\\/|\\.|年|月|日)\\d{1,2}日?(\\s)*\\d{2}:\\d{2}(:\\d{2})?",Pattern.CASE_INSENSITIVE);
		//Pattern timePattern=Pattern.compile("",Pattern.CASE_INSENSITIVE);
		//html="hahahah hahahah hahahaha ";
		Pattern simplePattern=Pattern.compile("\\d{4}(\\-|\\/|\\.|年|月|日)\\d{1,2}(\\-|\\/|\\.|年|月|日)\\d{1,2}日?",Pattern.CASE_INSENSITIVE);
		Matcher accurateMatcher=testPattern.matcher(html);
		if(accurateMatcher.find())
		{		
			date=accurateMatcher.group();
		}
		else
		{
			Matcher simpleMatcher=simplePattern.matcher(html);
			if(simpleMatcher.find())
			{
				date=simpleMatcher.group();
			}
		}
		if(date==null)
			return null;
		//System.out.println(date);
		int count=0;
		char c=' ';
		StringBuilder sub=new StringBuilder();
		
		for(int i=0;i<date.length();i++)
		{
			c=date.charAt(i);
			if(c>='0'&&c<='9')
			{
				sub.append(c);
				continue;
			}
			if(sub.length()!=0)
			{
				switch (count) {
				case 0:
					time.setYear(sub.toString());
					sub.setLength(0);
					count++;
					break;
				case 1:
					time.setMonth(sub.toString());
					sub.setLength(0);
					count++;
					break;
				case 2:
					time.setDay(sub.toString());
					sub.setLength(0);
					count++;
					break;
				case 3:
					time.setHour(sub.toString());
					sub.setLength(0);
					count++;
					break;
				case 4:
					time.setMinute(sub.toString());
					sub.setLength(0);
					count++;
					break;
				case 5:
					time.setSecond(sub.toString());
					sub.setLength(0);
					count++;
					break;
				default:
					break;
				}
			}
		}
		if(sub.length()!=0)
		{
			switch (count) {
			case 0:
				time.setYear(sub.toString());
				sub.setLength(0);
				count++;
				break;
			case 1:
				time.setMonth(sub.toString());
				sub.setLength(0);
				count++;
				break;
			case 2:
				time.setDay(sub.toString());
				sub.setLength(0);
				count++;
				break;
			case 3:
				time.setHour(sub.toString());
				sub.setLength(0);
				count++;
				break;
			case 4:
				time.setMinute(sub.toString());
				sub.setLength(0);
				count++;
				break;
			case 5:
				time.setSecond(sub.toString());
				sub.setLength(0);
				count++;
				break;
			default:
				break;
			}
		}
		if(count==0)
			return null;
		
		return time;
	}
	public static void main(String []args)
	{
		String test="2014年05月07日 。kfjslfjlaaskfdsk";
		getHtmlTime(test);
	}
	
}
