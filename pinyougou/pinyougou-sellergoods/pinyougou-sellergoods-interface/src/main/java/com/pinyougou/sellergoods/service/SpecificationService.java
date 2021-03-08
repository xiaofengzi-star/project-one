package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellergoods.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface SpecificationService extends BaseService<TbSpecification> {

    void save(Specification specification);
}