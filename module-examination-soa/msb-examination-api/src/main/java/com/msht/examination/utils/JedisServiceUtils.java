package com.msht.examination.utils;
 
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msht.framework.common.utils.FileUtils;
import com.msht.framework.common.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 
 * @author lindaofen
 *
 */
public class JedisServiceUtils {
	
	private static Logger log = LoggerFactory.getLogger(JedisServiceUtils.class);
	
	private static JedisServiceUtils redis = new JedisServiceUtils();
	
	private static JedisPool jedisPool = null;
    //private static ShardedJedisPool shardedJedisPool = null;

	
	/** sesssion库 协调者库 */
	public static final int DEFAULT_DB = 0; //协调库
	public static final int TASK_CONFIG_DB = 1;//采集任务配置库,listpage页面配置
	public static final int DETAIL_PAGE_TASK_DATA_DB = 2;//带采集的详细文章
	public static final int TASK_DATA_ERROR_DB = 3;//存放采集有问题的文章
	public static final int TEMP_DB = 4;//存放采集有问题的文章
	//------------------------------------------------
	public static final int TASK_DATA_OPERATE_DB=9; //任务变更  同web程序一样，两边要相同，如做修改同时修改
	
	/**
	 * 缓存生存时间
	 */
	private final int expire = 60000;

	public static final int EXPIRE_SEC = 1;
	
	public static final int EXPIRE_SEC_10 = EXPIRE_SEC*10;
	
	public static final int EXPIRE_MINUTE = 60;
	
	public static final int EXPIRE_MINUTE_2 = EXPIRE_MINUTE*2;
	
	public static final int EXPIRE_MINUTE_5 = EXPIRE_MINUTE*5;
	
	public static final int EXPIRE_MINUTE_10 = EXPIRE_MINUTE*10;
	
	public static final int EXPIRE_HALF_HOUR = EXPIRE_MINUTE*30;
	
	public static final int EXPIRE_HOUR = EXPIRE_MINUTE*60;
	
	public static final int EXPIRE_DAY = EXPIRE_HOUR*24;
	
	public static final int EXPIRE_DAY_3 = EXPIRE_DAY*3;
	
	public static final int EXPIRE_WEEK = EXPIRE_DAY*7;
	
