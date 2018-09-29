package jp.prj.araku.util;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CommonUtil {
	public static final String SEARCH_TYPE_SRCH = "srch";
	
	public static final String SPLIT_BY_TAB = "	";
	public static final String SPLIT_BY_STAR = "\\*";
	public static final String SPLIT_BY_NPER = "\n";
	public static final String SPLIT_BY_COLON = ":";
	public static final String JPCOMMA = "、";
	
	public static final String TRANS_TARGET_R = "R"; /*R:楽天*/
	public static final String TRANS_TARGET_A = "A"; /*A:アマゾン*/
	public static final String TRANS_ERR = "ERR"; /*エラーテキスト*/
	
	public static final String ORDER_STATUS_COMPLETE = "出荷準備済";
	public static final String TOMORROW_MORNING = "午前中";
	public static final String YA_TOMORROW_MORNING_CODE = "0812";
	public static final String SA_TOMORROW_MORNING_CODE = "01";
	public static final String TITLE_SAMA = "様";
	public static final String INVOICE_TYPE_0 = "0";
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void executeCSVDownload(
			CSVWriter csvWriter
			, BufferedWriter writer
			, String[] header
			, ArrayList<ArakuVO> list) 
					throws CsvDataTypeMismatchException
					, CsvRequiredFieldEmptyException {
		StatefulBeanToCsv<ArakuVO> beanToCSV = new StatefulBeanToCsvBuilder(writer)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .build();
		
		csvWriter.writeNext(header);
		
		beanToCSV.write(list);
	}
}
