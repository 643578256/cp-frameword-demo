package com.winshare.demo.es.api;

import com.winshare.demo.es.CommFieldListVo;
import com.winshare.demo.es.SearchResultVo;
import org.elasticsearch.common.unit.TimeValue;

public interface ISearchHelpApi {

    SearchResultVo search(CommFieldListVo search);

    SearchResultVo scrollSearch(String scorllId, TimeValue keepAlive);

    boolean closeScroll(String scorllId);

    SearchResultVo searchAfter(CommFieldListVo search,Object[] afterSort);

}
