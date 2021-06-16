package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;

public interface GoodsService extends BaseService<TbGoods> {

    Goods findOneByGoodId(Long id);

    void add(Goods goods);

    void update(Goods goods);

    void updateStatus(Long[] ids,String status);
}