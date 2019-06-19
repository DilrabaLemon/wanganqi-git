package com.boye.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.boye.base.annotation.Column;
import com.boye.base.annotation.IgnoreNull;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.common.utils.DateUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;


/**
 * 抽像实体类
 *
 * 定义数据库公共字段，及相关操作方法
 * 
 *
 */
public class BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1842013486642288479L;

	protected Long id;

	@Column
	@CreatedDate
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	protected Timestamp create_time=DateUtils.currentTimestamp();

	@Column
	@LastModifiedDate
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	protected Timestamp update_time;
	
	@Column
	protected int delete_flag;

	@JsonIgnore
	@IgnoreNull
	private transient static Map<Class<? extends BaseEntity>, List<String>> COLUMN_MAP = new HashMap<>();

	public boolean paramIsNull() {
		return false;
	}
	
	private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}

	private boolean isNull(String fileName) {
		try {
			Field field = getField(this.getClass(), fileName);
			return isNull(field);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean isNull(Field field) {
		try {
			field.setAccessible(true);
			return field.get(this) == null;
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return false;
	}

	private String getColumnName(String column) {
		String fieldName = "";
		try {
			Field field = getField(this.getClass(), column);

			Column colu = field.getAnnotation(Column.class);

			if (colu.value().equals(""))
				fieldName = field.getName();
			else
				fieldName = colu.value();

			return fieldName;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return fieldName;
	}

	private String getColumnValue(String column) {
		String fieldValue = "";
		try {
			Field field = getField(this.getClass(), column);

			if (BaseEntity.class.isAssignableFrom(field.getType()))
				fieldValue = field.getName() + ".id";
			else
				fieldValue = field.getName();

			return fieldValue;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return fieldValue;
	}

	/**
	 * 获取数据库表名
	 * 
	 * @return String
	 */
	@JsonIgnore
	public String returnTableName() {
		Table table = this.getClass().getAnnotation(Table.class);
		if (table == null)
			return "`" + this.getClass().getSimpleName() + "`";
		else
			return "`" + table.value() + "`";
	}

	public void caculationColumnList() {
		if (COLUMN_MAP.get(this.getClass()) != null)
			return;

		Class<?> targetClass = this.getClass();
		List<String> columnList = caculationColumnList(targetClass);
		targetClass = targetClass.getSuperclass();

		while (targetClass != null) {
			
			List<String> superClassColumns = caculationColumnList(targetClass);
			
			//去重, 子类已经有的字段启用子类的， 隐藏超类的。
			superClassColumns.removeIf((String t)-> columnList.contains(t));
			
			columnList.addAll(superClassColumns);
			targetClass = targetClass.getSuperclass();
		}

		COLUMN_MAP.put(this.getClass(), columnList);
	}

	/**
	 * 用于计算类定义 需要POJO中的属性定义@Column(name)
	 */
	private List<String> caculationColumnList(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> columnList = new ArrayList<>(fields.length);

		for (Field field : fields) {

			if (!field.isAnnotationPresent(Column.class))
				continue;

			columnList.add(field.getName());
		}

		return columnList;

	}

	/**
	 * 用于获取Insert的字段累加
	 *
	 * @return String
	 */
	public String returnInsertColumnsName() {
		StringBuilder sb = new StringBuilder();

		List<String> list = COLUMN_MAP.get(this.getClass());

		int i = 0;
		for (String column : list) {
			boolean isNull;

			isNull = isNull(column);

			if (isNull)
				continue;

			if (i++ != 0)
				sb.append(',');
			sb.append(getColumnName(column));
		}

		String names = sb.toString();
		if (names.equals(""))
			throw new NullPointerException("entity column doesn't  the have value");
		return names;
	}

	/**
	 * 用于获取Insert的字段映射累加
	 * 
	 * @return String
	 */
	public String returnInsertColumnsDefine() {
		StringBuilder sb = new StringBuilder();

		List<String> list = COLUMN_MAP.get(this.getClass());
		int i = 0;
		for (String column : list) {
			if (isNull(column))
				continue;

			if (i++ != 0)
				sb.append(',');

			sb.append("#{").append(getColumnValue(column)).append('}');
		}
		return sb.toString();
	}

	/**
	 * 用于获取Insert的字段映射累加
	 * 
	 * @return String
	 */
	public String returnInsertColumnsDefine(int index) {
		StringBuilder sb = new StringBuilder();

		List<String> list = COLUMN_MAP.get(this.getClass());
		int i = 0;
		for (String column : list) {
			if (isNull(column))
				continue;

			if (i++ != 0)
				sb.append(',');

			sb.append("#{list[").append(index).append("].").append(getColumnValue(column)).append('}');
		}
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

	public int getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}
}
