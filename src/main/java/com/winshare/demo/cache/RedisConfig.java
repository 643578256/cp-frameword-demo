package com.winshare.demo.cache;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class RedisConfig {


     @EnableConfigurationProperties(value = RedisCacheProperties.class)
@Configuration(proxyBeanMethods = false)
@Conditional(RedisCondition.class)
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private RedisCacheProperties properties;

    @Bean
    @Primary
    public RedisTemplate redisTemplate(@Autowired ApplicationContext applicationContext){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(applicationContext.getBean(RedisConnectionFactory.class));
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnProperty(prefix = "im.redis",name = "client-type",havingValue = "lettuceCluster")
    public LettuceConnectionFactory lettuceCluster(){

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.setClusterNodes(redisNodeList());
        clusterConfig.setMaxRedirects(3);
        if(StringUtils.isNotBlank(this.properties.getPassword())){
            clusterConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
        }


        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        if(properties.getPool() == null){
            properties.setPool(new RedisCacheProperties.Pool());
        }
        genericObjectPoolConfig.setMinIdle(properties.getPool().getMinIdle());
        genericObjectPoolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(properties.getPool().getMaxTotal());
        genericObjectPoolConfig.setMaxWaitMillis(1000);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(100);

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enableAllAdaptiveRefreshTriggers()  // 开启所有自适应刷新,自适应刷新不开启,Redis集群变更时将会导致连接异常
                //.enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.ASK_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.UNKNOWN_NODE)// 开启指定自适应刷新
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30)) // 自适应刷新超时时间(默认30秒)
                .enablePeriodicRefresh(Duration.ofSeconds(20))// 开启周期刷新(默认60秒)

                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)//拓扑刷新
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .socketOptions(SocketOptions.builder().keepAlive(true).build())
                .validateClusterNodeMembership(false)// 取消校验集群节点的成员关系
                .build();


        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED) //io.lettuce.core.ReadFromImpl.ReadFromNearest#select 读节点的选择
                .clientOptions(clusterClientOptions)
                .commandTimeout(Duration.ofSeconds(10))
                .poolConfig(genericObjectPoolConfig).build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig,clientConfig);
        return factory;
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnProperty(prefix = "im.redis",name = "client-type",havingValue = "jedisCluster")
    public JedisConnectionFactory jedisCluster(){

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        if(StringUtils.isNotBlank(this.properties.getPassword())){
            clusterConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
        }
        clusterConfig.setClusterNodes(redisNodeList());
        clusterConfig.setMaxRedirects(3);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        if(properties.getPool() == null){
            properties.setPool(new RedisCacheProperties.Pool());
        }
        poolConfig.setMinIdle(properties.getPool().getMinIdle());
        poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        poolConfig.setMaxTotal(properties.getPool().getMaxTotal());
        poolConfig.setTestOnBorrow(true);
        JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfig,poolConfig);
        return factory;
    }

    @Bean
    @ConditionalOnProperty(prefix = "im.redis",name = "client-type",havingValue = "lettuce")
    public RedisConnectionFactory lettuceRedis(){
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        if(StringUtils.isNotBlank(this.properties.getPassword())){
            standaloneConfig.setPassword(RedisPassword.of(this.properties.getPassword()));
        }
        standaloneConfig.setDatabase(0);
        if(StringUtils.isEmpty(properties.getUrl())){
            throw new ApplicationContextException("redis 连接配置错误");
        }
        String[] split = properties.getUrl().split("\\@");
        if(split.length > 1){
            String[] hostAndPort = split[1].split("\\:");
            standaloneConfig.setHostName(hostAndPort[0]);
            standaloneConfig.setPort(Integer.parseInt(hostAndPort[1]));
        }else {
            String[] hostAndPort = split[0].split("\\:");
            standaloneConfig.setHostName(hostAndPort[0]);
            standaloneConfig.setPort(Integer.parseInt(hostAndPort[1]));
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        if(properties.getPool() == null){
            properties.setPool(new RedisCacheProperties.Pool());
        }
        poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        poolConfig.setMinIdle(properties.getPool().getMinIdle());
        poolConfig.setMaxTotal(properties.getPool().getMaxTotal());

        LettuceClientConfiguration clientConfig =  LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        RedisConnectionFactory factory = new LettuceConnectionFactory(standaloneConfig,clientConfig);
        return factory;
    }

    private List<RedisNode> redisNodeList(){
        List list = Lists.newArrayList();
        String[] nodes = properties.getUrl().split("\\,");
        if(nodes.length < 1){
            throw new ApplicationContextException("redis 连接配置错误");
        }
        for (int i = 0; i < nodes.length; i++) {
            String[] split = nodes[i].split("\\:");
            list.add(new RedisNode(split[0], Integer.parseInt(split[1])));
        }
        return list;
    }


}
