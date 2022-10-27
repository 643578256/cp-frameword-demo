package com.winshare.demo.es.test;

import com.winshare.demo.es.CommFieldListVo;
import com.winshare.demo.es.SearchResultVo;
import com.winshare.demo.es.api.ESIndexHelpService;
import com.winshare.demo.es.api.SearchQueryHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsTestController {

    @Autowired
    private ESIndexHelpService esIndexService;
    @Autowired
    private SearchQueryHelpService esSearchService;

    @PostMapping("/es/add/doc")
    public void addDoc(@RequestBody OrderInfo orderInfo){
        esIndexService.addDoc(orderInfo);
    }

    @PostMapping("/es/find/doc")
    public SearchResultVo findDoc(@RequestBody OrderInfo orderInfo){
        CommFieldListVo vo = new CommFieldListVo();
        vo.setIndexName("order_info");
        vo.addAndEqualtField("address",orderInfo.getAddress());
        SearchResultVo search = esSearchService.search(vo);
        return search;
    }
}
