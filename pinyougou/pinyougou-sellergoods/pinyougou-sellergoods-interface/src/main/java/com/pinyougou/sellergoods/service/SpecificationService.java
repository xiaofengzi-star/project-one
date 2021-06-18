package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SpecificationService extends BaseService<TbSpecification> {

    //重写了add方法
    void add(Specification specification);

    //重写了update方法
    void update(Specification specification);

    Specification findOneById(Long id);

    void deleteSpecAndOps(Long[] ids);

    List<Map<String, TbSpecification>> selectSpecificationList();
}