package com.lvmama.lvf.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedUtil {
	private static Object LOCK = new Object();
	private static Object SESSION_LOCK = new Object();
	private final static  Log log = LogFactory.getLog(MemcachedUtil.class);
	private Properties properties;
	private MemCachedClient memCachedClient;
	private MemCachedClient sessionMemCachedClient;
	public final static int ONE_HOUR=3600;
	public final static int TWO_HOUR=7200;
	public final static String memcacheMapKey="MEMACACHE_MAP_KEY";
	
	private static MemcachedUtil instance;
	
	private String getProperty(String key){
		return CustomizedPropertyPlaceholderConfigurer.getContextProperty(key);
	}
	
	private void init() {
		try {
			
			//数据缓存服务器，“,”表示配置多个memcached服务
			String[] servers = getProperty("cache.server").replaceAll(" ", "").split(",");
			SockIOPool pool = SockIOPool.getInstance("dataServer");
			pool.setServers(servers);
			pool.setFailover(true);
			pool.setInitConn(10);
			pool.setMinConn(5);
			pool.setMaxConn(50);
			pool.setMaintSleep(30);
			pool.setNagle(false);
			pool.setSocketTO(30000);
			pool.setBufferSize(1024*1024*10);
			pool.setAliveCheck(true);
			pool.initialize(); /* 建立MemcachedClient实例 */
			memCachedClient = new MemCachedClient("dataServer");
		}catch (Exception ex) {
			log.error(ex.getMessage(),ex);
		}
	}
	
	private void initSessionMemCached() {
		try {
			
			//Session缓存服务器，“,”表示配置多个memcached服务
			String[] sessionServers = getProperty("session.cache.server").replaceAll(" ", "").split(",");
			SockIOPool sessionPool = SockIOPool.getInstance("sessionServer");
			sessionPool.setServers(sessionServers);
			sessionPool.setFailover(true);
			sessionPool.setInitConn(10);
			sessionPool.setMinConn(5);
			sessionPool.setMaxConn(50);
			sessionPool.setMaintSleep(30);
			sessionPool.setNagle(false);
			sessionPool.setSocketTO(30000);
			sessionPool.setBufferSize(1024*1024*10);
			sessionPool.setAliveCheck(true);
			sessionPool.initialize(); /* 建立MemcachedClient实例 */
			sessionMemCachedClient = new MemCachedClient("sessionServer");
		}catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private MemcachedUtil(){
		init();
	}
	
	private boolean isCacheEnabled() {
		boolean useCache = false;
		try {
			useCache = Boolean.valueOf(getProperty("cache.enable"));
		} catch (Exception e) {
			useCache = false;
			log.error("Please enable memcached");
		}
		return useCache;
	}
	
	/**
	 * 改用嵌套类静态实始化
	 * @return
	 */
	public static MemcachedUtil getInstance() {
		if(instance == null){
			synchronized(LOCK) {
				if (instance==null) {
					instance=new MemcachedUtil();
				}
			}
		}
		return instance;
	}

	private MemCachedClient getMemCachedClient(boolean isForSession) {
		if(isForSession){
			if(sessionMemCachedClient == null){
				synchronized(SESSION_LOCK) {
					if (sessionMemCachedClient==null) {
						initSessionMemCached();
					}
				}
			}
			return sessionMemCachedClient;
		}
		else
			return memCachedClient;
	}
	
	public boolean replace(String key, int seconds, Object obj) {
		return replace(key, seconds, obj,false);
	}
	/**
	 * 替换
	 * @param key
	 * @param seconds 过期秒数
	 * @param obj
	 * @return
	 */
	public boolean replace(String key, int seconds, Object obj,boolean isForSession) {
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				Date  expDate = getDateAfter(seconds);
				boolean result = getMemCachedClient(isForSession).replace(key, obj, expDate);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + ", exp:" + expDate  + ", result:" + result);
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		return false;
	}

	/**
	 * 放
	 * @param key
	 * @param seconds 过期秒数
	 * @param obj
	 * @return
	 */
	public boolean set(String key, int seconds, Object obj,boolean isForSession) {
		return set(key, getDateAfter(seconds), obj,isForSession);
	}
	
	public boolean set(String key, int seconds, Object obj) {
		return set(key, getDateAfter(seconds), obj,false);
	}

	
	/**
	 * 将KEY保存到memcache中
	 * 
	 * @param key
	 * @param exp
	 * @param obj
	 * @return
	 */
	public boolean set(String key,Date exp,Object obj,boolean isForSession){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				boolean result = getMemCachedClient(isForSession).set(key, obj, exp);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + ", exp:" + exp + ", result:" + result);
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		return false;
	}
	
	public boolean set(String key,Date exp,Object obj){
		return set(key,exp,obj,false);
	}
	
	/**
	 * @deprecated 此方法严重影响流量，应该被废弃 <a>
	 * @author Brian
	 * 
	 * 保存key和描述信息；
	 * 
	 * @param key
	 * @param second （单位：秒）
	 * @param obj
	 * @return
	 */
	public boolean setWithDis(String key,int seconds,Object obj,String discript){
		if(StringUtils.isEmpty(key)){
			return false;
		}
		if(obj==null){
			return true;
		}
		try{
			if (isCacheEnabled()) {
				Date expDate = getDateAfter(seconds);
				//this.putKeyDisMap(key,discript);
				boolean result = memCachedClient.set(key, obj, expDate);
				if(log.isDebugEnabled()) {
					log.debug("SET A OBJECT: KEY:" + key + ", OBJ:" + obj + ", expDate:" + expDate) ;
				}
				return result;
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		return false;
	}
	
	/**
	 * 把相应的Key值和描述存到此方法中；
	 * 
	 * @param key
	 * @param discript
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean putKeyDisMap(String key,String discript) {
		Map<String,String> memMap ;
		Object obj = memCachedClient.get(memcacheMapKey);
		if(obj == null) {
			memMap = new HashMap<String, String>();
		} else {
			memMap = (HashMap<String, String>) obj;
		}
		memMap.put(key, discript);
		memCachedClient.set(memcacheMapKey,memMap,getDateAfter(60*60*48));
		
		return true;
	}
	
	/**
	 * 放
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean set(String key, Object obj) {
		return set(key,ONE_HOUR,obj);
	}
	
	/**
	 * 取
	 * @param key
	 * @return
	 */
	public Object get(String key,boolean isForSession) {
		try{
			if (isCacheEnabled()) {
				Object obj = getMemCachedClient(isForSession).get(key);
				if(log.isDebugEnabled()) {
					log.debug("GET A OBJECT: KEY:" + key + " OBJ:" + obj) ;
				}
				return obj;
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		return null;
	}
	
	/**
	 * 变为泛型,避免处处都需要强制转换导致警告
	 * <功能详细描述>
	 * @param key
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
    public <T> T  get(String key) {
		try
        {
            return (T)get(key,false);
        }
        catch (Exception e)//转型出错,返回空
        {
            return null;
        }
	}
	
	/**
	 * 取
	 * @param key
	 * @return
	 */
	public boolean remove(String key,boolean isForSession) {
		if(StringUtils.isEmpty(key)){
			return false;
		}
		try{
			if (isCacheEnabled()) {
				if(log.isDebugEnabled()) {
					log.debug("delete memcached key: " + key);
				}
				return getMemCachedClient(isForSession).delete(key);
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		return true;
	}
	public boolean remove(String key) {
		return remove(key,false);
	}
	
    /**
     * 获得当前开始活参数秒的时间日期
    * @Title: getDateAfter
    * @Description:
    * @param
    * @return Date    返回类型
    * @throws
     */
    public static Date getDateAfter(int second) {
        Calendar cal = Calendar.getInstance();
 		cal.add(Calendar.SECOND, second);
 		return cal.getTime();
 	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Map<String, String> map = (Map<String, String>) MemcachedUtil.getInstance().get("MEM_TEST_KEY_LL_6");
		if(map == null){
			map = new HashMap<String, String>();
		}

		for(int i=0;i<1;i++) {
			Random r =  new Random(System.currentTimeMillis());
			String key = "KEY_" + r.nextLong();
//			//System.out.println("keys: " +key);

			byte[] bt = new byte[0];
			String value= new String(bt,"UTF-8");
			for(int j=0;j<10000;j++) {
				value= value + "qweqweqweqweqweqwewequyrqwieurpasjdflkasdfasdrwqioeurpqwerqweqwertyuiopqwertyuiopqwertyuiopqwertqqqq";
			}
			//System.out.println(key + " : " + MemcachedUtil.getInstance().get(key));
			map.put(key, value);
		}
		
		boolean a =MemcachedUtil.getInstance().set("MEM_TEST_KEY_LL_6", map);
		
		boolean b = MemcachedUtil.getInstance().set("MEM_TEST_KEY_LL_2", "2");

		
		//System.out.println("keys: " + map.size());
		
		System.out.println(a + ", " + b);

	}
}