	public static final int EXPIRE_MONTH = EXPIRE_DAY*30;
	
	
	static {
		try{
			InputStream in  = FileUtils.getResourceAsStream("redis.properties");
			Properties props = new Properties();
			props.load(in);
			init(props);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

 
	
	public static String REDIS_HOST = null;
	public static int REDIS_PORT = 0;
	public static String REDIS_PASSWORD = null;
	public static int REDIS_TIMEOUT = 2000;
	
	public static int MAX_TOTAL = 1024;
	public static int MAX_IDLE = 1024; 
	public static int MIN_IDLE = 5;
	public static long MAX_WAIT_MILLIS = 1000;
	
	public static boolean TEST_ON_BORROW = true;
	public static boolean TEST_ON_RETURN = true;
	
	/**
	 * 构建redis连接池
	 * 
	 * @return JedisPool
	 */
	public static void init(Properties redisProp) {
 
		REDIS_HOST = redisProp.getProperty("redis.host");
		REDIS_PORT = Integer.valueOf(redisProp.getProperty("redis.port"));
		REDIS_PASSWORD =  redisProp.getProperty("redis.password");
		if (StringUtils.isBlank(REDIS_PASSWORD)){
			REDIS_PASSWORD = null;
		}
		REDIS_TIMEOUT = Integer.valueOf(redisProp.getProperty("redis.timeout"));
		
		MAX_TOTAL = Integer.valueOf(redisProp.getProperty("redis.pool.maxTotal"));
		MAX_IDLE = Integer.valueOf(redisProp.getProperty("redis.pool.maxIdle"));
		MIN_IDLE = Integer.valueOf(redisProp.getProperty("redis.pool.minIdle"));
		MAX_WAIT_MILLIS = Integer.valueOf(redisProp.getProperty("redis.pool.maxWait"));
		
		TEST_ON_BORROW = Boolean.valueOf(redisProp.getProperty("redis.pool.testOnBorrow"));
		TEST_ON_RETURN = Boolean.valueOf(redisProp.getProperty("redis.pool.testOnReturn"));
		
		
        if (jedisPool == null) { 
            JedisPoolConfig poolConfig = new JedisPoolConfig();
     
//            redis.pool.maxTotal=1024 
//            		redis.pool.maxIdle=200 
//            		redis.pool.minIdle=5 
//            		redis.pool.maxWaitMillis=1000 
//            		redis.pool.testOnBorrow=true 
//            		redis.pool.testOnReturn=true 
//          redis.pool.maxActive=200  #最大连接数：能够同时建立的“最大链接个数”  
//    		redis.pool.maxIdle=20     #最大空闲数：空闲链接数大于maxIdle时，将进行回收
//    		redis.pool.minIdle=5      #最小空闲数：低于minIdle时，将创建新的链接
//    		redis.pool.maxWait=3000    #最大等待时间：单位ms
//    		redis.pool.testOnBorrow=true   #使用连接时，检测连接是否成功 
//    		redis.pool.testOnReturn=true  #返回连接时，检测连接是否成功 
      
            poolConfig.setMaxTotal(MAX_TOTAL);
            poolConfig.setMaxIdle(MAX_IDLE); 
            poolConfig.setMinIdle(MIN_IDLE);
            poolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
            poolConfig.setTestOnBorrow(TEST_ON_BORROW);
            poolConfig.setTestOnReturn(TEST_ON_RETURN);
            
            jedisPool = new JedisPool(poolConfig,  REDIS_HOST, REDIS_PORT,
            		 REDIS_TIMEOUT, REDIS_PASSWORD);       
        }
	}

	public JedisPool getPool() {
		return jedisPool;
	}

 

	/**
	 * 从jedis连接池中获取获取jedis对象
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		Jedis jedis = jedisPool.getResource();
		jedis.select(DEFAULT_DB);
		
		return jedis;
	}

	/**
	 * 获取JedisUtil实例
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JedisServiceUtils getInstance() {
		return redis;
	}

	/**
	 * 回收jedis
	 * 
	 * @param jedis
	 */
	public static void returnJedis(Jedis jedis) {
	  
		 if (jedis != null) {
			 
			try {
				jedis.close();
			} catch (Exception e) {

			}
 
			jedis = null;
			 
         }
		 
		
	
	}
	
	public static interface RedisExecutor<T>{
	
		public T execute(Jedis jedis); 
		
	}
	
	private <T> T redisOp(RedisExecutor<T> executor,Object ... args){
		Jedis jedis = getJedis();
		try{
			T result = executor.execute(jedis);
			 
			return result;
		}catch(Exception e){
			 log.info(e.getMessage());
		}finally{
			returnJedis(jedis);
		}
		
		
		return null;
	}
 
	/**
	 * 添Lish 左边PUSH操作，添加元素到末尾
	 * @param key List的名称
	 * @param value
	 */
	public static void LPUSH(String key, Object value, int db) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			
			//value为字符串，直接存字符串
			if(value.getClass().equals(String.class)){
				jedis.lpush(key, (String)value);
			}
			
			jedis.lpush(genKey(key), SerializeUtils.serialize(value));
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 添加到List<String>中
	 * @param key List的名称
	 */
	public static List<? extends Object> getFromList(String key, Class valueElemClazz, int db) {
		Jedis jedis = null;
		List<Object> rt = new ArrayList<Object>();
		try {
			jedis = getJedis();
			jedis.select(db);
			
			if(valueElemClazz.equals(String.class)){
				Long count = jedis.llen(key);
				return jedis.lrange(key, 0, count.intValue() - 1);
			}
			
			Long count = jedis.llen(genKey(key));
			List<byte[]> list = jedis.lrange(genKey(key), 0, count.intValue() - 1);
			for(byte[] el : list){
				rt.add( SerializeUtils.deserialize(el) );
			}
			
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
		return rt;
	}

	/**
	 * 设置默认过期时间
	 * 
	 * @author ruan 2013-4-11
	 * @param key
	 */
	public void expire(String key) {
		expire(key, expire);
	}
	/**
	 * 获取hash中的field
	 * 
	 * @param key
	 * @param field
	 */
    public static Long setHashFieldValue(String key, String field,int db,String value) {
    	if (value == null || value.equals("")) {
			log.info("key: " + key + " 对应的value为空");
			return 0L;
		}		
		Jedis jedis = null;
		Long l = 0L;
		
		try {
			jedis = getJedis();
			jedis.select(db);
			
			int n = field.indexOf("_");
			if(n < 0)
				 n = 0;				
			
            String script = "local res=redis.call('HKEYS','"+key+"') local field='"+field.substring(n)+"'"
                    + " for i,v in ipairs(res) do local n = string.sub(v,13) if(n == field) then redis.call('HDEL','"+key+"',v) end end" 
                    + " return redis.call('HSET','"+key+"','"+field+"','"+value+"') ";
            
            //l = jedis.hset(key, field, value);						   
            l = Long.parseLong(jedis.eval(script).toString());
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}  
		return l;
    }
	/**
	 * 获取hash中的field
	 * 
	 * @param key
	 * @param field
	 * @param expire    --过期秒数
	 */
	public static Object getHashField(String key, String field,int db,int expire) {		
		Jedis jedis = null;
		Object obj =null;
		try {
			jedis = getJedis();
			jedis.select(db);
                              
            Date dt = new Date();
            dt.setTime(dt.getTime() - (expire*1000));
                    
            String script = "local res=redis.call('HKEYS','"+key+"') local field='_"+field+"'"
                    + " for i,v in ipairs(res) do local n = string.sub(v,0,12)  if(tonumber(n)< tonumber('"+new SimpleDateFormat("yyyyMMddHHmm").format(dt)+"')) then redis.call('HDEL','"+key+"',v) end "
                    + " n=string.sub(v,13) if (n==field) then field= v end end" 
                    + " return redis.call('HGET','"+key+"',field)";
            
      		obj = jedis.eval(script);

		} catch (JedisException  e) {
			log.error(e.getMessage());
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}					
		}		
		return obj;
	}	
	/**
	 * 自增JSON缓存属性
	 * 
	 * @param key
	 * @param property
	 */
	public static long incrJSON(String key, String property,int db,int value) {		
		Jedis jedis = null;
		long redisValue = 0;
		try {
			jedis = getJedis();
			jedis.select(db);
			
		    String script = "local msg=redis.call('get','"+key+"');local result=0;"
					   + "local msg=string.gsub(msg,'\"(%a+)\":(%d+)\',function(k,v) if(string.find(k, '"+property+"')) then result=v+"+value+"; if(result < 0) then result=0; end return '\"'..k..'\":'..result; end end);"
					   + "redis.call('set','"+key+"',msg);"	
			           + "return result;";
      		Object obj = jedis.eval(script);
      		if(obj != null){
      			redisValue = Long.parseLong(obj.toString());     			
      		} 
		} catch (JedisException  e) {
			log.error(e.getMessage());
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}					
		}
		
		return redisValue;
	}	
	/**
	 * 自增缓存
	 * 
	 * @param key
	 * @param value
	 */
	public static void incr(String key, int value, int db) {		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			long redisValue = jedis.incr(key);
			
            if ((redisValue+99) >= Long.MAX_VALUE  || redisValue < value) {
            	jedis.getSet(key, ""+(value+1));                            	
            }
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param value
	 * @param db
	 */
	public static void set(String key, String value, int db) {
		if (value == null || value.equals("")) {
			log.info("key: " + key + " 对应的value为空");
			return;
		}
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			jedis.set(key, value);
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param seconds 过期时间
	 * @param value
	 */
	public static void set(String key, int seconds, String value, int db, boolean override) {
		if (value == null || value.equals("")) {
			log.info("key: " + key + " 对应的value为空");
			return;
		}
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			if(override)
			   jedis.setex(key, seconds, value);
			else{
			   jedis.setnx(key, value);
			   jedis.expire(key, seconds);
			}
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	public static void set(String key, int seconds, Object obj, int db, boolean override) {
		if (obj == null) {
			log.info("key: " + key + " 对应的value为空");
			return;
		}
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
		    if(override)
			   jedis.setex(genKey(key), seconds, SerializeUtils.serialize(obj));
		    else{
		    	jedis.setnx(genKey(key), SerializeUtils.serialize(obj));
		    	jedis.expire(genKey(key), seconds);
		    }
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	public static void set(String key, Object obj, int db) {
		if (obj == null) {
			log.info("key: " + key + " 对应的value为空");
			return;
		}
		
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			jedis.set(genKey(key), SerializeUtils.serialize(obj));
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @param db
	 */
	public static String get(String key, int db) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			value = jedis.get(key);
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
		return value;

	}

	


	/**
	 * 根据键的前缀模糊匹配查询
	 * @param keyPrefix
	 * @param db
	 * @return
	 */
	public static Set<String> findByPrefix(String keyPrefix, int db){
		Set<String> set = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			set = jedis.keys(keyPrefix);
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
		return set;
	}	

	/**
	 * 根据key删除缓存
	 * 
	 * @param key
	 * @param db
	 */
	public static void delByKey(String key, int db) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			jedis.del(key);			
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}

	}

	/**
	 * 根据前缀删除缓存
	 * 
	 * @param likeKey
	 */
	public static void delByPrefix(String likeKey, int db) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(db);
			Set<String> keys = jedis.keys(likeKey + "*");
			if (keys.size() > 0) {
				String[] delKeys = new String[keys.size()];
				int i = 0;
				for (String delKey : keys) {
					delKeys[i] = delKey;
					i++;
				}
				jedis.del(delKeys);
			}			
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
	}

	/**
	 * 清空所有数据库
	 */
	public static String flushAll() {
		String state = null;
		Jedis jedis = null;
		
		try {
			jedis = getJedis();
			state = jedis.flushAll();
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
		
		if ("OK".equals(state)) {
			log.warn("清空redis所有缓存成功!");
		} else {
			log.warn("清空redis所有缓存失败!");
		}
		return state;
	}

	/**
	 * 清空单个数据库
	 */
	public static String flushDB(int db) {
		
		String state = null;
		Jedis jedis = null;
		
		try {
			jedis = getJedis();
			jedis.select(db);
			state = jedis.flushDB();
		} catch (JedisException  e) {
			log.error(e.getMessage());
			if(jedis != null){
				jedis.close();
			}
		} catch (Exception  e) {
			log.error(e.getMessage());
		} finally {
			if(jedis != null){
				jedis.close();
			}
		}
		
		
		if ("OK".equals(state)) {
			log.warn("清空redis所有缓存成功!");
		} else {
			log.warn("清空redis所有缓存失败!");
		}
		return state;
		
	}
	/**
	 * 
	 * TODO 获取Value Map 的值
	 * @param mapKey
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	private static Object getObject(String key, String mapKey,int db){		 
		 byte[] val = null;
		 Jedis jedis = getJedis();
		 try {		   
			 
		   if(db > 0)
		       jedis.select(db);
		   
		   val = jedis.hget(genKey(key), mapKey.getBytes());
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 if (val != null && val.length > 0)
			 	return SerializeUtils.deserialize(val);
		 return null ;
	}
	
	/**
	 * 
	 * TODO 更新或新增缓存信息
	 * @param map
	 * @exception 
	 * @since  1.0.0
	 */
	public static String setObject(String key,Map<byte[], byte[]> map,int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		  
		   //如果存在则插入覆盖，如果不存在则初始化
		   return jedis.hmset(genKey(key), map);		   
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return null;
	}
	 
	private void setObject(final String key,final Object value){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				byte[] valueBytes = SerializeUtils.serialize(value);
				jedis.set(genKey, valueBytes);
				return null;
			}
		}, key,value);
	}
	
