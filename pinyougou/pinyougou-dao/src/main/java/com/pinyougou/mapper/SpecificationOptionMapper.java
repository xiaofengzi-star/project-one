package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecificationOption;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecificationOptionMapper extends Mapper<TbSpecificationOption> {

   public List<TbSpecificationOption> selectBySpecId(Long id);
}
