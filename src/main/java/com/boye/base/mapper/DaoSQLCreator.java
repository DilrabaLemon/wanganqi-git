package com.boye.base.mapper;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.jdbc.SQL;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.vo.QueryBean;


/**
 * SQL生成器
 * 
 * @param <T>
 * @param <PK>
 *
 * @author zyf
 */
public class DaoSQLCreator<T, PK> {

	protected final Log logger = LogFactory.getLog(getClass());

	public static final String INSERT_ALL_PARAMNAME = "list";
	
	
	public static final String PRIMARY_KEY_NAME="id";

	Map<String, Field> ENTITY_FIELDS_CACHE = new HashMap<>();

	public String insert(final T entity) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);

				INSERT_INTO(ew.getTableName());
				VALUES(ew.getColumnsString(), ew.getValuesString());
			}

		}.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(sql.replaceAll("\n", " "));
		}
		return sql;
	}
	
	/**
	 * 根据ID获取实体类集合
	 * @param entity 实体
	 * @param query 查询类
	 * @return
	 */
	public String getObjectAll(final T entity) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);
				SELECT("*");
				FROM(ew.getTableName());
				WHERE("delete_flag = 0");
			}
		}.toString();
		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	/**
	 * 根据ID获取实体类分页集合
	 * @param entity 实体
	 * @param query 查询类
	 * @return
	 */
	public String getObjectCount(final T entity, QueryBean query) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);
				SELECT("count(*)");
				FROM(ew.getTableName());
				WHERE("delete_flag = 0");
				if (query.getMain_condition() != null) WHERE(query.getMain_condition());
				if (query.getStart_time() != null) WHERE("start_time = " + query.getStart_time());
				if (query.getEnd_time() != null) WHERE("end_time = " + query.getEnd_time());
				if (query.getShop_phone() != null) WHERE("login_number like '%" + query.getShop_phone() + "%'");
				if (query.getMin_money() != null) WHERE("money &gt;= " + query.getMin_money());
				if (query.getMax_money() != null) WHERE("money &lt;= " + query.getMax_money());
			}
		}.toString();
		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	/**
	 * 根据ID获取实体类分页集合
	 * @param entity 实体
	 * @param query 查询类
	 * @return
	 */
	public String getObjectByPage(final T entity, QueryBean query) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);
				SELECT("*");
				FROM(ew.getTableName());
				WHERE("delete_flag = 0");
				if (query.getMain_condition() != null) WHERE(query.getMain_condition());
				if (query.getStart_time() != null) WHERE("start_time = " + query.getStart_time());
				if (query.getEnd_time() != null) WHERE("end_time = " + query.getEnd_time());
				if (query.getShop_phone() != null) WHERE("login_number like '%" + query.getShop_phone() + "%'");
				if (query.getMin_money() != null) WHERE("money &gt;= " + query.getMin_money());
				if (query.getMax_money() != null) WHERE("money &lt;= " + query.getMax_money());
				
			}
		}.toString();
		sql = sql + " limit " + query.getStart() + "," + query.getPage_size();
		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	/**
	 * 根据ID获取实体类
	 * @param entity 实体
	 * @param tableName 表名
	 * @return
	 */
	public String getObjectById(final T entity) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);

				SELECT("*");
				FROM(ew.getTableName());
				WHERE("id = #{id}");
				WHERE("delete_flag = 0");
			}
		}.toString();
		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	
	/**
	 * 插入指定表的书
	 * @param entity 实体
	 * @param tableName 表名
	 * @return
	 */
	public String insertTable(final T entity,String tableName) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);

				INSERT_INTO(tableName);
				VALUES(ew.getColumnsString(), ew.getTableValuesString());
			}

		}.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(sql.replaceAll("\n", " "));
		}
		return sql;
	}

	/**
	 * 批量插入SQL生成
	 * 
	 * @param entitys
	 * @return
	 */
	public String insertAll(ParamMap<?> map) {

		@SuppressWarnings("unchecked")
		List<T> entitys = (List<T>) map.get(INSERT_ALL_PARAMNAME);

		String sql = new SQL() {
			{
				T e = entitys.get(0);
				EntityWapper<T> ew = new EntityWapper<>(e);

				INSERT_INTO(ew.getTableName());
				VALUES(ew.getColumnsString(), ew.insertAllValue(entitys));

			}

		}.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(sql.replaceAll("\n", " "));
		}
		return sql;

	}
	
	
	
	
	/**
	 * 批量插入SQL生成
	 * 
	 * @param entitys
	 * @return
	 */
	public String insertTableAll(ParamMap<?> map,String tableName) {

		@SuppressWarnings("unchecked")
		List<T> entitys = (List<T>) map.get(INSERT_ALL_PARAMNAME);

		String sql = new SQL() {
			{
				T e = entitys.get(0);
				EntityWapper<T> ew = new EntityWapper<>(e);

				INSERT_INTO(tableName);
//				ew.getTableValuesString()
				VALUES(ew.getColumnsString(), ew.insertAllValue(entitys));

			}

		}.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(sql.replaceAll("\n", " "));
		}
		return sql;

	}

	

	/**
	 * 按主键更新实体
	 * <p>
	 * 只对不为空的字段更新
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public String updateByPK(final T entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity) entity;
			baseEntity.setUpdate_time(null);
		}
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);

				UPDATE(ew.getTableName());
				Map<Field, Object> map = ew.getEntityFields(entity.getClass(),false);

				if (map.size() == 0)
					throw new NullPointerException("must update the field you want to modify");

				for (Field field : map.keySet()) {
					if (null!=map.get(field)) {
						SET(ew.getColumnName(field) + " = #{" + field.getName() + "}");
					}
				}
				WHERE("id = #{id}");
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	
	
	/**
	 * 按主键更新指定表实体
	 * <p>
	 * 只对不为空的字段更新
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public String updateTableByPK(final T entity,String tableName) {
		String sql = new SQL() {
			{
				EntityWapper<T> ew = new EntityWapper<>(entity);

				UPDATE(tableName);
				Map<Field, Object> map = ew.getEntityFields(entity.getClass(),true);
				if (map.size() == 0) {
					throw new NullPointerException("must update the field you want to modify");
				}
				Object id=null;	
				for (Field field : map.keySet()) {
					//只有 不为空的数据才更新
					if (field.getName().equals(PRIMARY_KEY_NAME)) {
						id=map.get(field);
					}else {
						if (null!=map.get(field)) {
							SET(ew.getColumnName(field) + " = " +addOpenClose(String.valueOf(map.get(field)),"'", "'")+ "");
						}
					}
				}
				WHERE("id ="+id);
			}
		}.toString();

		logger.debug(sql.replaceAll("\n", " "));
		return sql;
	}
	
	
	
	private static  String addOpenClose(String keyword, String open, String close) {

		StringBuilder builder = new StringBuilder();

		builder.append(open).append(keyword).append(close);

		return builder.toString();
	}

	private static class EntityWapper<T> {

		protected final Log logger = LogFactory.getLog(getClass());

		// private transient static Map<Class<? extends BaseEntity>, List<String>>
		// COLUMN_MAP = new HashMap<>();

		private T entity;
		
		private Map<Field, Object> fields;

		private List<String> ignoreFields = new ArrayList<>();

		/**
		 * @param entity
		 */
		public EntityWapper(T entity) {

			this.entity = entity;

			this.fields = getEntityFields(entity.getClass(),false);

		}


		/**
		 * 获取实体类表名
		 * 
		 * @return
		 */
		private String getTableName() {

			StringBuilder builder = new StringBuilder();

			Table table = entity.getClass().getAnnotation(Table.class);
			if (table == null)
				builder.append('`').append(entity.getClass().getSimpleName()).append('`');
			else
				builder.append('`').append(table.value()).append('`');

			return builder.toString();
		}



		/**
		 * 用于获取Insert的字段映射累加
		 * 
		 * <p>用于支持批量插入
		 * 
		 * @return String
		 */
		private String returnInsertColumnsDefine(int index) {
			StringBuilder sb = new StringBuilder();

			int i = 0;
			for (Field column : fields.keySet()) {

				if (i++ != 0)
					sb.append(',');

				sb.append("#{list[").append(index).append("].").append(getColumnName(column)).append('}');
			}
			return sb.toString();
		}

		/**
		 * 获取列字符串
		 * 
		 *            是否忽略空值字段
		 * @return
		 */
		public String getColumnsString() {

			List<String> columns = new ArrayList<>();

			for (Map.Entry<Field, Object> item : fields.entrySet()) {
				
				columns.add(addOpenClose(getColumnName(item.getKey()), "`", "`"));
			}

			String strColumns = StringUtils.join(columns, ',');

			return strColumns;
		}

		/**
		 * 获取值字符串
		 * @return
		 */
		public String getValuesString() {

			List<String> values = new ArrayList<>();

			for (Map.Entry<Field, Object> item : fields.entrySet()) {

				if (item.getValue() != null) {

					values.add(addOpenClose(item.getKey().getName(), "#{", "}"));
				}
			}

			String strValues = StringUtils.join(values, ',');

			return strValues;
		}
		
		
		/**
		 * 获取值
		 * @return
		 */
		public String getTableValuesString() {
			
			List<String> values = new ArrayList<>();

			for (Map.Entry<Field, Object> item : fields.entrySet()) {

				if (item.getValue() != null) {
					values.add(addOpenClose(String.valueOf(item.getValue()),"'", "'"));
//					values.add(addOpenClose(item.getKey().getName(), "#{", "}"));
				}
			}
			String strValues = StringUtils.join(values, ',');
			return strValues;
		}
		
		/**
		 * 获取批量插入值字符串
		 * @param entitys
		 * @return
		 */
		private String insertAllValue(List<T> entitys) {
			StringBuilder sb = new StringBuilder();
			String value;
			for (int i = 0; i < entitys.size(); i++) {

				value = returnInsertColumnsDefine(i);

				if (i == (entitys.size() - 1))
					sb.append(value).append("");
				else {
					sb.append(value).append("),\n(");
				}
			}
			return sb.toString();
		}

		

		/**
		 * 获取数据库列名
		 * 
		 * @param field
		 * @return
		 */
		private String getColumnName(Field field) {

			Column an = field.getAnnotation(Column.class);

			if (StringUtils.isEmpty(an.value())) {
				return field.getName();
			} else {
				return field.getAnnotation(Column.class).value();
			}
		}

		/**
		 * 获取实体字段关联
		 * 
		 * @param clazz
		 *            是否忽略空值
		 * @return
		 */
		private Map<Field, Object> getEntityFields(Class<?> clazz,Boolean primaryKey) {

			Map<Field, Object> result = null;
			try {
				result = new HashMap<>();
				for (Field field : clazz.getDeclaredFields()) {

					field.setAccessible(true);
					
					if (!field.isAnnotationPresent(Column.class)) {
						if (primaryKey&&field.getName().equals(PRIMARY_KEY_NAME)) {
							result.put(field, field.get(entity));
						}
						continue;
					}
						
					Column[] an = field.getAnnotationsByType(Column.class);
					if (an[0].ignore()) {		//注解中忽略配置实现
						ignoreFields.add(field.getName());
						continue;
					}
					if (field.get(entity) == null && isIgnoreNull(field)) {	//注解中忽略空值配置实现	
						ignoreFields.add(field.getName());
						continue;
					}

					if (ignoreFields.contains(field.getName())) {
						continue;
					}

					result.put(field, field.get(entity));
				}
				Class<?> superClass = clazz.getSuperclass();
				if (superClass != null && !superClass.equals(Object.class))
					result.putAll(getEntityFields(superClass,primaryKey));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("组装SQL失败", e);
			}
			return result;
		}
		
		/**
		 * 处理空值忽略配置
		 * @param field		字段对象
		 * @return
		 * @throws IllegalAccessException 
		 * @throws IllegalArgumentException 
		 */
		private boolean isIgnoreNull(Field field) throws IllegalArgumentException, IllegalAccessException {
			
			Column[] an = field.getAnnotationsByType(Column.class);

			boolean ignoreNull = an[0].ignoreNull();
			
			Object firstValue = field.get(entity);
			
			if (ignoreNull && firstValue==null) {
				return true;
			} else {
				return false;
			}
			
		}

	}
}
