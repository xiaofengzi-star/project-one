package com.pinyougou.pojo;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_logistics")
public class TbLogistics implements Serializable {


    private Long orderId;

    private String logisticsId;

    private String logisticsName;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
