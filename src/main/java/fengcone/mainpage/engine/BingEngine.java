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
 * Bing 搜索解析类
 * 
 * @author fengcone
 * 
 */
@Component
public class BingEngine extends SearchEngine {
	@PostConstruct
	public void preHandle() {
		this.engineName = "bing";
		requestUrl = env.getProperty(engineName + "." + "requestUrl");
		requestName = env.getProperty(engineName + "." + "requestName");
	}

	@Override
	public List<SearchResult> getSearchResults(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = null;
		try {
			elements = document.getElementById("b_results").children();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		List<SearchResult> results = new ArrayList<SearchResult>();

		for (Element result : elements) {
			try {
				if (result.className().contains("ad")) {
					continue;
				}

				String title = result.getElementsByTag("h2").first()
						.getElementsByTag("a").text();
				if (title == null || "".equals(title)) {
					continue;
				}
				SearchResult searchResult = new SearchResult();
				searchResult.setTitle(title);
				searchResult.setUrl(result.getElementsByTag("h2").first()
						.getElementsByTag("a").attr("href"));
				try {
					searchResult.setContent(result.getElementsByTag("p")
							.first().text());
				} catch (Exception e) {
					searchResult.setContent(result.text());
				}
				// 设置时间很可能出错
				try {
					searchResult.setImageUrl(result.getElementsByTag("img")
							.first().attr("src"));
				} catch (Exception e) {
				}
				try {
					String time = result.select("cite").first().parent()
							.ownText();
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

	@Override
	public Search getSearchByPage(Search search, Integer page) {
		return null;
	}

	
}
