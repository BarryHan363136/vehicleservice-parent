package com.bmw.vehicleservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * 描述: Redis服务配置
 *
 * @outhor hants
 * @create 2018-04-27 上午10:19
 */
@Configuration
public class RedisConfiguration implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
    public static final String PARAS_CONFIG_FILENAME = "application.properties";
    protected static final Properties properties = new Properties();
    private Environment environment;

    /**
     * Redis服务配置参数
     * */
    private Integer maxIdle;
    private Long maxWait;
    private Boolean testOnBorrow;
    private String hostName;
    private Integer port;
    private String password;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        logger.info("开始初始化redis配置参数...");
        try {
            /**
             * 1.从系统环境变量中获取redis配置参数，如果获取不到就从配置文件中获取
             * */
            this.maxIdle = StringUtils.isNotBlank(environment.getProperty("redis.maxIdle")) ? Integer.parseInt(environment.getProperty("redis.maxIdle")) : 200;
            this.maxWait = StringUtils.isNotBlank(environment.getProperty("redis.maxWait")) ? Long.valueOf(environment.getProperty("redis.maxWait")) : 1000;
            this.testOnBorrow = StringUtils.isNotBlank(environment.getProperty("redis.testOnBorrow")) ? Boolean.valueOf(environment.getProperty("redis.testOnBorrow")) : true;
            this.hostName = environment.getProperty("redis.addr");
            this.port = StringUtils.isNotBlank(environment.getProperty("redis.port")) ? Integer.parseInt(environment.getProperty("redis.port")) : null;
            this.password = environment.getProperty("redis.auth");

            if (StringUtils.isBlank(hostName) ||
                    StringUtils.isBlank(hostName) ||
                    port==null ||
                    StringUtils.isBlank(password)){
                logger.warn("<=====从配置文件中获取redis配置参数,系统环境变量未设置redis相关参数!!!=====>");
                /**
                 * 2.从配置文件中获取redis配置参数
                 * */
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(PARAS_CONFIG_FILENAME);
                if (in!=null){
                    properties.load(in);
                }
                this.hostName = properties.getProperty("redis.addr");
                this.port = StringUtils.isNotBlank(properties.getProperty("redis.port")) ? Integer.parseInt(properties.getProperty("redis.port")) : 6379;
                this.password = properties.getProperty("redis.auth");
            }
            logger.info("redis配置参数初始化完成...");
        } catch (Exception e) {
            logger.error("RedisConfiguration服务初始化失败 {} ", e);
        }
    }

    @Bean
    public JedisPoolConfig getJedisConfig(){
        JedisPoolConfig jedisConfig = new JedisPoolConfig();
        //jedisConfig.setMaxActive();无此属性
        jedisConfig.setMaxIdle(maxIdle);
        jedisConfig.setMaxWaitMillis(maxWait);
        jedisConfig.setTestOnBorrow(testOnBorrow);

        return jedisConfig;
    }

    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setPoolConfig(getJedisConfig());
        return jedisConnectionFactory;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

    @Bean
    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        redisTemplate.setKeySerializer(stringRedisSerializer());
        /**
         * 序列化方式为:StringRedisSerializer,JdkSerializationRedisSerializer
         * 默认采用StringRedisSerializer,但是不能存储对象、数组等，JdkSerializationRedisSerializer则支持
         * 自定义序列化方式: 使用Jackson2JsonRedisSerialize替换默认序列化
         * */
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}