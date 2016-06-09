package fengcone.mainpage.engine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.pojo.search.SearchResult;
import fengcone.mainpage.util.TimeUtil;

/**
 * 好搜搜索解析类
 * 
 * @author fengcone
 * 
 */

@Component
public class HaosouEngine extends SearchEngine {

	@PostConstruct
	public void preHandle() {
		super.engineName = "haosou";
		requestUrl = env.getProperty(engineName + "." + "requestUrl");
		requestName = env.getProperty(engineName + "." + "requestName");
	}

	@Override
	public List<SearchResult> getSearchResults(String html) {
		if (html == null || "".equals(html)) {
			return null;
		}
		Document document = Jsoup.parse(html);
		// 得到搜索结果的集合
		Elements elements = null;
		try {
			elements = document.getElementById("m-result").children();
		} catch (Exception e) {
			return null;
		}
		List<SearchResult> results = new ArrayList<SearchResult>();
		for (Element result : elements) {
			try {
				SearchResult searchResult = new SearchResult();

				searchResult.setTitle(result.getElementsByTag("h3").first()
						.getElementsByTag("a").text());

				searchResult.setUrl(result.getElementsByTag("h3").first()
						.getElementsByTag("a").attr("href"));
				try {
					searchResult.setContent(result.select("h3 ~ div").first()
							.text());
				} catch (Exception e) {
					searchResult.setContent(result.select("h3 ~ p").first()
							.text());
				}

				// 设置时间很可能出错
				try {
					searchResult.setImageUrl(result.select("div div a img")
							.first().attr("src"));
				} catch (Exception e) {
				}
				try {
					String time = result.select("div div span").first()
							.getElementsByClass("gray").text();
					time = time.substring(0, time.length() - 3);
					searchResult.setSiteTime(TimeUtil.parseDate(time,
							engineName));
				} catch (Exception e) {
				}
				results.add(searchResult);
			} catch (Exception e) {
			}
		}
		return results;
	}

	/**
	 * 这里面的 方法参数上Search 对象 第一种是：从 后台（已经在页面返回了，准备向Redis中存入 第二页数据） 的一种 第二种是：
	 * 前端的ajax 过来，只想取Json 数据，那个时候的Search对象只 包含一个ID 其他都是空的
	 */
	@Override
	public Search getSearchByPage(Search search, Integer page) {
		if (search.getId()==null) {
			return null;
		}
		Search search0 = redisService.getSearch(search.getId());
		// 如果search0 ==null 那么说明这个是第一种情况，需要向 这个带过来 有内容的Search对象 中存入第二页的数据
		if (search0 == null) {
			String fullUrl = requestUrl + "?" + requestName + "="
					+ search.getSearchContent() + "&"
					+ env.getProperty(engineName + "." + "page") + "=" + page;
			String backHtml = super.getBackHtml(fullUrl);
//			String backHtml = super.getBackHtml();

			List<SearchResult> pageResults = this.getSearchResults(backHtml);
			// 添加第二页的结果到这个Search对象
			search.getAllResults().addAll(pageResults);
			search.setPage(search.getPage() + page + ",");
			super.setPublicInfo(pageResults, search, page);
			System.out.println(redisService.saveSearch(search));
			return search;
		} else {
			// 这里的else block 的情况是 在Redis中根据前台提供过来的Id 取到了Search对象
			// 那么只需要判断搜索引擎的名字和页数就能知道Redis中是否已经更新了这个对象
			// 如果有返回对象，如果没有，就需要网络现场抓取
			if (engineName.equals(search0.getEngineName())
					&& search0.getPage().contains(page + ",")) {
				return search0;
			} else {// 现场抓取
				String fullUrl = requestUrl + "?" + requestName + "="
						+ search.getSearchContent() + "&"
						+ env.getProperty(engineName + "." + "page") + "="
						+ page;
//				String backHtml = super.getBackHtml(fullUrl);
				String backHtml = super.getBackHtml();
				List<SearchResult> pageResults = this
						.getSearchResults(backHtml);
				search0.getAllResults().addAll(pageResults);
				search0.setPage(search.getPage() + page + ",");
				super.setPublicInfo(pageResults, search0, page);
				System.out.println(redisService.saveSearch(search0));
				return search0;
			}
		}
	}
}
