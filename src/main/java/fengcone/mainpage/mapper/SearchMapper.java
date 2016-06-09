package fengcone.mainpage.mapper;

import fengcone.mainpage.pojo.search.Search;
/**
 * 搜索对象映射接口
 * @author fengcone
 *
 */
public interface SearchMapper {
	public void insert(Search search);
	//这个方法是这样的，查询一个Search 通过的是Search 对象的ID 
	//如果这个ID 在数据库中存在，就返回null ，如果不存在就返回null
	public Search querySearchById(String id);
}
