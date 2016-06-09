package fengcone.mainpage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 对展现在搜索结果页面上的时间内容进行翻译，翻译层普通的java Date 类
 * @author fengcone
 *
 */
public class TimeUtil {
	public static Date parseDate(String time, String engineName) throws Exception{
		if (time==null||"".equals(time)) {
			return null;
		}
		if ("bing".equals(engineName)) {
			return bingParse(time);
		}
		if ("baidu".equals(engineName)||"haosou".equals(engineName)) {
			return parse(time);
		}
		if ("google".equals(engineName)) {
			return googleParse(time);
		}
		return null;
	}
	public static Date parse(String time){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}
	public static Date googleParse(String time){
		time = time.trim();
		time = time.substring(0, time.length()-1);
		Date date = parse(time);
		return date;
		 
	}
	public static Date bingParse(String time) {
		// 今天
		// 2 天前
		// 3 天前
		// 2015-8-28
		SimpleDateFormat bingSdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (time.contains("-")) {
				return bingSdf.parse(time);
			}
			if (time.contains("今天")) {
				return new Date((new Date().getTime() - (new Date().getTime())
						% (1000 * 60 * 60 * 24L)));
			}
			if (time.contains("天前")) {
				int index = time.indexOf("天前") - 2;
				int day = Integer.valueOf(time.substring(index, index + 1));
				return new Date((new Date().getTime() - (new Date().getTime())
						% (1000 * 60 * 60 * 24L))
						- (day * (1000 * 60 * 60 * 24L)));
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
