package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = ItemSearchService.class)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //创建高亮搜索对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //查询条件
        Criteria criteria = new
                Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //品牌过滤
        setSolrQuery(searchMap,"brand","item_brand",query);
        //商品分类过滤
        setSolrQuery(searchMap,"category","item_category",query);
        //规格过滤
        if (!StringUtils.isEmpty(searchMap.get("spec"))){
            Map<String, String> spec = (Map<String, String>) searchMap.get("spec");
            Set<Map.Entry<String, String>> entrySet = spec.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                Criteria specCriteria = new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
                SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(simpleFilterQuery);
            }
        }
        //价格过滤
        if (!StringUtils.isEmpty(searchMap.get("price"))){
            String[] prices = searchMap.get("price").toString().split("-");
            Criteria minPriceCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
            SimpleFilterQuery minPriceFilterQuery = new SimpleFilterQuery(minPriceCriteria);
            query.addFilterQuery(minPriceFilterQuery);

            if (!"*".equals(prices[1])){
                Criteria maxPriceCriteria = new Criteria("item_price").lessThan(prices[1]);
                SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(maxPriceCriteria);
                query.addFilterQuery(simpleFilterQuery);
            }
        }

        //设置分页信息
        Integer pageNo = 1;
        Integer pageSize = 40;
        if (searchMap.get("pageNo") != null){
            pageNo = Integer.parseInt(searchMap.get("pageNo").toString());
        }
        if (searchMap.get("pageSize") != null){
            pageSize = Integer.parseInt(searchMap.get("pageSize").toString());
        }
        //起始索引号
        query.setOffset((pageNo-1)*pageSize);
        //页大小
        query.setRows(pageSize);

        //设置高亮域
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        //高亮标签
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);
        //排序
        if (!StringUtils.isEmpty(searchMap.get("sort")) && !StringUtils.isEmpty(searchMap.get("sortField"))){
            String sortField = searchMap.get("sort").toString();
            Sort sort = new Sort(sortField.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    "item_" + searchMap.get("sortField"));
            query.addSort(sort);
        }

        //查询
        HighlightPage<TbItem> itemHighlightPage =
                solrTemplate.queryForHighlightPage(query, TbItem.class);

        //处理高亮标题
        List<HighlightEntry<TbItem>> highlighted =
                itemHighlightPage.getHighlighted();

        if (highlighted!=null&&highlighted.size()>0){
            for (HighlightEntry<TbItem> entry : highlighted) {
                List<HighlightEntry.Highlight> highlights = entry.getHighlights();
                if (highlights != null && highlights.size() > 0 &&
                        highlights.get(0).getSnipplets() != null) {
                    //设置高亮标题
                    entry.getEntity().setTitle(highlights.get(0).getSnipplets().get(0));
                }
            }

        }


        //设置返回的商品列表
        resultMap.put("rows", itemHighlightPage.getContent());
        resultMap.put("totalPages",itemHighlightPage.getTotalPages());
        resultMap.put("total",itemHighlightPage.getTotalElements());
        return resultMap;
    }

    /**
     *  设置过滤搜索条件
     * @param searchMap 搜索条件Map
     * @param filed     过滤条件
     * @param solrFiled 对应实体类Field
     * @param query     操作的query
     */
    private void setSolrQuery(Map<String, Object> searchMap,String filed,String solrFiled,SimpleHighlightQuery query){
        if (!StringUtils.isEmpty(searchMap.get(filed))){
            Criteria brandCriteria = new Criteria(solrFiled).is(searchMap.get(filed));
            SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(simpleFilterQuery);
        }
    }
}
