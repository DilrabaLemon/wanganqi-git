package com.boye.bean.vo;

import com.boye.base.entity.BaseEntity;

import java.io.Serializable;

public class PageBean extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2258133256542418960L;

	private Integer page_size;

    private Integer page_index;

//    private Integer record_type;

    public PageBean() {
    }

    public Integer getIndexSet(){
        return (page_index-1)*page_size;
    }

    public Integer getOffSet(){
        return page_index*page_size;
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

//    public Integer getRecord_type() {
//        return record_type;
//    }
//
//    public void setRecord_type(Integer record_type) {
//        this.record_type = record_type;
//    }

    @Override
    public String toString() {
        return "PageBean{" +
                "page_size=" + page_size +
                ", page_index=" + page_index +
//                ", record_type=" + record_type +
                '}';
    }
}
