package fengcone.mainpage.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fengcone.mainpage.pojo.search.Search;
/**
 * Redis 缓存核心服务类
 * @author fengcone
 *
 */
@Service
public class RedisService {
	@Autowired
	private JedisPool jedisPool;
	private ObjectMapper mapper = new ObjectMapper();
	public void set(String key,String value,Integer db){
		Jedis jedis = jedisPool.getResource();
		jedis.select(db);
		jedis.set(key, value);
		jedis.close();
	}
	public void set(String key,String value){
		Jedis jedis = jedisPool.getResource();
		jedis.select(0);
		jedis.set(key, value);
		//Close这个方法就是 把这个返回到连接池，原来讲 数据库连接池的时候讲过！
		jedis.close();
	}
	/**
	 * 设置一个可以自动销毁的键值对 
	 * @param key
	 * @param value
	 * @param db 选择的数据库
	 * @param destoryMin 多少分钟后销毁
	 */
	public void set(String key,String value,Integer db,Integer destoryMin){
		Jedis jedis = jedisPool.getResource();
		jedis.select(db);
		//NX 是表示这个键在Redis中不存在，XX 表示这个键在Redis中存在
		//第三个参数 EX 表示时间单位是秒 PX 表示是毫秒
		//我这里就默认 在Redis中不存在了，
		jedis.set(key, value, "NX", "EX", destoryMin*60);
		jedis.close();
	}
	public String get(String key,Integer db){
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.select(db);
			String value = jedis.get(key);
			return value;
		} finally{
			jedis.close();
		}
	}
	public String get(String key){
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.select(0);
			String value = jedis.get(key);
			if ("".equals(value)||value == null) {
				for (int i = 1; i < 15; i++) {
					jedis.select(i);
					value = jedis.get(key);
					if (value!=null||!"".equals(value)) {
						break;
					}
				}
			}
			return value;
		} finally{
			jedis.close();
		}
	}
	public Long del(String key,Integer db){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.select(db);
			return jedis.del(key);
		}finally{
			jedis.close();
		}
	}
	public Long del(String key){
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.select(0);
			String value = jedis.get(key);
			if (value==null||"".endsWith(value)) {
				for (int i = 1; i < 15; i++) {
					jedis.select(i);
					value = jedis.get(key);
					if (value!=null) {
						break;
					}
				}
				return jedis.del(key);
			}
			return jedis.del(key);
		} finally{
			jedis.close();
		}
	}
	/**
	 * 得到指定库的所有的Keys
	 * @param db Redis 的库的名称
	 * @return 所有由key 组成的Set集合
	 */
	public Set<String>	getKeys(Integer db){
		Jedis jedis = jedisPool.getResource();
		jedis.select(db);
		return jedis.keys("*");
	}
	
	public Boolean saveSearch(Search search){
		if (search==null||search.getId()==null) {
			return false;
		}
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(search.getId(), mapper.writeValueAsString(search));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public Search getSearch(String searchId){
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(searchId);
		if (json==null) {
			return null;
		}
		Search search;
		try {
			search = mapper.readValue(json, Search.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return search;
	}
}
