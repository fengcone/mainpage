package fengcone.mainpage.mapper;

import java.util.List;

import fengcone.mainpage.pojo.search.SearchResult;
/**
 * 搜索结果对象映射接口
 * @author fengcone
 *
 */
public interface SearchResultMapper {
	// 定义两个重名的方法会不会有问题呢？
	public void insert(SearchResult searchResult);
	public void insertList(List<SearchResult> results);
}
