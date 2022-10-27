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


     @Bean
     @ConditionalOnProperty(prefix = "spring.redis",name = "client-type",havingValue = "lettuce",matchIfMissing = false)
     public RedisConnectionFactory lettuceCluster(){



          RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
          List<RedisNode> redisNodeList = new ArrayList<>();
          redisNodeList.add(new RedisNode("132.232.20.136",7001));
          redisNodeList.add(new RedisNode("132.232.20.136",7002));
          redisNodeList.add(new RedisNode("132.232.20.136",7003));
          redisNodeList.add(new RedisNode("132.232.20.136",7004));
          redisNodeList.add(new RedisNode("132.232.20.136",7005));
          redisNodeList.add(new RedisNode("132.232.20.136",7006));
          clusterConfig.setClusterNodes(redisNodeList);
          clusterConfig.setMaxRedirects(3);


          GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
          genericObjectPoolConfig.setMaxIdle(10);
          genericObjectPoolConfig.setMinIdle(10);
          genericObjectPoolConfig.setMaxTotal(50);
          genericObjectPoolConfig.setMaxWaitMillis(1000);
          genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(100);

          ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                  .enablePeriodicRefresh(Duration.ofSeconds(10))// 开启周期刷新(默认60秒)
                  .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.ASK_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.UNKNOWN_NODE)// 开启自适应刷新

                  .build();
          ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                  .topologyRefreshOptions(clusterTopologyRefreshOptions)//拓扑刷新
                  .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                  .autoReconnect(true)
                  .socketOptions(SocketOptions.builder().keepAlive(true).build())
                  .validateClusterNodeMembership(false)// 取消校验集群节点的成员关系
                  .build();


          LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                  .readFrom(ReadFrom.REPLICA_PREFERRED)
                  .clientOptions(clusterClientOptions)
                  .commandTimeout(Duration.ofSeconds(10))
                  .poolConfig(genericObjectPoolConfig).build();

          RedisConnectionFactory factory = new LettuceConnectionFactory(clusterConfig,clientConfig);
          return factory;
     }


     @Bean
     @ConditionalOnProperty(prefix = "spring.redis",name = "client-type",havingValue = "jedis",matchIfMissing = true)
     public RedisConnectionFactory jedisCluster(){

          RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
          List<RedisNode> redisNodeList = new ArrayList<>();
          redisNodeList.add(new RedisNode("132.232.20.136",7001));
          redisNodeList.add(new RedisNode("132.232.20.136",7002));
          redisNodeList.add(new RedisNode("132.232.20.136",7003));
          redisNodeList.add(new RedisNode("132.232.20.136",7004));
          redisNodeList.add(new RedisNode("132.232.20.136",7005));
          redisNodeList.add(new RedisNode("132.232.20.136",7006));
          clusterConfig.setClusterNodes(redisNodeList);
          clusterConfig.setMaxRedirects(3);

          JedisPoolConfig poolConfig = new JedisPoolConfig();
          poolConfig.setMaxIdle(70);
          poolConfig.setMaxTotal(100);
          poolConfig.setTestOnBorrow(true);
          RedisConnectionFactory factory = new JedisConnectionFactory(clusterConfig,poolConfig);
          return factory;
     }

     @Bean
     @Primary
     public RedisTemplate redisTemplate(RedisConnectionFactory factory ){
          RedisTemplate redisTemplate = new RedisTemplate();
          redisTemplate.setConnectionFactory(factory);
          return redisTemplate;
     }


}
