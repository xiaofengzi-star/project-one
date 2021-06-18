package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.service.BaseService;

import java.util.List;

public interface ItemCatService extends BaseService<TbItemCat> {


    List<TbItemCat> findByParentId(Long parentId);
}