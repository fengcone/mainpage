package fengcone.mainpage.service.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fengcone.mainpage.engine.DaemonEngine;
import fengcone.mainpage.engine.SearchEngine;
import fengcone.mainpage.engine.SearchEngineFactory;
import fengcone.mainpage.mapper.SearchMapper;
import fengcone.mainpage.mapper.SearchResultMapper;
import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.pojo.search.SearchResult;
import fengcone.mainpage.service.RedisService;



/**
 * 搜索业务核心service
 * 
 * @author fengcone
 * 
 */
@Service
public class SearchService {
	@Autowired
	private SearchEngineFactory engineFactory;
	@Autowired
	private SearchMapper searchMapper;
	@Autowired
	private SearchResultMapper srMapper;
	@Autowired
	private DaemonEngine daemonEngine;
	// 第一次搜索后返回的结果
	@SuppressWarnings("finally")
	// 这个是个什么鬼？？
	public Search search(String searchContent, String engineName) {
		SearchEngine engine = engineFactory.getEngine(engineName);
		if ("".equals(engine) || engine == null) {
			throw new RuntimeException("搜索引擎错误");
		}
		Search search = engine.search(searchContent);
		search = this.setPresentResults(search, 1);
		// 跑一个后台程序
		try {
			// 在Redis 中缓存第二页的信息
			new RedisDaemon(engine, search, 2).start();
			// 后台缓存其他引擎的数据
			new OtherSearchDaemon(engineName, searchContent).start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("那个对象没有保存成功");
		} finally {
			return search;
		}
	}

	// 分页后得到的结果
	public Search page(String searchId, String engineName,Integer page) {
		// 第一步：尝试从缓存中拿取这个Search 对象
		// 第二步：如果有，判断有没有这一页的数据 ，有就直接返回，没有再发送一次请求

		// POJO 改造 在每个Result中标记当前结果的页数
		// pojo 改造 在Search 中添加集合，两个中心集合 一个是当前页的结果集合，一个是 全部的集合
		SearchEngine engine = engineFactory.getEngine(engineName);
		Search search = new Search();
		search.setId(searchId);
		search = engine.getSearchByPage(search, page);
		this.setPresentResults(search, page);
		try {
			new RedisDaemon(engine, search, page+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return search;
	}

	public void saveSearch(Search search) {
		// 保存对象到数据库中
		searchMapper.insert(search);
		srMapper.insertList(search.getClickedResults());
	}

	public Search setPresentResults(Search search, Integer page) {
		List<SearchResult> presentResults = new ArrayList<SearchResult>();
		for (SearchResult searchResult : search.getAllResults()) {
			if (searchResult.getPage() == page) {
				presentResults.add(searchResult);
			}
		}
		search.setPageResults(presentResults);
		return search;
	}
	

	class OtherSearchDaemon extends Thread {
		private String engineName;
		private String searchContent;

		public OtherSearchDaemon(String engineName, String searchContent) {
			this.engineName = engineName;
			this.searchContent = searchContent;
		}

		@Override
		public void run() {
			daemonEngine.daemonSearch(engineName, searchContent);
		}
	}

	class RedisDaemon extends Thread {
		private Search search;
		private SearchEngine engine;
		// 想要缓存第几页的数据
		private Integer page;

		public RedisDaemon(SearchEngine engine, Search search, Integer page) {
			this.search = search;
			this.engine = engine;
			this.page = page;
		}

		@Override
		public void run() {
			try {
				// 缓存 第N页的数据  这里是一个方法两用，这个方法既可以缓存指定页的数据，也可以从指定页中拿取
				engine.getSearchByPage(search, page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * public void testQuery(String id){ Search search =
	 * searchMapper.querySearchById(id); System.out.println(search); }
	 */

}
