package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.findPage(page, rows);
    }

    @GetMapping("/findOne")
    public Goods findOneByGoodId(Long id) {
        return goodsService.findOneByGoodId(id);
    }



    /**
     * 分页查询列表
     * @param goods 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbGoods goods, @RequestParam(value = "page", defaultValue = "1")Integer page,
                             @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.search(goods, page, rows);
    }

    @GetMapping("/updateStatus")
    public Result update(Long[] ids,String status) {
        try {
            goodsService.updateStatus(ids,status);
            if ("2".equals(status)){
                //如果审核通过需要导入到索引库
                List<TbItem> itemList = goodsService.findItemListByGoodsAndStatus(ids,status);
                itemSearchService.importItemListToSolr(itemList);
            }
            return Result.ok("提交审核成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("提交审核失败");
    }
}
