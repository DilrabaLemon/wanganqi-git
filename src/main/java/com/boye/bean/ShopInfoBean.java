package com.boye.bean;

import com.boye.bean.entity.ShopConfig;

import java.sql.Timestamp;
import java.util.List;

public class ShopInfoBean {

    private String id;
    private String shop_name;
    private Double total_turnover;
    private List<ShopConfig> shopConfigs;//alipay_rote, weixin_rote
    private int shop_category;
    private int shop_type;
    private int examine;
    private Timestamp create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public List<ShopConfig> getShopConfigs() {
        return shopConfigs;
    }

    public void setShopConfigs(List<ShopConfig> shopConfigs) {
        this.shopConfigs = shopConfigs;
    }

    public Double getTotal_turnover() {
        return total_turnover;
    }

    public void setTotal_turnover(Double total_turnover) {
        this.total_turnover = total_turnover;
    }

    public int getShop_category() {
        return shop_category;
    }

    public void setShop_category(int shop_category) {
        this.shop_category = shop_category;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public int getExamine() {
        return examine;
    }

    public void setExamine(int examine) {
        this.examine = examine;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
