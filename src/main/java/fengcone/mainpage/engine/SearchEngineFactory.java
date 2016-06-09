package fengcone.mainpage.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 搜索结果分析引擎工厂
 * @author fengcone
 *
 */
@Component
public class SearchEngineFactory {
	@Autowired
	private BaiduEngine baiduEngine;
	@Autowired
	private HaosouEngine haosouEngine;
	@Autowired
	private BingEngine bingEngine;
	@Autowired
	private GoogleEngine googleEngine;
	
	private String allName = "baidu,haosou,bing,google";
	
	public  SearchEngine getEngine(String engineName){
		if ("baidu".equals(engineName)) {
			return baiduEngine; 
		}else if ("haosou".equals(engineName)) {
			return haosouEngine;
		}else if("bing".equals(engineName)){
			return bingEngine;
		}else if ("google".equals(engineName)) {
			return googleEngine;
		}
		return null;
	}
	public List<SearchEngine> getOtherEngine(String engineName){
		List<SearchEngine> list = new ArrayList<SearchEngine>();
		for(String name : allName.split(",")){
			if (!name.equals(engineName)) {
				list.add(getEngine(name));
			}
		}
		list.remove(baiduEngine);
		list.remove(googleEngine);
		return list;
	}
}
