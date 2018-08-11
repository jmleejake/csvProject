package jp.prj.araku.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
	public static String getStartDate() {
		Calendar cal = Calendar.getInstance( );
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date today = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(today) + " 00:00:00";
	}
}
