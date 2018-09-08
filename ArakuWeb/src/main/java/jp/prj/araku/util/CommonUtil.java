package jp.prj.araku.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
	public static final String SEARCH_TYPE_SRCH = "srch";
	
	public static final String SPLIT_BY_TAB = "	";
	public static final String SPLIT_BY_STAR = "\\*";
	public static final String SPLIT_BY_NPER = "\n";
	public static final String SPLIT_BY_COLON = ":";
	public static final String JPCOMMA = "、";
	
	public static final String TRANS_TARGET_R = "R"; /*R:楽天*/
	public static final String TRANS_TARGET_A = "A"; /*A:アマゾン*/
	
	public static final String ORDER_STATUS_COMPLETE = "出荷準備済";
	public static final String TOMORROW_MORNING = "午前中";
	
	public static String getStartDate() {
		Calendar cal = Calendar.getInstance( );
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date today = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(today) + " 00:00:00";
	}
	
	public static String getDate(String format, int day) { 
		Calendar cal = Calendar.getInstance( ); 
		cal.add(Calendar.DAY_OF_MONTH, day);
		Date today = cal.getTime(); 
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		return sdf.format(today); 
	}
}