	public void setObject(final String key,final Object value,final int expire){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				byte[] valueBytes = SerializeUtils.serialize(value);
				jedis.set(genKey, valueBytes);
				jedis.expire(genKey, expire);
				return null;
			}
		},"set",key,value,expire);
	}

	public void setObject(final int dbindex,final String key,final Object value,final int expire){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				byte[] valueBytes = SerializeUtils.serialize(value);
				jedis.select(dbindex);
				
				jedis.set(genKey, valueBytes);
				jedis.expire(genKey, expire);
				return null;
			}
		},"set",key,value,expire);
	}
	
	public Set<String> sget(final String set){
		return this.redisOp(new RedisExecutor<Set<String>>(){
			@Override
			public Set<String> execute(Jedis jedis) {
				Set<String> values = jedis.smembers(set);
				if(values!=null){
					return values;
				}
				return Collections.emptySet();
			}
		}, "smembers",set);
	}

 
	
	public Set<String> keys(final String pattern){
		return this.redisOp(new RedisExecutor<Set<String>>(){
			@Override
			public Set<String> execute(Jedis jedis) {
				Set<String> keys = jedis.keys(pattern);
				return keys;
			}
			
		}, pattern);
	}
	
	public List<Object> keysObject(final String pattern){
		return this.redisOp(new RedisExecutor<List<Object>>(){
			@Override
			public List<Object> execute(Jedis jedis) {
				List<Object> listValue = new ArrayList<Object>();
				jedis.select(DEFAULT_DB);
				Set<String> keys = jedis.keys(pattern);
				for (String key : keys) {
					Object obj  = redis.getObject(key);
					listValue.add(obj);
				}

				return listValue;
			}
			
		}, pattern);
	}

	
	/**
	 * Set集合 add操作
	 * @param key
	 * @param member
	 * @param db
	 * @return
	 */
	public static Long sadd(String key, String member, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0){
		       jedis.select(db);
		  }
		   return jedis.sadd(key, member);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return 0l;
	}
	
	public void sadd(final String set,final int expire,final String ...values){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				jedis.sadd(set, values);
				jedis.expire(set, expire);
				return null;
			}
		}, "sadd",set,values);
	}
	
	
	public void sremove(final String set,final int expire,final String ... values){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				jedis.srem(set, values);
				jedis.expire(set, expire);
				return null;
			}
		}, "srem",set,values);
	}
	
	public void sremove(final String set,final String ... values){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				jedis.srem(set, values);
				jedis.expire(set, 0);
				return null;
			}
		}, "srem",set,values);
	}
	
	
	/**
	 * Set集合 members
	 * @param key
	 * @param db
	 * @return
	 */
	public static Set<String> smembers(String key, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		   return jedis.smembers(key);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return new HashSet<String>();
	}
	/**
	 * Set集合 删除操作
	 * @param key
	 * @param db
	 * @param member
	 * @return
	 */
	public static Long srem(String key, int db,String... member){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		  
		   return jedis.srem(key, member);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return 0l;
	}
	/**
	 * Set集合 add操作（字节）
	 * @param key
	 * @param member
	 * @param db
	 * @return
	 */
	public static Long saddByte(String key, String member, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		   //如果存在则插入覆盖，如果不存在则初始化
		   return jedis.sadd(genKey(key), member.getBytes());
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return 0l;
	}
	/**
	 * Lish 右PUSH操作，添加元素到末尾
	 * @param key
	 * @param member
	 * @param db
	 * @return
	 */
	public static Long RPUSH(String key, String member, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		   //如果存在则插入覆盖，如果不存在则初始化
		   return jedis.rpush(key, member);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return 0l;
	}
	/**
	 * 获取List 某个区间(注意此方法为闭区间)
	 * @param key
	 * @param startIndex	0代表第一个元素
	 * @param endIndex		-1代表最后一个元素
	 * @param db
	 * @return
	 */
	public static List<String> LRANGE(String key,long startIndex, long endIndex, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		   //如果存在则插入覆盖，如果不存在则初始化
		   return jedis.lrange(key,startIndex, endIndex);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return new ArrayList<String>();
	}
	/**
	 * List移除数据
	 * @param key
	 * @param count <br>
		    count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 <br>
		    count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。 <br>
		    count = 0 : 移除表中所有与 value 相等的值。 <br>
	 * @param value
	 * @param db
	 * @return
	 */
	public static long LREM(String key,long count , String value, int db){
		Jedis jedis = getJedis();
		 try {
		  if(db > 0)
		       jedis.select(db);
		   //如果存在则插入覆盖，如果不存在则初始化
		   return jedis.lrem(key,count, value);
		 } catch (JedisException e) {
		   log.error(e.getMessage());
		   if (jedis != null) {
		     jedis.close();
		   }
		 } catch (Exception e) {
		   log.error(e.getMessage());
		 } finally {
		   if (jedis != null) {
		     jedis.close();
		   }
		 }
		 return 0l;
	}
	   /**
     * 移除表头数据
     * @param key
     * @param db
     * @return
     */
    public static Object LPOP(String key,int db){
        Jedis jedis = getJedis();
         try {
          if(db > 0)
               jedis.select(db);
          return SerializeUtils.deserialize(jedis.rpop(genKey(key)));
         } catch (JedisException e) {
           log.error(e.getMessage());
           if (jedis != null) {
             jedis.close();
           }
         } catch (Exception e) {
           log.error(e.getMessage());
         } finally {
           if (jedis != null) {
             jedis.close();
           }
         }
         return null;
    }
    /**
     * 移除表尾数据
     * @param key
     * @param db
     * @return
     */
    public static Object RPOP(String key,int db){
        Jedis jedis = getJedis();
         try {
          if(db > 0)
               jedis.select(db);
           return SerializeUtils.deserialize(jedis.rpop(genKey(key)));
         } catch (JedisException e) {
           log.error(e.getMessage());
           if (jedis != null) {
             jedis.close();
           }
         } catch (Exception e) {
           log.error(e.getMessage());
         } finally {
           if (jedis != null) {
             jedis.close();
           }
         }
         return null;
    }
    
    public static Object lindex(String key, int db){
    	Jedis jedis = getJedis();
        try {
         if(db > 0)
              jedis.select(db);
         byte[] j = jedis.lindex(genKey(key),0);
          return SerializeUtils.deserialize(j);
        } catch (JedisException e) {
          log.error(e.getMessage());
          if (jedis != null) {
            jedis.close();
          }
        } catch (Exception e) {
          log.error(e.getMessage());
        } finally {
          if (jedis != null) {
            jedis.close();
          }
        }
        return null;
    }
    

	private static byte[] genKey(String key){
		try {
			return key.getBytes("utf-8");
			//return SerializeUtil.serialize(key);
		} catch (Exception e) {
			//can't happen
			return key.getBytes();
		}
	}
	
	private String byte2String(byte[] key){
		try {
			return new String(key,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return new String(key);
		}
	}
	
	
	
	/**
	 * 多个key get
	 * @param keys
	 * @return
	 */
	public Map<String,Object> mget(final Collection<String> keys){
		return this.redisOp(new RedisExecutor<Map<String,Object>>(){
			@Override
			public Map<String,Object> execute(Jedis jedis) {
				HashMap<String, Object> result = new HashMap<String,Object>();
				HashSet<byte[]> set = new HashSet<byte[]>();
				for(String key:keys){
					byte[] genKey = genKey(key);
					set.add(genKey);
				}
				byte[][] keyArr = set.toArray(new byte[0][0]);
				List<byte[]> values = jedis.mget(keyArr);
				if(values.size()>0&&values.size()==keys.size()){
					for(int i=0;i<values.size();i++){
						String key = byte2String(keyArr[i]);
						Object value = null;
						if(values.get(i)!=null){
							value = SerializeUtils.deserialize(values.get(i));
						}
						result.put(key, value);
					}
				}
				return result;
			}
		}, "mget",keys);
	}
	
	public void expire(final String key,final int seconds){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				jedis.expire(key, seconds);
				return null;
			}
		}, "expire",key,seconds);
	}
	
	
	/**
	 * 删除缓存
	 * @param key
	 */
	public void del(final String key){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				jedis.del(genKey);
				return null;
			}
		}, "del",key);
	}

	/**
	 * 删除缓存
	 * @param key
	 */
	public void remove(final String key){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				jedis.del(genKey);
				return null;
			}
		}, "del",key);
	}
	
	
	/**
	 * 批量删除缓存
	 * @param keys
	 */
	public void del(final Set<String> keys){
		this.redisOp(new RedisExecutor<Void>(){
			@Override
			public Void execute(Jedis jedis) {
				HashSet<byte[]> set = new HashSet<byte[]>();
				for(String key:keys){
					byte[] genKey = genKey(key);
					set.add(genKey);
				}
				jedis.del(set.toArray(new byte[0][0]));
				return null;
			}
		}, "del",keys);
	}
	
	
	public Object getObject(final String key){
		return this.redisOp(new RedisExecutor<Object>(){
			@Override
			public Object execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				jedis.select(DEFAULT_DB);
				byte[] bs = jedis.get(genKey);
				Object res = null;
				if(bs!=null){
					res = SerializeUtils.deserialize(bs);
				}
				return res;
			}
		}, "get",key);
	}
	
	public Object getObject(final int dbindex,final String key){
		return this.redisOp(new RedisExecutor<Object>(){
			@Override
			public Object execute(Jedis jedis) {
				byte[] genKey = genKey(key);
				jedis.select(dbindex);
				byte[] bs = jedis.get(genKey);
				Object res = null;
				if(bs!=null){
					res = SerializeUtils.deserialize(bs);
				}
				return res;
			}
		}, "get",key);
	}
	
	
 
	public static class NullObject implements java.io.Serializable{

		private static final long serialVersionUID = 882419175555440980L;
		
	}
	
	
	public Object eval(String script, int keyCount, String... params){
		Jedis jedis = this.getJedis();
		Object obj = jedis.eval(script,keyCount,params); 
		
		this.returnJedis(jedis);
		
		return obj;
	}

}
