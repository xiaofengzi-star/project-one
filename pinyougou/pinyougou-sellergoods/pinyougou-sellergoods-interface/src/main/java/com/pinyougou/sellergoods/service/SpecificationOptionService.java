package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecificationOption;

import java.util.List;

public interface SpecificationOptionService extends BaseService<TbSpecificationOption> {

    List<TbSpecificationOption> findBySpecId(Long specId);
}