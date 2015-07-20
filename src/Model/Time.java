package Model;

public class Time {
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String second;
	
	public Time() {
		this.year = "";
		this.month = "";
		this.day = "";
		this.hour = "00";
		this.minute = "00";
		this.second = "00";
	}
	
	public String toString(){
		if (year.length() > 4)
			year = "2014";
		if (month.length() > 2)
			month = "12";
		if (day.length() > 2)
			day = "10";
		if (hour.length() > 2)
			hour = "12";
		if (minute.length() > 2)
			minute = "00";
		if (second.length() > 2)
			second = "00";
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
}
