package Model;

public class DT_RAW_DATA {

	private int ID;
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
	private String inputTime;
	private String pubDate;
	
	public DT_RAW_DATA(int ID, String listID, String category,
			String source, String siteName, String channel, 
			String urlName, String url, String title, 
			String herfText, String text, String inputTime, 
			String pubDate) {
		this.ID = ID;
		this.listID = listID;
		this.category = category;
		this.source = source;
		this.siteName = siteName;
		this.channel = channel;
		this.urlName = urlName;
		this.url = url;
		this.title = title;
		this.herfText = herfText;
		this.text = text;
		this.inputTime = inputTime;
		this.pubDate = pubDate;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

}
