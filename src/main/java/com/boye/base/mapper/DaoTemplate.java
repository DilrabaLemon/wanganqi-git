package com.boye.base.mapper;

import com.boye.base.annotation.Column;
import com.boye.base.entity.BaseEntity;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 数据访问层通用抽象类提供基本增＼删＼改＼查操作
 *
 * @param <T> 实休对象
 */
public class DaoTemplate<T extends BaseEntity> {

	private final Logger logger = LogManager.getLogger(this.getClass());

	public String insert(final T entity) {
		String sql = new SQL() {
			{
				INSERT_INTO(entity.returnTableName());
				entity.caculationColumnList();
				VALUES(entity.returnInsertColumnsName(), entity.returnInsertColumnsDefine());
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}

	public String insertAll(ParamMap<T> map) {
		@SuppressWarnings("unchecked")
		List<T> entitys = (List<T>) map.get("list");

		String sql = new SQL() {
			{
				T e = entitys.get(0);
				INSERT_INTO(e.returnTableName());
				e.caculationColumnList();
				VALUES(e.returnInsertColumnsName(), insertAllValue(entitys));
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}

	private String insertAllValue(List<T> entitys) {
		StringBuilder sb = new StringBuilder();
		String value;
		for (int i = 0; i < entitys.size(); i++) {
			T e = entitys.get(i);

			value = e.returnInsertColumnsDefine(i);

			if (i == (entitys.size() - 1))
				sb.append(value).append("");
			else {
				sb.append(value).append("),\n(");
			}
		}
		return sb.toString();
	}


	public String updateByPrimaryKey(final T entity) {
		String sql = new SQL() {
			{
				UPDATE(entity.returnTableName());
				Map<Field, Object> map = getEntityFields(entity.getClass(), entity);

				if (map.size() == 0)
					throw new NullPointerException("must update the field you want to modify");

				for (Field field : map.keySet()) {
					if (field.getAnnotation(Column.class).value().equals(""))
						SET(field.getName() + " = #{" + field.getName() + "}");
					else
						SET(field.getAnnotation(Column.class).value() + " = #{" + field.getName() + "}");
				}
				WHERE("id = #{id}");
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}


	public String deleteByPrimaryKey(final T entity) {
		String sql = new SQL() {
			{
				DELETE_FROM(entity.returnTableName());
				WHERE("id = #{id}");
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}



	private Map<Field, Object> getEntityFields(Class<?> clazz, T entity) {
		Map<Field, Object> result = null;
		try {
			result = new HashMap<>();
			for (Field field : clazz.getDeclaredFields()) {

				if (!field.isAnnotationPresent(Column.class))
					continue;

				field.setAccessible(true);

				if (field.get(entity) != null)
					result.put(field, field.get(entity));
			}
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !superClass.equals(Object.class))
				result.putAll(getEntityFields(superClass, entity));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getResultFields(List<String> list) {
		if (list.size() == 0)
			return "*";

		StringBuilder sb = new StringBuilder(" ");

		int size = list.size();
		int last = size - 1;

		for (int i = 0; i < size; i++) {
			sb.append("`").append(list.get(i)).append("`");

			if (i != last)
				sb.append(",");
			else
				sb.append(" ");
		}

		return sb.toString();
	}
}
