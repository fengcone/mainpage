package fengcone.mainpage.controller.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import fengcone.mainpage.pojo.search.Search;
import fengcone.mainpage.service.search.SearchService;

/**
 * 搜索业务的主Controller
 * 
 * @author fengcone
 * 
 */
@Controller
@RequestMapping("search")
public class SearchController {

	@Autowired
	SearchService searchService;
	ObjectMapper mapper = new ObjectMapper();

	// 第一次过来的时候的方法
	@RequestMapping("{engineName}/{searchContent}")
	public ModelAndView search(
			@PathVariable("searchContent") String searchContent,
			@PathVariable("engineName") String engineName) throws Exception {
		ModelAndView mv = new ModelAndView("searchResult");
		searchContent = new String(searchContent.getBytes("iso8859-1"), "utf-8");
		Search search = searchService.search(searchContent, engineName);
		mv.addObject("result", search);
		return mv;
	}

	@RequestMapping("page")
	@ResponseBody
	public Search page(String searchId,String engineName, String page)
			throws Exception {
		// ajax 请求的路径 http://www.fengcone.cn/s/search/page
		// 参数是是Post 请求过来的 匹配 这里的参数名字
		// 这一次请求应该是至少是第二页 ，上面的是第一页，
		// 返回Json 格式数据 ,Spring MVC 会自动转换成为Json的
		// 假设第二页过来的请求也是ajax请求 返回的结果是一个完整的Search对象，
		// 只不过里面的AllResult 被替换成了页的数据
		// 要用的话完全可以转换成为Json 再返回的
		return searchService.page(searchId,engineName,Integer.valueOf(page));
	}

}
