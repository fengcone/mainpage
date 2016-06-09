import java.util.Set;

import redis.clients.jedis.Jedis;

public class test {
	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		Set<String> keys = jedis.keys("*");
		for (String key : keys) {
			String content = jedis.get(key);
			System.out.println(key);
			System.out.println(content);
		}
		
		jedis.select(1);
		keys = jedis.keys("*");
		for (String key : keys) {
			String content = jedis.get(key);
			System.out.println(key);
			System.out.println(content);
		}
	}
}
