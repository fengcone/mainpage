package fengcone.mainpage.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import fengcone.mainpage.httpclient.HttpClientService;
import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.pojo.search.SearchResult;
import fengcone.mainpage.service.RedisService;

/**
 * 搜索引擎核心抽象类 这些子类都设置成多例的？ 要不要这么干呢？
 * 
 * @author fengcone
 * 
 */
@PropertySource(value = "classpath:/properties/search.properties")
public abstract class SearchEngine {

	@Autowired
	Environment env;
	@Autowired
	RedisService redisService;

	// 由Spring 注入的HttpClient
	@Autowired
	HttpClientService httpClientService;

	ObjectMapper mapper = new ObjectMapper();
	// 小小疑问哦？，这个是定义的SearchEngine 的抽象父类，之类可以用吗？
	private static final Logger logger = LoggerFactory
			.getLogger(SearchEngine.class);

	// 搜索请求的Url
	// 这些东西其实不是经常变，可在配置文件中读取
	protected String requestUrl;

	// 各个搜索引擎的请求的参数的name 比如百度是wd
	protected String requestName;

	// 搜索引擎的名字 这个得与在配置文件中定义的搜索引擎的名字相对应
	protected String engineName;

	/**
	 * 需要子类去实现的方法
	 * 
	 * @param searchContent
	 *            搜索的内容
	 * @param search
	 *            这个是在这里初始化的Search 对象
	 * @return 包装好的搜索结果集合的搜索对象
	 */
	public abstract List<SearchResult> getSearchResults(String html);

	// 此方法设计有两个意图： 一从缓存中拿取 带有页数的Search对象 、二、将Search对象存入缓存中
	public abstract Search getSearchByPage(Search search, Integer page);

	/**
	 * 搜索的主方法 第一页的搜索 ，因为第二页搜索的时候 在Redis中已经有了对象，所以另外写一个方法
	 * 
	 * @param searchContent
	 * @return 搜索结果对象
	 */
	public Search search(String searchContent) {
		if (searchContent == null || "".equals(searchContent)) {
			return null;
		}
		String idKey = engineName + searchContent;
		// 防止连续请求，在Redis中缓存大量的网页
		String searchId = redisService.get(idKey, 1);
		if (searchId == null || "".equals(searchId)) {
			searchId = UUID.randomUUID().toString();
			// 将这个存入到Redis 中，并设置30分钟自动销毁
			redisService.set(idKey, searchId, 1, 30);
		} else {
			Search search0 = redisService.getSearch(searchId);
			if (search0 != null) {
				return search0;
			}
		}
		// 第一步创建 Search 对象 的Id
		Search search = new Search(searchId);

		// 第二部设置搜索的内容
		search.setSearchContent(searchContent);

		// 第三步设置 搜索时间
		Date date = new Date();
		search.setSearchTime(date);

		// 第四步 设置搜索引擎的名字
		if (engineName != null) {
			search.setEngineName(engineName);
		}

		// 第五步得到搜索结果集合
		String fullUrl = this.getFullUrl(searchContent);
		// Id key 传进去的时候如果请求错误，就删除这个Idkey
		// 第一次使用这个 getBackHtml() 方法 XXXXXXXXXXXXXXXXXX
//		String backHtml = this.getBackHtml(fullUrl);
		String backHtml = this.getBackHtml();

		List<SearchResult> results = this.getSearchResults(backHtml);
		// 对这些结果集合设置一些公有的信息 标记这些结果是第一页的
		this.setPublicInfo(results, search, 1);
		// 设置当前页的Results 这个东西准备解耦到 Service层
		// search.setPageResults(results);
		search.setAllResults(results);
		// 标记Search 对象中有第一页的结果第一页
		search.setPage("1,");
		return search;
	}
	/**
	 * 
	 * @param results 代表的是搜索结果集合
	 * @param search 代表有Search公共信息的Search对象
	 * @param page  代表设置当前结果集合是第几页的
	 */
	public void setPublicInfo(List<SearchResult> results, Search search,
			Integer page) {
		if (results != null) {
			for (SearchResult searchResult : results) {
				searchResult.setId(UUID.randomUUID().toString());
				searchResult.setSearchContent(search.getSearchContent());
				searchResult.setEngineName(search.getEngineName());
				searchResult.setSearchTime(search.getSearchTime());
				searchResult.setSearchId(search.getId());
				searchResult.setPage(page);
				searchResult.setClicked(false);
			}
		}
	}

	/**
	 * 
	 * @param fullUrl
	 *            请求的完整路径
	 * @return backHtml 请求后得到的Html 网页
	 */
	public String getBackHtml(String fullUrl) {
		String backHtml = null;
		try {
			backHtml = httpClientService.doGet(fullUrl);
			return backHtml;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求错误" + e.getMessage());
			// 应该再重新发送一次请求
			// 请求错误的话应该清除在Redis Idkey 的缓存
			// redisService.del(key)
			// return getBackHtml(fullUrl);
			return null;
		}
	}

	// 这个是设置第一页的SearchUrl 的方法 其他页不能复用了 忧伤
	public String getFullUrl(String searchContent) {
		// 这个还有问题 ，我要翻页的时候可能加参数
		return requestUrl + "?" + requestName + "=" + searchContent;
	}

	public String getEngineName() {
		return engineName;
	}
	// 临时方法 会删除掉的
	public String getBackHtml() {
		String backHtml = null;
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"G://苹果_好搜.html")));
			while ((backHtml = reader.readLine()) != null) {
				buffer.append(backHtml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		backHtml = buffer.toString();
		return backHtml;
	}
}
