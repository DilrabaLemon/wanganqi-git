package com.boye.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip处理工具类
 *
 */
public class IpUtils {

	/**
	 * \\d{1,3})\\. 1 - 3位 数字 :\\d{1,5}) 1-5 位数字
	 * 获取字符串中ip地址
	 * @param res
	 * @return
	 */
	public static List<String> getIps(String res) {
		List<String> ipList = new ArrayList<String>();
		Matcher m = Pattern.compile("((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\:\\d{1,5})").matcher(res);
		while (m.find()) {
			ipList.add(m.group(1));
		}
		return ipList;
	}
}
