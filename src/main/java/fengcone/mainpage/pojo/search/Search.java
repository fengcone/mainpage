package fengcone.mainpage.pojo.search;

import java.util.Date;
import java.util.List;

/**
 * 这里封装一个搜索对象
 * 
 * @author fengcone
 * 
 */
public class Search {
	// 类型是UUID，是这个对象的主键，在数据库中
	private String id;
	// 封装的是用户输入的搜索的内容
	private String searchContent;
	// 封裝关键搜索对象 数据库中存Content的ID
	private SearchResult keyResult;
	// 搜索引擎的名字
	private String engineName;
	// 封装搜索时间
	private Date searchTime;
	// 封装点击过的对象
	private List<SearchResult> clickedResults;
	// 所有的搜索结果 Redis中只缓存这个
	private List<SearchResult> allResults;
	// 翻页的时候当前页的 搜索结果对象 其实一般就是下一页
	private List<SearchResult> pageResults;
	// 保存这个Search对象有哪些页的结果 逗号隔开
	private String page;


	public Search(){}
	public Search(String id){
		this.id = id;
	}
	
	public SearchResult getKeyResult() {
		return keyResult;
	}
	public void setKeyResult(SearchResult keyResult) {
		this.keyResult = keyResult;
	}
	public List<SearchResult> getAllResults() {
		return allResults;
	}
	public void setAllResults(List<SearchResult> allResults) {
		this.allResults = allResults;
	}
	public String getId() {
		return id;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
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
	public Date getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}
	public List<SearchResult> getClickedResults() {
		return clickedResults;
	}
	public void setClickedResults(List<SearchResult> clickedResults) {
		this.clickedResults = clickedResults;
	}
	
	public List<SearchResult> getPageResults() {
		return pageResults;
	}
	public void setPageResults(List<SearchResult> pageResults) {
		this.pageResults = pageResults;
	}
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "Search [id=" + id + ", searchContent=" + searchContent
				+ ", keyResult=" + keyResult + ", engineName=" + engineName
				+ ", searchTime=" + searchTime + ", clickedResults="
				+ clickedResults + ", allResults=" + allResults + "]";
	}
	
}
