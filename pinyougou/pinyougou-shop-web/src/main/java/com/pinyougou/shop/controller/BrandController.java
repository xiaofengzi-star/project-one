package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
@RestController
public class BrandController {

    /**
     * 从注册中心获取该对象；在配置文件中已经指定了注册中心
     */
    @Reference
    private BrandService brandService;

    /**
     * 选择下拉品牌列表
     * @return List<Map<String,TbBrand>>
     */
    @GetMapping("/findBrandList")
    public List<TbBrand> findBrandList(){
        return brandService.findBrandList();
    }
}
