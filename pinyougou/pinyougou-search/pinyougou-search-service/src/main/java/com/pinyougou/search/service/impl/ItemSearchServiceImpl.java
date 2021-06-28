package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = ItemSearchService.class)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        SimpleQuery query = new SimpleQuery();

        //查询条件
        Criteria criteria = new
                Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //查询
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query,
                TbItem.class);

        //设置返回的商品列表
        resultMap.put("rows", scoredPage.getContent());
        return resultMap;
    }
}
