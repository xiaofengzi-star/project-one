package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;

import java.util.List;

public interface GoodsService extends BaseService<TbGoods> {

    Goods findOneByGoodId(Long id);

    void add(Goods goods);

    void update(Goods goods);

    void updateStatus(Long[] ids,String status);

    void deleteGoodsByIds(Long[] ids);

    List<TbItem> findItemListByGoodsAndStatus(Long[] ids, String status);
}