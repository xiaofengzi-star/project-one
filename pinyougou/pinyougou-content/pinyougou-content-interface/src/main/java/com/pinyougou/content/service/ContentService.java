package com.pinyougou.content.service;

import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.BaseService;

import java.util.List;

public interface ContentService extends BaseService<TbContent> {

    List<TbContent> findContentListByCategoryId(Long id);
}