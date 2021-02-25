package com.pinyougou.pojo;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_seckillOrder_logistics")
public class TbSeckillOrderLogistics implements Serializable {

    private Long seckillOrderId;

    private String logisticsId;

    private String logisticsName;

    public Long getSeckillOrderId() {
        return seckillOrderId;
    }

    public void setSeckillOrderId(Long seckillOrderId) {
        this.seckillOrderId = seckillOrderId;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }
}
