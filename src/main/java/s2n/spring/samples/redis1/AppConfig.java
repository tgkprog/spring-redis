package s2n.spring.samples.redis1;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ComponentScan("s2n.spring.samples.redis1")
@PropertySource("classpath:/redis.properties")
@EnableCaching
public class AppConfig {

	public static final String CAC = "messageCache";
	private @Value("${redis.host-name}") String redisHostName;
	private @Value("${redis.port}") int redisPort;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHostName);
		factory.setPort(redisPort);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	CacheManager cacheManager() {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate());
		//rcm.setDefaultExpiration(999999);
		rcm.setUsePrefix(false);
		//seperator
		RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix("-");

		rcm.setCachePrefix(cachePrefix);
		rcm.getCache(CAC);
		System.out.println("Cac ore :" + cachePrefix.prefix(CAC));
		
		//debug
		SimpleCacheManager cm = new SimpleCacheManager();
		org.springframework.cache.concurrent.ConcurrentMapCache cmc = new org.springframework.cache.concurrent.ConcurrentMapCache(CAC);
		cmc.put("S2", "aa val.");
		Collection<Cache> col = new ArrayList<Cache>();
		col.add(cmc);
		cm.setCaches(col);
		//
		
		boolean useDebug = false;
		if(useDebug)return cm;
		
		return rcm;
	}

}
