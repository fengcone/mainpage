package fengcone.mainpage.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.service.RedisService;
/**
 * 当一次搜索完成后(还没完成) 后台程序去搜索其他的搜索引擎，并将结果保存在Redis 缓存中
 * @author fengcone
 *
 */
@Component
public class DaemonEngine {
	@Autowired
	private RedisService redisService;
	@Autowired
	private SearchEngineFactory engineFactory;
	//注意： 这里可能会有并发的问题
	public void daemonSearch(String engineName, String searchContent) {
		List<SearchEngine> list = engineFactory.getOtherEngine(engineName);
		for (SearchEngine searchEngine : list) {
			new DaemonSearch(searchEngine, searchContent).start();
		}
	}

	class DaemonSearch extends Thread {
		private SearchEngine searchEngine;
		private String searchContent;
		public DaemonSearch(SearchEngine searchEngine,String searchContent) {
			this.searchEngine = searchEngine;
			this.searchContent = searchContent;
		}
		@Override
		public void run() {
			try {
				String idKey = searchEngine.getEngineName() + searchContent;
				String searchId = redisService.get(idKey,1);
				if (searchId == null || "".equals(searchId)) {
					Search search = searchEngine.search(searchContent);
					redisService.saveSearch(search);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
