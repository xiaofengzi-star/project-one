package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.service.BaseService;

public interface SellerService extends BaseService<TbSeller> {

    void updateStatus(String sellerId, String status);
}