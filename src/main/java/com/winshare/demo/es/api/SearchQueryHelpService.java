package com.winshare.demo.es.api;

import com.winshare.demo.es.*;
import org.apache.commons.collections4.MapUtils;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class SearchQueryHelpService extends AbstractBaseEs implements ISearchHelpApi{

    public SearchQueryHelpService(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public SearchResultVo search(CommFieldListVo search) {

        BoolQueryBuilder builders = QueryBuilders.boolQuery();
        List<AbstactTermVo> fieldVoList = search.getFieldVoList();
        for (AbstactTermVo termVo : fieldVoList) {
            buildCommtQuery(termVo, builders,search.getIndexName());
        }
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .timeout(new TimeValue(3, TimeUnit.SECONDS))
                .from(search.getFrom())
                .size(search.getSize())
                .fetchSource(search.isFetchSource())
                .query(builders);
        if(StringUtils.isEmpty(search.getSortFiledName())
                || StringUtils.isEmpty(search.getSortOrder())){

        }else {
            builder.sort(search.getSortFiledName(),search.getSortOrder());
        }
        SearchRequest searchRequest = new SearchRequest(search.getIndexName());
        //es7.0 默认 ——doc searchRequest.types(search.getType());
        searchRequest.source(builder);
        if (!StringUtils.isEmpty(search.getRouting())) {
            searchRequest.routing(search.getRouting());
        }
        if (search.isUseScorll()) {
            if (search.getScroll() != null) {
                searchRequest.scroll(search.getScroll());
            }
        }

        try {
            SearchResponse sr = super.client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = sr.getHits();
            List<Map<String, Object>> maps = wrapDocValues(hits);
            SearchResultVo resultVo = new SearchResultVo();
            resultVo.setScrollId(sr.getScrollId());
            resultVo.setDocValues(maps);
            resultVo.setTotalSize(hits.getTotalHits().value);
            resultVo.setPageSize(search.getSize());
            resultVo.setStartIndex(search.getFrom());
            return resultVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public SearchResultVo scrollSearch(String scrollId, TimeValue keepAlive) {
        if (StringUtils.isEmpty(scrollId) || keepAlive == null) {
            throw new IllegalArgumentException("参数异常");
        }

        SearchScrollRequest request = new SearchScrollRequest();
        request.scrollId(scrollId);
        request.scroll(keepAlive);
        SearchResponse sr =  null;
        try {
            sr = super.client.scroll(request, RequestOptions.DEFAULT);
            SearchHits hits = sr.getHits();
            SearchHit[] hitsArrars = hits.getHits();
            if(hitsArrars == null || hitsArrars.length == 0){
                closeScroll(scrollId);
            }
            List<Map<String, Object>> maps = wrapDocValues(hits);
            SearchResultVo resultVo = new SearchResultVo();
            resultVo.setScrollId(sr.getScrollId());
            resultVo.setDocValues(maps);
            resultVo.setTotalSize(hits.getTotalHits().value);
            return resultVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean closeScroll(String scorllId) {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.setScrollIds(Arrays.asList(scorllId));
        try {
            ClearScrollResponse clearScrollResponse = super.client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            return clearScrollResponse.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public SearchResultVo searchAfter(CommFieldListVo search,Object[] afterSort) {
        BoolQueryBuilder builders = QueryBuilders.boolQuery();
        List<AbstactTermVo> fieldVoList = search.getFieldVoList();
        for (AbstactTermVo termVo : fieldVoList) {
            buildCommtQuery(termVo, builders,search.getIndexName());
        }
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .timeout(new TimeValue(3, TimeUnit.SECONDS))
                .from(0)
                .size(search.getSize())
                .fetchSource(search.isFetchSource())
                .query(builders);

        SearchRequest searchRequest = new SearchRequest(search.getIndexName());
        searchRequest.types(search.getType());
        builder.sort(search.getSortFiledName(),search.getSortOrder());
        if(afterSort != null && afterSort.length > 0){
            builder.searchAfter(afterSort);
        }
        searchRequest.source(builder);
        if (!StringUtils.isEmpty(search.getRouting())) {
            searchRequest.routing(search.getRouting());
        }
        try {
            SearchResponse sr = super.client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = sr.getHits();
            List<Map<String, Object>> maps = wrapDocValues(hits);
            SearchResultVo resultVo = new SearchResultVo();
            resultVo.setDocValues(maps);
            resultVo.setPageSize(search.getSize());
            resultVo.setStartIndex(search.getFrom());
            SearchHit[] hit = hits.getHits();
            resultVo.setAfterSort(hit[hit.length-1].getSortValues());
            return resultVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Map<String, Object>> wrapDocValues(SearchHits hits) {
        SearchHit[] hit = hits.getHits();
        List<Map<String, Object>> list = new ArrayList<>(hit.length);
        for (int i = 0; i < hit.length; i++) {
            Map<String, Object> item = hit[i].getSourceAsMap();
            Map<String, HighlightField> hMap = hit[i].getHighlightFields();
            StringBuilder builder = new StringBuilder();
            if (MapUtils.isNotEmpty(hMap)) {
                Set<String> keySet = hMap.keySet();
                keySet.forEach(key -> {
                    Text[] fragments = hMap.get(key).fragments();
                    for (int j = 0; j < fragments.length; j++) {
                        builder.append(fragments[j]);
                    }
                    item.put(key, builder.toString());
                });

            }
            item.put("_score", hit[i].getScore());
            list.add(item);
        }
        return list;
    }

    private QueryBuilder buildCommtQuery(AbstactTermVo termVo, BoolQueryBuilder mainBuilder,String indexName) {
        String fieldName = termVo.getFieldName();
        FilterLogicType logicType = termVo.getLogicType();
        boolean nested = false;
        String[] split = fieldName.split("\\.");
        if (logicType == FilterLogicType.NESTED) {
            nested = true;
        }
        Map<String, Object> mapping = findMapping(indexName);
        Map<String, Object> fieldNameType = (Map<String, Object>)mapping.get(termVo.getFieldName());
        if(MapUtils.isNotEmpty(fieldNameType)){
            Object type = fieldNameType.get("type");
            //termVo.setFieldName(termVo.getFieldName()+"."+type.toString());
            termVo.setFieldName(termVo.getFieldName());
        }
        switch (termVo.getTermType()) {
            case MATCH:

                QueryBuilder match = buildHasNested(nested, split, QueryBuilders.matchQuery(termVo.getFieldName(), termVo.getFieldValue()));
                //分词器
                mainBuilder.filter(match);
                break;
            case TERM:
                QueryBuilder term = buildHasNested(nested, split, QueryBuilders.termQuery(termVo.getFieldName(), termVo.getFieldValue()));
                //term 不分词器
                mainBuilder.filter(term);
                break;
            case NOT_TERM:
                QueryBuilder mustNot = buildHasNested(nested, split, QueryBuilders.termQuery(termVo.getFieldName(), termVo.getFieldValue()));
                mainBuilder.mustNot(mustNot);
                break;
            case IN:
                Object[] inValues = (Object[]) termVo.getFieldValue();
                QueryBuilder terms = buildHasNested(nested, split, QueryBuilders.termsQuery(termVo.getFieldName(), inValues));
                mainBuilder.filter(terms);
                break;
            case NOT_IN:
                Object[] noInValues = (Object[]) termVo.getFieldValue();
                QueryBuilder mustNotNested = buildHasNested(nested, split, QueryBuilders.termsQuery(termVo.getFieldName(), noInValues));
                mainBuilder.mustNot(mustNotNested);
                break;
            case FORM_TO:
                ESRangeVo rangeVo = (ESRangeVo) termVo;
                RangeQueryBuilder to = QueryBuilders
                        .rangeQuery(termVo.getFieldName())
                        .from(rangeVo.getFrom(), true)
                        .to(((ESRangeVo) termVo).getTo(), true);
                QueryBuilder nest_to_from = buildHasNested(nested, split, to);
                mainBuilder.filter(nest_to_from);
                break;
            case GT:
                RangeQueryBuilder gt = QueryBuilders
                        .rangeQuery(termVo.getFieldName())
                        .gt(termVo.getFieldValue());
                QueryBuilder gtTerm = buildHasNested(nested, split, gt);
                mainBuilder.filter(gtTerm);
                break;
            case LT:
                mainBuilder.filter(buildHasNested(nested, split, QueryBuilders
                        .rangeQuery(termVo.getFieldName())
                        .lt(termVo.getFieldValue())));
                break;
            case GTE:
                mainBuilder.filter(buildHasNested(nested, split, QueryBuilders
                        .rangeQuery(termVo.getFieldName())
                        .gte(termVo.getFieldValue())));
                break;
            case LTE:
                mainBuilder.filter(buildHasNested(nested, split, QueryBuilders
                        .rangeQuery(termVo.getFieldName())
                        .lte(termVo.getFieldValue())));
                break;
            case PREFIX:
                mainBuilder.filter(buildHasNested(nested, split, QueryBuilders
                        .prefixQuery(termVo.getFieldName(),termVo.getFieldValue().toString())));
                break;
            case FUZZY:
                mainBuilder.filter(buildHasNested(nested, split, QueryBuilders
                        .fuzzyQuery(termVo.getFieldName(),termVo.getFieldValue().toString())));
                break;
            default:
                throw new UnsupportedOperationException("不支持此操作" + termVo.getTermType().name());
        }
        return mainBuilder;

    }

    private QueryBuilder buildHasNested(boolean nested, String[] split, QueryBuilder queryBuilder) {
        if (nested) {
            return QueryBuilders.nestedQuery(split[0], queryBuilder, ScoreMode.None);
        } else {
            return queryBuilder;
        }
    }


}
