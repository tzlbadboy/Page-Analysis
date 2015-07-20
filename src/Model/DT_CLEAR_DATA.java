package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

import Config.Config;

public class DT_CLEAR_DATA {

	private String ID;
	private int rawID;
	private String listID;
	private String category;
	private String source;
	private String siteName;
	private String channel;
	private String urlName;
	private String url;
	private String title;
	private String herfText;
	private String text;
	private String htmlText;
	private String textCode;
	private String inputTime;
	private String pubDate;
	private String batchNo;
	
	public DT_CLEAR_DATA(ResultSet rs){
		try {
			this.ID = rs.getInt(1) + "_" + Config.getValue("NODENAME");
			this.rawID = rs.getInt(1);
			this.listID = rs.getString(2);
			this.category = rs.getString(3);
			this.source = rs.getString(4);
			this.siteName = rs.getString(5);
			this.channel = rs.getString(6);
			this.urlName = rs.getString(7);
			this.url = rs.getString(8);
			this.title = rs.getString(9);
			this.herfText = rs.getString(10);
			this.text = rs.getString(11);
			this.htmlText = "";
			this.textCode = "";
			this.inputTime = rs.getString(12);
			this.pubDate = rs.getString(13);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public DT_CLEAR_DATA(DT_RAW_DATA raw) {
		this.ID = raw.getID() + "_" + Config.getValue("NODENAME");
		this.rawID = raw.getID();
		this.listID = raw.getListID();
		this.category = raw.getCategory();
		this.source = raw.getSource();
		this.siteName = raw.getSiteName();
		this.channel = raw.getChannel();
		this.urlName = raw.getUrlName();
		this.url = raw.getUrl();
		this.title = raw.getTitle();
		this.herfText = raw.getHerfText();
		this.text = raw.getText();
		this.htmlText = "";
		this.textCode = "";
		this.inputTime = raw.getInputTime();
		this.pubDate = raw.getPubDate();
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getRawID() {
		return rawID;
	}

	public void setRawID(int rawID) {
		this.rawID = rawID;
	}

	public String getListID() {
		return listID;
	}

	public void setListID(String listID) {
		this.listID = listID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHerfText() {
		return herfText;
	}

	public void setHerfText(String herfText) {
		this.herfText = herfText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}

	public String getTextCode() {
		return textCode;
	}

	public void setTextCode(String textCode) {
		this.textCode = textCode;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
