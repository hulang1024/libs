package util;

import java.util.Calendar;

public class DateTimeUtils {
	/**
	 * @return 日期的8位数字yyyymmdd
	 */
	public static int toDateNumber(Calendar cal) {
		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
	}
}
