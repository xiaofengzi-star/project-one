package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public PageResult search(TbGoods tbGoods, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(goods.get***())){
            criteria.andLike("***", "%" + goods.get***() + "%");
        }*/

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();

        //1、根据商品id查询商品基本信息
        goods.setGoods(goodsMapper.selectByPrimaryKey(id));

        //2、根据商品id查询商品描述信息
        goods.setGoodsDesc(goodsDescMapper.selectByPrimaryKey(id));

        //3、根据商品id查询商品sku列表
        TbItem param = new TbItem();
        param.setGoodsId(id);

        goods.setItemList(itemMapper.select(param));
        return goods;
    }

    @Override
    public void add(Goods goods) {
        goodsMapper.insert(goods.getGoods());
        goodsDescMapper.insert(goods.getGoodsDesc());
        for (TbItem tbItem : goods.getItemList()) {
            itemMapper.insert(tbItem);
        }
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.updateByPrimaryKeySelective(goods.getGoods());
        goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());
        for (TbItem tbItem : goods.getItemList()) {
            itemMapper.updateByPrimaryKeySelective(tbItem);
        }
    }
}
