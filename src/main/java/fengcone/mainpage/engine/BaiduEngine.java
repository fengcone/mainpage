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
 * 百度搜索网页分析类 多线程并发问题
 * 
 * @author fengcone
 * 
 */

@Component
public class BaiduEngine extends SearchEngine {
	/**
	 * 当类加载起来做的预处理
	 */
	@PostConstruct
	public void preHandle() {
		super.engineName = "baidu";
		requestUrl = env.getProperty(engineName + "." + "requestUrl");
		requestName = env.getProperty(engineName + "." + "requestName");
	}
	@Override
	public List<SearchResult> getSearchResults(String html) {

		Document document = Jsoup.parse(html);
		// 第一过来的是时候创建 Search 对象UUID
		Elements elements = null;
		try {
			elements = document.getElementById("content_left").children();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// 可能会有推荐的玩意，不要它
		if (!elements.get(0).hasAttr("id")) {
			elements.remove(0);
		}
		List<SearchResult> results = new ArrayList<SearchResult>();
		int index = 0;
		for (Element result : elements) {
			SearchResult searchResult = new SearchResult();
			try {
				if (index++ == 11) {
					break;
				}
				searchResult.setContent(result.getElementsByTag("div").get(1)
						.text());
				searchResult.setUrl(result.getElementsByTag("h3").first()
						.getElementsByTag("a").attr("href"));
				searchResult.setTitle(result.getElementsByTag("h3").first()
						.getElementsByTag("a").text());

				// 设置它的主对象
				// 设置时间很可能出错
				try {
					String time = result.getElementsByClass("c-abstract")
							.first().child(0).text();
					time = time.substring(0, time.length() - 3);
					searchResult.setSiteTime(TimeUtil.parseDate(time,
							engineName));
				} catch (Exception e) {
				}

				try {
					String imageUrl = result.select("h3 ~ div").first()
							.child(0).getElementsByTag("img").attr("src");
					searchResult.setImageUrl(imageUrl);
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
		// TODO Auto-generated method stub
		return null;
	}

}
