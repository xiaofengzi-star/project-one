package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.service.BaseService;

import java.util.List;

public interface SpecificationOptionService extends BaseService<TbSpecificationOption> {

    List<TbSpecificationOption> findBySpecId(Long specId);
}