package com.boye.common.utils;

import java.text.NumberFormat;

public class FormatUtils {
	
	private static NumberFormat numberFormat = null;
	static {
		numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
	}
	
	public static String getMoneyString(Double money) {
		if (money == null) return "0.00";
		if (numberFormat == null) return "";
		return numberFormat.format(money);
	}

}
