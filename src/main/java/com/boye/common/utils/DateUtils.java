package com.boye.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	
	/** 一分钟的秒数 */
	public static final Integer ONE_MINUTE_SECONDS  = 60;
	
	/** 一小时的秒数 */
	public static final Integer ONE_HOUR_SECONDS = 60*ONE_MINUTE_SECONDS  ;
	
	/** 一天的秒数 */
	public static final Integer ONE_DAY_SECONDS = 24*ONE_HOUR_SECONDS;
	
	/** 一秒等于多少毫秒 */
	public static final Integer ONE_SECOND_MILLISECOND = 1000;
	
	/**
	 * GMT+8
	 */
	public static final String TIME_ZONE = "GMT+8";
	
	
	
	/**============日期格式化==================*/
	public static interface DateFormatType{
		/** yyyy-MM-dd HH:mm:ss */
		public static final String YYYYMMDDHHmmss ="yyyy-MM-dd HH:mm:ss";
		public static final String YYYY="YYYY";
		public static final String YYYYMMDDHHmmssSSS ="yyyyMMddHHmmssSSS";
		public static final String DATE_FORMAT_TWO = "yyyy-MM-dd";
	}
	
	
	/**
	 * 获取当前日期，不含当前时间
	 * 
	 * @return Date
	 */
	public static Date currentDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	 /* 获取当前日期，不含当前时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp currentTimestamp() {
		 Timestamp ts = new Timestamp(System.currentTimeMillis());
		return ts;
	}
	
	
	
	
	
	/**
	 * 时间格式化
	 * 
	 * @param date
	 *            需要格式化的时间
	 * @param patter
	 *            需要格式化的格式
	 * 
	 **/
	public static String dateToString(Date date, String patter) {

		SimpleDateFormat mat = new SimpleDateFormat();
		mat.applyPattern(patter);
		mat.setLenient(false);

		return mat.format(date);

	}
	
	
}
