package fengcone.mainpage.controller.index;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fengcone.mainpage.pojo.news.News;
/**
 * 首页Controller
 * @author fengcone
 *
 */

@Controller
@PropertySource(value="classpath:properties/search.properties")
public class IndexController {
	
	@Autowired
	private Environment env;

	@RequestMapping("index")
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView("index");
		News news = new News();
		news.setContent("百度发言人今天声称百度将投资200亿元人民币到百度糯米");
		news.setTitle("百度宣布将投资两百亿元人民币到百度糯米");
		news.setTime(new Date());
		news.setImgurl("http://localhost:8056/360.png");
		
		News news2 = new News();
		List<News> newsList = new ArrayList<News>();
		newsList.add(news2);
		newsList.add(news);
		mv.addObject("newsList", newsList);
		mv.addObject("searchUrl",env.getProperty("searchUrl"));
		return mv;
	}
}
