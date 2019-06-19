package com.boye.common.utils.jzf;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private DateUtil() {
	}

	/**
	 * 格式化为"yyyyMMddHHmmss"
	 */
	public static String formatLong(Date d) {
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
		return time.format(d);
	}

	/**
	 * 格式化为"yyyyMMdd"
	 */
	public static String formatShort(Date d) {
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");
		return time.format(d);
	}

}
