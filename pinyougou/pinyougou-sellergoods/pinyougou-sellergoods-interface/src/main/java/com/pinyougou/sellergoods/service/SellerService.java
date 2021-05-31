package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.vo.PageResult;

public interface SellerService extends BaseService<TbSeller> {

    void updateStatus(String sellerId, String status);

    TbSeller findOne(String username);
}