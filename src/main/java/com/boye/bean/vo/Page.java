package com.boye.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 2420937904438373203L;
	// -----------------------返回项----------------------- //
    private int current_page;// 跳转页码
    @JsonIgnore
    private int pageSize;// 单页容量
    private int max_page;// 总页数
    private int max_index;// 总记录数
    private List<T> datalist;// 数据集

    public Page(Integer start, Integer size) {
        this.current_page = start == null ? 1: start ;
        this.pageSize = size == null ? 10 : size;
    }
    
    public Page(Object start,Object size) {
        this.current_page = start == null ? 1: Integer.parseInt(start.toString());
        this.pageSize = size == null ? 10 : Integer.parseInt(size.toString());
    }
    
    public Page() {
    	this(1, 10);
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page() {
        this.max_page = (max_index % pageSize == 0) ? (max_index / pageSize) : ((max_index / pageSize) + 1);
    }

    public int getTotals() {
        return max_index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMax_index() {
        return max_index;
    }

    public void setMax_index(int max_index) {
        this.max_index = max_index;
    }

    public void setTotals(int totals) {
        this.max_index = totals;
        setMax_page();
    }

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }

	public static Page<?> nullPage() {
		Page<Object> page = new Page<Object>();
		page.setDatalist(new ArrayList<Object>());
		return page;
	}
}