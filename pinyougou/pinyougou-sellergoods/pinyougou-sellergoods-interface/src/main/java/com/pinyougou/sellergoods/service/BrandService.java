package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService extends BaseService<TbBrand> {
    public List<TbBrand> queryAll();

    public List<TbBrand> testPage(int page, int rows);

    List<Map<String, TbBrand>> selectBrandList();
}
