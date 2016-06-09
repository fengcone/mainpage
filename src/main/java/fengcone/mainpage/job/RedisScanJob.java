package fengcone.mainpage.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.pojo.search.SearchResult;
import fengcone.mainpage.service.RedisService;
import fengcone.mainpage.service.search.SearchService;
/**
 * 定时扫描Redis缓存数据，将合适的数据持久化到数据库中
 * @author fengcone
 *
 */
public class RedisScanJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		ApplicationContext acontext = (ApplicationContext) context
				.getJobDetail().getJobDataMap().get("applicationContext");
		//要是有Spring注入多好
		RedisService redisService = acontext.getBean(RedisService.class);
		SearchService searchService = acontext.getBean(SearchService.class);
		Set<String> searchIDs = redisService.getKeys(0);
		ObjectMapper mapper = new ObjectMapper();
		for (String id : searchIDs) {
			String json = redisService.get(id);
			try {
				Date time= new Date();
				Search search = mapper.readValue(json, Search.class);
				int minute = (int) (((time.getTime() - search.getSearchTime().getTime())/1000)/60);
				if (minute<30) {
//					continue;
				}
				List<SearchResult> results = search.getAllResults();
				List<SearchResult> shouldRemove = new ArrayList<SearchResult>();
				for (SearchResult result : results) {
					if (result.getClicked()==false||result.getClicked()==null) {
						shouldRemove.add(result);
					}
				}
//				results.removeAll(shouldRemove);
				search.setClickedResults(results);
				System.out.println("这里的代码已经执行过一遍了，Search 对象中应该没有任何没有点击的结果对象");
				searchService.saveSearch(search);
				System.out.println("保存了Search对象到数据库中");
			} catch (Exception e) {
				//如果转换出错，说明在库中的json 不是我想要的Search 对象 删除它！
				e.printStackTrace();
				// 刚才报错了 全给我删 了
//				redisService.del(id);
			} 
		}
	}
}
