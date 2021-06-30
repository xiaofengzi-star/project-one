package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public PageResult search(TbGoods goods, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();

        //不查找已经删除的商品
        criteria.andNotEqualTo("isDelete","1");

        //根据sellerId获取商品
        if(!StringUtils.isEmpty(goods.getSellerId())){
            criteria.andEqualTo("sellerId", goods.getSellerId());
        }
        //搜索条件  状态
        if(!StringUtils.isEmpty(goods.getAuditStatus())){
            criteria.andEqualTo("auditStatus",goods.getAuditStatus());
        }
        //搜索条件  商品名称
        if(!StringUtils.isEmpty(goods.getGoodsName())){
            criteria.andLike("goodsName","%"+goods.getGoodsName()+"%");
        }
        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Goods findOneByGoodId(Long id) {
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
        add(goods.getGoods());
        /*//GoodsDesc不知道为啥id没有自动生成  insert Goods后主动查询id
        List<TbGoods> tbGoods = goodsMapper.select(goods.getGoods());
        goods.getGoodsDesc().setGoodsId(tbGoods.get(0).getId());*/

        //1、保存基本信息；在mybatis中如果在保存成功后主键可以回填到保存时候的那个对象中
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insert(goods.getGoodsDesc());

        //新增sku
        saveItemList(goods);
    }

    @Override
    public void update(Goods goods) {
        //更新之后状态为未审核
        goods.getGoods().setAuditStatus("0");
        goodsMapper.updateByPrimaryKeySelective(goods.getGoods());
        goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());

        //先删掉再更新
        TbItem param = new TbItem();
        param.setGoodsId(goods.getGoods().getId());
        itemMapper.delete(param);
        saveItemList(goods);
    }


    /**
     * 保存sku动态数据
     * @param goods goods
     */
    private void saveItemList(Goods goods){

        //是否启用规格
        if ("1".equals(goods.getGoods().getIsEnableSpec())){
            for (TbItem item : goods.getItemList()) {
                TbItem tbItem = new TbItem();
                setSameValue(tbItem,goods);

                tbItem.setPrice(item.getPrice());
                tbItem.setNum(item.getNum());
                tbItem.setIsDefault(item.getIsDefault());
                tbItem.setSpec(item.getSpec());

                //拿到规格列表
                Map maps = JSON.parseObject(item.getSpec(), Map.class);
                //title = 商品名称+规格
                String title = goods.getGoods().getGoodsName();
                for (Object value : maps.values()) {
                    title += " " + value  ;
                }
                tbItem.setTitle(title);

                //设置新增sku的其他参数
                setItemValue(tbItem,goods);
                itemMapper.insert(tbItem);
            }

        }else {
            TbItem tbItem = new TbItem();
            setSameValue(tbItem,goods);

            //商品标题
            tbItem.setTitle(goods.getGoods().getGoodsName());

            tbItem.setNum(9999);
            tbItem.setIsDefault("1");
            tbItem.setSpec("{}");
            tbItem.setPrice(goods.getGoods().getPrice());
            //设置新增sku的其他参数
            setItemValue(tbItem,goods);

            itemMapper.insert(tbItem);
        }

    }

    /**
     * 设置新增sku的其他参数
     * @param tbItem insert参数
     * @param goods goods
     */
    private void setItemValue(TbItem tbItem, Goods goods) {

            //image  url
            if (!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())){
                List<Map> maps = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
                tbItem.setImage(maps.get(0).get("url").toString());
            }

            //所属类目  叶子类目
            tbItem.setCategoryid(goods.getGoods().getCategory3Id());
            //更新时间
            tbItem.setUpdateTime(new Date());

            //根据Category3Id获取Category
            TbItemCat param = new TbItemCat();
            param.setId(goods.getGoods().getCategory3Id());
            tbItem.setCategory(itemCatMapper.selectByPrimaryKey(param).getName());

            //根据BrandId获取BrandName
            TbBrand tbBrand = new TbBrand();
            tbBrand.setId(goods.getGoods().getBrandId());
            tbItem.setBrand(brandMapper.selectByPrimaryKey(tbBrand).getName());

            //根据sellerId获取seller
            TbSeller tbSeller = new TbSeller();
            tbSeller.setSellerId(tbItem.getSellerId());
            tbItem.setSeller(sellerMapper.selectByPrimaryKey(tbSeller).getName());
    }

    /**
     * 设置新增sku的其他参数
     * @param tbItem update参数
     * @param goods goods
     */
    private void setSameValue(TbItem tbItem,Goods goods){
        //未审核
        tbItem.setStatus("0");
        tbItem.setSellerId(goods.getGoods().getSellerId());
        tbItem.setGoodsId(goods.getGoods().getId());
        tbItem.setCreateTime(new Date());
    }

    /**
     * 根据选中的id跟状态更新goods  以及更新item
     * @param ids 选中的商品id
     * @param status 更新后的状态
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = new TbGoods();
            tbGoods.setId(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKeySelective(tbGoods);
            if ("2".equals(status)){
                Example example = new Example(TbItem.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("goodsId",id);

                TbItem tbItem = new TbItem();
                tbItem.setStatus("1");

                itemMapper.updateByExampleSelective(tbItem,example);
            }
        }
    }

    /**
     * 根据选中的id 删除 goods
     * @param ids 选中的商品id
     */
    @Override
    public void deleteGoodsByIds(Long[] ids) {
        for (Long id : ids) {
            TbGoods param = new TbGoods();
            param.setId(id);
            param.setIsDelete("1");
            goodsMapper.updateByPrimaryKeySelective(param);
        }
    }

    @Override
    public List<TbItem> findItemListByGoodsAndStatus(Long[] ids, String status) {

        Example example = new Example(TbItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("goodsId", Arrays.asList(ids)).andEqualTo("status",status);
        List<TbItem> itemList = itemMapper.selectByExample(example);

        return itemList;
    }

}
