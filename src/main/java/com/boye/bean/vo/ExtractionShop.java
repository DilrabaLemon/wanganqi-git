package com.boye.bean.vo;

import com.boye.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Objects;

public class ExtractionShop extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -2530527410569384500L;

	private String start_time;

    private String end_time;

    private String shop_phone;

    private Integer page_size;

    private Integer page_index;

    public ExtractionShop() {
    }

    public Integer getIndexSet(){
        return (page_index-1)*page_size;
    }

    public Integer getOffSet(){
        return page_index*page_size;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getPage_index() {
        return page_index;
    }

    public void setPage_index(Integer page_index) {
        this.page_index = page_index;
    }

    @Override
    public String toString() {
        return "ExtractionShop{" +
                "start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", shop_phone='" + shop_phone + '\'' +
                ", page_size=" + page_size +
                ", page_index=" + page_index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtractionShop)) return false;
        ExtractionShop that = (ExtractionShop) o;
        return Objects.equals(getStart_time(), that.getStart_time()) &&
                Objects.equals(getEnd_time(), that.getEnd_time()) &&
                Objects.equals(getShop_phone(), that.getShop_phone()) &&
                Objects.equals(getPage_size(), that.getPage_size()) &&
                Objects.equals(getPage_index(), that.getPage_index());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStart_time(), getEnd_time(), getShop_phone(), getPage_size(), getPage_index());
    }
}
