package fengcone.mainpage.pojo.search;

import java.util.Date;

/**
 * 这个类用来封装搜索引擎搜索后的每一条内容 一条搜索结果为一个对象
 * 
 * @author fengcone
 * 
 */
public class SearchResult {

	// 类型是UUID，是这个对象的主键，在数据库中
	private String id;
	// Search ID
	private String searchId;
	// 封装的是用户输入的搜索的内容
	private String searchContent;
	// 封装标题
	private String title;
	// 封装内容
	private String content;
	// 封装搜索时间
	private Date searchTime;
	// 保存内容在网站上的产生时间
	private Date siteTime;
	// 保存URL
	private String url;
	// 保存imageurl
	private String imageUrl;
	// 保存引擎的名字
	private String engineName;
	// 是否被点击过，这个东西不会存在于数据库
	private Boolean clicked;
	// 最后一次点击的时间
	private Date clickTime;
	// 表示这是第几页的数据 最主要是方便翻页而设计
	private Integer page;
	public Boolean getClicked() {
		return clicked;
	}
	public void setClicked(Boolean clicked) {
		this.clicked = clicked;
	}
	public Date getClickTime() {
		return clickTime;
	}
	public void setClickTime(Date clickTime) {
		this.clickTime = clickTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		if (content.length()>250) { // 内容过多，直接截取
			content = content.substring(0, 250)+"...";
		}
		this.content = content;
	}
	public Date getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}
	public Date getSiteTime() {
		return siteTime;
	}
	public void setSiteTime(Date siteTime) {
		this.siteTime = siteTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		// URL 和ImageUrl 过长会怎么办呢？
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
	
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "SearchResult [id=" + id + ", searchContent=" + searchContent
				+ ", title=" + title + ", content=" + content + ", searchTime="
				+ searchTime + ", siteTime=" + siteTime + ", url=" + url
				+ ", imageUrl=" + imageUrl + ", engineName=" + engineName
				+ ", search=" + ", clicked=" + clicked
				+ ", clickTime=" + clickTime + "]";
	}
	
}
