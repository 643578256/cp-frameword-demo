package com.winshare.demo.es.config;

import com.winshare.demo.es.api.ESIndexHelpService;
import com.winshare.demo.es.api.SearchQueryHelpService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = SearchProperties.class)
public class SearchConfig {


    @Bean(name = "esClient",destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient(SearchProperties properties){
        HttpHost hosts = new HttpHost(properties.getHost(),properties.getPort());
        RestClientBuilder builder = RestClient.builder(hosts);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    @Bean
    public SearchQueryHelpService esSearchService(RestHighLevelClient esClient){
        SearchQueryHelpService search = new SearchQueryHelpService(esClient);
        return search;
    }

    @Bean
    public ESIndexHelpService esIndexService(RestHighLevelClient esClient){
        ESIndexHelpService index = new ESIndexHelpService(esClient);
        return index;
    }


}
