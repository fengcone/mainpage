package fengcone.mainpage.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

@Component
public class GoogleEngine extends SearchEngine {
	@PostConstruct
	public void preHandle() {
		this.engineName = "google";
		requestUrl = env.getProperty(engineName + "." + "requestUrl");
		requestName = env.getProperty(engineName + "." + "requestName");
	}

	@Override
	public List<SearchResult> getSearchResults(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.body().getElementsByClass("srg").get(0)
				.children();
		List<SearchResult> results = new ArrayList<SearchResult>();

		for (Element element : elements) {
			SearchResult result = new SearchResult();
			result.setTitle(element.getElementsByTag("h3").first()
					.getElementsByTag("a").text());
			result.setContent(element.getElementsByClass("st").first().text());
			result.setUrl(element.getElementsByTag("h3").first()
					.getElementsByTag("a").attr("href"));
			try {
				result.setSiteTime(TimeUtil.parseDate(
						element.getElementsByClass("st").first()
								.getElementsByClass("f").text(), "google"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			results.add(result);
		}
		return results;
	}

	@Override
	public Search getSearchByPage(Search search, Integer page) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"G:\\这个是很好的 - Google 搜索.html")));
		String html = "";
		String temp = "";
		while ((temp = reader.readLine()) != null) {
			html = html + temp;
		}

		List<SearchResult> results = new GoogleEngine().getSearchResults(html);
		System.out.println(results);

	}

}
