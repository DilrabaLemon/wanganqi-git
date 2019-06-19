package com.boye.base.mapper;

import java.util.HashMap;

/**
 * 查询参数对象
 * 
 * <P>
 * 可以直接使用QueryPageContext作为参数与MYBATIS交互
 * </P>
 * 
 */
public class QueryPage {

	public static final Integer DEFAULT_PAGE_SIZE = 15;
	public static final Integer DEFAULT_PAGE_NO = 1;

	private int offset;
	private int rows;
	private Integer pageNo;
	private Integer pageSize;

	private QueryPageContext context = new QueryPageContext();

	private QueryPage() {
		super();
	}

	public static QueryPage build(Integer pageNo, Integer pageSize) {
		QueryPage page = new QueryPage();

		page.limit(pageNo, pageSize);
		return page;
	}

	/**
	 * 构建非分页的查询对象
	 * 
	 * @return
	 */
	public static QueryPage build() {

		QueryPage page = new QueryPage();
		return page;

	}

	/**
	 * 根据{pageNo, pageSize}, 增加对{rows,offset}, {firstResult, maxResults}的支持
	 * 
	 * @param pageNo
	 * @param pageSize
	 */
	private void limit(Integer pageNo, Integer pageSize) {
		if (pageSize == null || pageSize < 1) {
			this.rows = DEFAULT_PAGE_SIZE;
			this.pageSize = DEFAULT_PAGE_SIZE;
		} else {
			this.rows = pageSize;
			this.pageSize = pageSize;
		}
		if (pageNo == null || pageNo < 1) {
			this.pageNo = DEFAULT_PAGE_NO;
			this.offset = 0;
		} else {
			this.pageNo = pageNo;
		}
		this.offset = (this.pageNo - 1) * rows;

		context.put("rows", this.rows);
		context.put("offset", this.offset);
		context.put("pageNo", this.pageNo);
		context.put("pageSize", this.pageSize);
		context.put("firstResult", this.offset);
		context.put("maxResults", this.rows);

	}

	public int getOffset() {
		return offset;
	}

	public int getRows() {
		return rows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public QueryPageContext set(String key, Object value) {
		context.put(key, value);
		return context;
	}

	/**
	 * 获取参数值
	 * 
	 * @param key
	 *            参数键
	 * @return
	 */
	public Object get(String key) {
		return context.get(key);
	}

	/**
	 * 获取查询参数上下文对象
	 * <p>
	 * 建议直接以QueryPageContext为参数与mybatis交互
	 * </p>
	 * 
	 * @return the context
	 */
	public QueryPageContext getContext() {
		return context;
	}

	public class QueryPageContext extends HashMap<String, Object> {

		private static final long serialVersionUID = 1L;

	}

}
