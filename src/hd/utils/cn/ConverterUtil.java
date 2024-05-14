package hd.utils.cn;

import hd.produce.security.cn.annotation.Except;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;

/**
 * 转换工具类
 * 
 * @author XuDL
 */
public final class ConverterUtil {
	/** 数字. */
	public static final String DATA_TYPE_NUMBER = "U";

	/** 文字. */
	public static final String DATA_TYPE_CHAR = "C";

	/** 补全类别：SAME. */
	public static final String COMPLEMENT_SAME = "SAME";

	/** 分隔符：元素 */
	public static final String SEPARATOR_ELEMENT = "#EM#";

	/** 分隔符：key-value */
	public static final String SEPARATOR_KEY_VALUE = "#KV#";
	
	/** Excel导出单元格属性：title */
	public static final String EXCEL_TITLE = "title";
	
	/** Excel导出单元格属性：width */
	public static final String EXCEL_WIDTH = "width";
	
	/** Excel导出单元格宽度：最小值 */
	public static final int EXCEL_WIDTH_MIN = 10;
	
	/** Excel导出单元格宽度：最大值 */
	public static final int EXCEL_WIDTH_MAX = 50;
	
	/** Excel导出单元格WIDTH：auto */
	public static final String EXCEL_WIDTH_AUTO = "auto";

	/** 时间格式：日期型(yyyy/MM/dd) */
	public static final String FORMATE_DATE = "yyyy/MM/dd";

	/** 时间格式：日期时间24小时制型(yyyy/MM/dd HH:mm:ss) */
	public static final String FORMATE_DATE_TIME_24H = "yyyy/MM/dd HH:mm:ss";

	/** 时间格式：日期时间12小时制型(yyyy/MM/dd hh:mm:ss) */
	public static final String FORMATE_DATE_TIME_12H = "yyyy/MM/dd hh:mm:ss";

	/** 时间格式：时间戳24小时制型(yyyy/MM/dd HH:mm:ss.SSS) */
	public static final String FORMATE_TIME_STAMP_24H = "yyyy/MM/dd HH:mm:ss.SSS";

	/** 时间格式：时间戳12小时制型(yyyy/MM/dd hh:mm:ss.SSS) */
	public static final String FORMATE_TIME_STAMP_12H = "yyyy/MM/dd hh:mm:ss.SSS";
	
	/** 正则日期类型:中线 */
	public static final String REGEX_DATE_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])$";
	
	/** 正则日期类型:反斜线 */
	public static final String REGEX_DATE_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])$";
	
	/** 正则日期时间类型:中线 */
	public static final String REGEX_DATE_TIME_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])$";
	
	/** 正则日期时间类型:反斜线 */
	public static final String REGEX_DATE_TIME_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])$";

	/** 正则时间戳类型:中线 */
	public static final String REGEX_TIME_STAMP_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])\\.\\d{3}$";
	
	/** 正则时间戳类型:反斜线 */
	public static final String REGEX_TIME_STAMP_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])\\.\\d{3}$";

	/** 方法名:get */
	public static final String METHOD_GET = "get";
	
	/** 方法名:set */
	public static final String METHOD_SET = "set";
	
	/** 字符集编码:UTF-8 */
	public static final String CHATSET_UTF8 = "UTF-8";
	
	/** 字符集编码:ISO-8859-1 */
	public static final String CHATSET_ISO88591 = "ISO-8859-1";
	
	/**
	 * 构造函数
	 */
	private ConverterUtil() {
	}
	
	/**
	 * Date->String转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param dt
	 *            日期
	 * @param formate
	 *            变换形式
	 * @return String数值
	 * 
	 */
	public static String toDateStr(Date dt, String formate) {
		if (dt == null) {
			return null;
		}

		DateFormat df = new SimpleDateFormat(formate);
		return df.format(dt);
	}
	
	/**
	 * 取得String的日期
	 * 
	 * @param formate
	 * @return
	 */
	public static String getDateStr(String formate) {
		return toDateStr(new Date(), formate);
	}

	/**
	 * Date->yyyy/MM/dd HH:mm型的String转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param dt
	 *            日期
	 * @return String数值
	 * 
	 */
	public static String toDateTimeString(Date dt) {
		if (dt == null) {
			return null;
		}

		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return df.format(dt);
	}

	/**
	 * Object->Timestamp型的String转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return String数值
	 * 
	 */
	public static String toTimestampString(Object obj) {
		if (obj == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSSSSS");

		if (obj instanceof java.util.Date) {
			return df.format(new Timestamp(((java.util.Date) obj).getTime()));
		}
		if (obj instanceof Timestamp) {
			return df.format((Timestamp) obj);
		}
		return obj.toString();
	}
	
	/**
	 * Long->Date转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param date
	 *            date
	 * @return Date数值
	 */
	public static Date toDate(Long date) {
		if (date == null) {
			return null;
		}
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(date);
		return cl.getTime();
	}

	/**
	 * Object->Date转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return Date数值
	 */
	public static Date toDate(Object obj, String formate) {
		if ((obj == null) || ("".equals(obj))) {
			return null;
		}
		if (obj instanceof Long) {
			return toDate(toLong(obj));
		}
		if (obj instanceof Timestamp) {
			return (Timestamp) obj;
		}
		if (obj instanceof java.sql.Timestamp) {
			return (java.sql.Timestamp) obj;
		}
		if (obj instanceof Date) {
			return new Timestamp(((Date) obj).getTime());
		}
		if (obj instanceof String) {
			String temp = obj.toString();
			if (temp.length() == 8) {
				char[] arra = temp.toCharArray();
				String temp1 = new String(arra, 0, 4);
				String temp2 = new String(arra, 4, 2);
				String temp3 = new String(arra, 6, 2);
				obj = temp1 + "/" + temp2 + "/" + temp3;
			}
		}
		DateFormat df = new SimpleDateFormat(formate);
		Date dateTemp = null;
		try {
			dateTemp = df.parse(obj.toString());
		} catch (ParseException e) {
			throw new RuntimeException("日期不合法", e);
		}
		return new Timestamp(dateTemp.getTime());

	}

	/**
	 * Object->Timestamp转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return Timestamp数值
	 * 
	 */
	public static Timestamp toTimestamp(Object obj) {
		Date date = toDate(obj, "yyyy/MM/dd HH:mm:ss.SSSSSSSSS");
		return date == null ? null : new Timestamp(date.getTime());
	}
	
	   /**
     * Object->Timestamp转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Timestamp数值
     * 
     */
    public static Timestamp toTimestamp(Object obj, String format) {
        Date date = toDate(obj, format);
        return date == null ? null : new Timestamp(date.getTime());
    }

	/**
	 * Object->Integer转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return Integer数值
	 */
	public static Integer toInteger(Object obj) {
		return toInteger(obj, null);
	}

	/**
	 * Object->Integer转换
	 * <p>
	 * 入参是null时，返回值为nullValue
	 * 
	 * @param obj
	 *            Object
	 * @return Integer数值
	 */
	public static Integer toInteger(Object obj, Integer nullValue) {
		if (obj instanceof Double) {
			return ((Double) obj).intValue();
		}
		return obj == null ? nullValue : Integer.valueOf(obj.toString());
	}

	/**
	 * Object->BigDecimal转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return BigDecimal数值
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		return toBigDecimal(obj, null);
	}

	/**
	 * Object->BigDecimal转换
	 * <p>
	 * 入参是null时，返回值为nullValue
	 * 
	 * @param obj
	 *            Object
	 * @return BigDecimal数值
	 */
	public static BigDecimal toBigDecimal(Object obj, BigDecimal nullValue) {
		if ((obj == null) || (obj.equals(""))) {
			return nullValue;
		}
		return new BigDecimal(obj.toString());
	}

	/**
	 * Object->String转换
	 * <p>
	 * 入参是null时，返回值为""
	 * 
	 * @param obj
	 *            Object
	 * @return String
	 * 
	 */
	public static String toNotNullString(Object obj) {
		return obj == null ? "" : toString(obj);
	}
	
	/**
	 * Object->String转换
	 * <p>
	 * 入参是null时，返回值为""
	 * 
	 * @param obj
	 *            Object
	 * @return String
	 * 
	 */
	public static String toNotNullString(Object obj, String nullVal) {
		return isEmpty(obj) ? nullVal : toString(obj);
	}

	/**
	 * Object->String转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return String
	 * 
	 */
	public static String toString(Object obj) {
		if (null == obj || "".equals(obj)) {
			return null;
		}
		if (obj instanceof String) {
			return obj.toString();
		}
		if (obj instanceof Integer) {
			return Integer.toString(toInteger(obj));
		}
		if (obj instanceof Double) {
			return Double.toString(toDouble(obj));
		}
		if (obj instanceof Long) {
			return Long.toString(toLong(obj));
		}
		if (obj instanceof BigDecimal) {
			return toBigDecimal(obj).toPlainString();
		}
		if (obj instanceof Timestamp) {
			return dateToString((Date) obj, FORMATE_TIME_STAMP_24H);
		}
		if (obj instanceof Date) {
			return dateToString((Date) obj, FORMATE_DATE_TIME_24H);
		}
		if (obj instanceof Object[]) {
			Object[] objArray = (Object[]) obj;
			return String.valueOf(objArray[0]);
		}
		if (obj instanceof Clob) {

			String reString = "";
			Reader is;
			try {
				is = ((Clob) obj).getCharacterStream();
				// 得到流
				BufferedReader br = new BufferedReader(is);
				String s = br.readLine();
				StringBuffer sb = new StringBuffer();
				// 执行循环将字符串全部取出付值给s
				while (s != null) {
					// StringBuffer由StringBuffer转成STRING
					sb.append(s);
					s = br.readLine();
				}
				reString = sb.toString();
			} catch (Exception e) {
				return null;
			}
			return reString;
		}
		return String.valueOf(obj);
	}
	
	/**
	 * Object->Boolean转换
	 * <p>
	 * flase<String> --> false<boolean>
	 * TRUE<String> --> true<boolean>
	 * 0<String> --> false<boolean>
	 * 1<String> --> true<boolean>
	 * @param Obj
	 * @return
	 */
	public static Boolean toBoolean(Object Obj){
		if(Obj instanceof Boolean){
			return ((Boolean) Obj).booleanValue();
		}
		String boo = toNotNullString(Obj);
		if("true".equalsIgnoreCase(boo)){
			return true;
		}
		if("false".equalsIgnoreCase(boo)){
			return false;
		}
		if("0".equalsIgnoreCase(boo)){
			return false;
		}
		if("1".equalsIgnoreCase(boo)){
			return true;
		}
		return false;
	}

	// /**
	// * Timestamp转字符串
	// *
	// * @param ts
	// * Timestamp
	// * @param format
	// * 转换格式
	// * @return 字符串
	// */
	// public static String timestampToString(Timestamp ts, String format) {
	// SimpleDateFormat sf = new SimpleDateFormat(format);
	// Calendar cl = Calendar.getInstance();
	// cl.setTimeInMillis(ts.getTime());
	// return sf.format(cl.getTime());
	// }

	/**
	 * 日期->字符串转换
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            转换格式
	 * @return 字符串
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	
	/**
	 * 字符串->日期转换
	 * <p>
	 * 支持yyyy/MM/dd|yyyy/MM/dd HH:mm.ss|yyyy/MM/dd HH:mm.SSS|
	 * yyyy-MM-dd|yyyy-MM-dd HH:mm.ss|yyyy-MM-dd HH:mm.SSS
	 * 的转换。如果不在这六种格式之内，返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 字符串
	 * @throws ParseException
	 */
	public static Date toDate(String str) throws ParseException {
		// 设置日期正则格式
		Map<String, String> regexMap = new HashMap<String, String>();
		regexMap.put("REGEX_DATE_MIDDELLINE", REGEX_DATE_MIDDELLINE);
		regexMap.put("REGEX_DATE_BACKSLASH", REGEX_DATE_BACKSLASH);
		regexMap.put("REGEX_DATE_TIME_MIDDELLINE", REGEX_DATE_TIME_MIDDELLINE);
		regexMap.put("REGEX_DATE_TIME_BACKSLASH", REGEX_DATE_TIME_BACKSLASH);
		regexMap.put("REGEX_TIME_STAMP_MIDDELLINE", REGEX_TIME_STAMP_MIDDELLINE);
		regexMap.put("REGEX_TIME_STAMP_BACKSLASH", REGEX_TIME_STAMP_BACKSLASH);

		regexMap = new HashMap<String, String>();
		SimpleDateFormat sf = null;
		for (String regex : regexMap.keySet()) {
			Pattern pat = Pattern.compile(regex);
			Matcher mat = pat.matcher(str);
			if (mat.find()) {
				if (regex.contains("TIME_STAMP")) {
					sf = new SimpleDateFormat(FORMATE_TIME_STAMP_24H);
				} else if (regex.contains("DATE_TIME")) {
					sf = new SimpleDateFormat(FORMATE_DATE_TIME_24H);
				} else if (regex.contains("DATE")) {
					sf = new SimpleDateFormat(FORMATE_DATE);
				}
				break;
			}
		}
		if (null == sf) {
			return null;
		}
		return sf.parse(str);
	}

	/**
	 * 
	 * Object->Double转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return Double型数值
	 * 
	 */
	public static Double toDouble(Object obj) {
		return toDouble(obj, null);
	}

	/**
	 * 
	 * Object->Double转换
	 * <p>
	 * 入参是null时，返回值为nullValue
	 * 
	 * @param obj
	 *            Object
	 * @return Double型数值
	 * 
	 */
	public static Double toDouble(Object obj, Double nullValue) {
		if ((obj == null) || (obj.equals(""))) {
			return nullValue;
		}
		return new Double(obj.toString());
	}
	
	/**
	 * 
	 * Object->Long转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param obj
	 *            Object
	 * @return Long型数值
	 * 
	 */
	public static Long toLong(Object obj) {
		return toLong(obj, null);
	}
	
	/**
	 * 
	 * Object->Long转换
	 * <p>
	 * 入参是null时，返回值为nullValue
	 * 
	 * @param obj
	 *            Object
	 * @return Long型数值
	 * 
	 */
	public static Long toLong(Object obj, Long nullValue) {
		if ((obj == null) || (obj.equals(""))) {
			return nullValue;
		}
		if(obj instanceof Long){
			return Long.valueOf(obj.toString());
		}
		if(obj instanceof Double){
			return toDouble(obj).longValue();
		}
		if(obj instanceof BigDecimal){
			return toBigDecimal(obj).longValue();
		}
		if (obj instanceof Date) {
			return toDate(obj, null).getTime();
		}
		return new Long(obj.toString());
	}

	/**
	 * 
	 * double型数值小数部分舍除
	 * <p>
	 * 四舍五入
	 * 
	 * @param value
	 *            double型数值
	 * @param scale
	 *            小数点以后位数
	 * @return double型数值
	 */
	public static Double getRoundValue(Double value, int scale) {
		double result = 0.0;
		if (null != value) {
			result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
		}
		return result;
	}

	/**
	 * double型数值小数部分舍除
	 * <p>
	 * 进位
	 * 
	 * @param value
	 *            double型数值
	 * @param scale
	 *            小数点以后位数
	 * @return double型数值
	 */
	public static Double getRoundUpValue(Double value, int scale) {
		double result = 0.0;
		if (null != value) {
			result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.UP).doubleValue();
		}
		return result;
	}

	/**
	 * double型数值小数部分舍除
	 * <p>
	 * 直接舍掉
	 * <p>
	 * 
	 * @param value
	 *            double型数值
	 * @param scale
	 *            小数点以后位数
	 * @return double型数值
	 */
	public static Double getRoundDownValue(Double value, int scale) {
		double result = 0.0;
		if (null != value) {
			result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.DOWN).doubleValue();
		}
		return result;
	}

	/**
	 * double型数值小数部分舍除
	 * <p>
	 * round: 0:直接舍掉 1：四舍五入 2：进位
	 * 
	 * @param dbValue
	 *            double型数值
	 * @param round
	 *            舍值方式
	 * @param scale
	 *            小数点以后位数
	 * @return double型数值
	 */
	public static double getDoubleValueWithScale(double dbValue, int round, int scale) {
		// 0:直接舍掉 1：四舍五入 2：进位
		if (round == 0) {
			dbValue = getRoundDownValue(dbValue, scale);
		} else if (round == 1) {
			dbValue = getRoundValue(dbValue, scale);
		} else if (round == 2) {
			dbValue = getRoundUpValue(dbValue, scale);
		}
		return dbValue;
	}

	/**
	 * 字符串按照固定长度填充
	 * <p>
	 * 文字型：后补空格 数字型：前补0
	 * 
	 * @param obj
	 *            字符串
	 * @param objType
	 *            变换类型
	 * @param objLen
	 *            长度
	 * @return 变换后的字符串
	 */
	public static String convertBySize(Object obj, String objType, int objLen) {
		String ret = null;
		int oldSize = 0;
		int size = objLen;

		if (obj == null || size == 0) {
			ret = "";
		} else {
			ret = obj.toString();
			oldSize = obj.toString().getBytes().length;
		}
		if (DATA_TYPE_NUMBER.equals(objType)) {
			// 无符号
			if (oldSize < size) {
				int m = size - oldSize;
				for (int i = 0; i < m; i++) {
					// 在最前面增加0
					ret = "0" + ret;
				}
			} else {
				ret = ret.substring(oldSize - size);
			}
		} else if (DATA_TYPE_CHAR.equals(objType)) {
			// 文字型
			if (oldSize < size) {
				int m = size - oldSize;
				for (int i = 0; i < m; i++) {
					// 最后半角空格增加
					ret = ret + " ";
				}
			} else if (oldSize > size) {
				byte[] bytes = ret.getBytes();
				byte[] retBytes = new byte[size];
				for (int i = 0; i < size; i++) {
					retBytes[i] = bytes[i];
				}
				ret = new String(retBytes);
				if (ret.getBytes().length != size) {
					ret = ret + " ";
				}
			}
		}
		return ret;
	}

	/**
	 * 有符号数字转->Integer转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param signNumber
	 *            有符号的字符串
	 * @return Integer型数值
	 */
	public static Integer convSignNumStrToInteger(String signNumber) {
		if (signNumber == null || "".equals(signNumber)) {
			return null;
		}
		try {
			if (signNumber.substring(0, 1).equals("+")) {
				return new Integer(signNumber.substring(1));
			} else if (signNumber.substring(0, 1).equals("-")) {
				return new Integer(Integer.parseInt(signNumber.substring(1)) * -1);
			} else {
				return new Integer(signNumber);
			}
		} catch (NumberFormatException e) {
			return null;
		}

	}

	/**
	 * 有符号数字转->BigDecimal转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param signNumber
	 *            有符号的字符串
	 * @return BigDecimal型数值
	 */
	public static BigDecimal convSignNumStrToBigDecimal(String signNumber) {
		if (signNumber == null || "".equals(signNumber)) {
			return null;
		}
		try {
			if (signNumber.substring(0, 1).equals("+")) {
				return new BigDecimal(signNumber.substring(1));
			} else if (signNumber.substring(0, 1).equals("-")) {
				return new BigDecimal(signNumber.substring(1)).negate();
			} else {
				return new BigDecimal(signNumber);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * BigDecimal->Integer转换(小数部分舍掉)
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param bdNum
	 *            BigDecimal型数值
	 * @return Integer数值
	 */
	public static Integer convBDNumToInteger(BigDecimal bdNum) {
		if (bdNum == null) {
			return null;
		}
		return new Integer(bdNum.setScale(0, RoundingMode.DOWN).intValue());
	}

	/**
	 * 有符号字符串->Integer转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param str
	 *            String文字列
	 * @return Integer数值
	 */
	public static Integer convStrToInteger(String str) {
		if (isEmpty(str)) {
			return null;
		}
		try {
			if (str.substring(0, 1).equals("+")) {
				return new Integer(str.substring(1));
			} else if (str.substring(0, 1).equals("-")) {
				return new Integer(Integer.parseInt(str.substring(1)) * -1);
			} else {
				return new Integer(str);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * String->BigDecimal转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param str
	 *            String文字列
	 * @return BigDecimal数值
	 */
	public static BigDecimal convStrToBigDecimal(String str) {
		if (isEmpty(str)) {
			return null;
		}
		try {
			if (str.substring(0, 1).equals("+")) {
				return new BigDecimal(str.substring(1));
			} else if (str.substring(0, 1).equals("-")) {
				return new BigDecimal(str.substring(1)).negate();
			} else {
				return new BigDecimal(str);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 16进制String->Integer转换
	 * <p>
	 * 入参是null时，返回值为null
	 * 
	 * @param str
	 *            String文字列
	 * @return Integer数值
	 */
	public static Integer convStrToIntegerHex(String str) {
		if (isEmpty(str)) {
			return null;
		}
		try {
			return new Integer(Integer.parseInt(str, 16));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * properties数组型数据按逗号分隔
	 * 
	 * @param properties
	 *            字符串
	 * 
	 * @return List
	 */
	public static List<String> getSplitProperties(String properties) {
		List<String> arrayList = new ArrayList<String>();
		if (isNotEmpty(properties)) {
			String propertieLine = properties.replace("[", "").replace("]", "");
			for (String property : propertieLine.split(",")) {
				arrayList.add(property);
			}
		}
		return arrayList;
	}

	/**
	 * 字符串按特定符号进行分割，返回分割后不为空的字符的List
	 * 
	 * @param line
	 *            字符串
	 * @param mark
	 *            分隔符
	 * @return List
	 */
	public static List<String> getSplitList(String line, String mark) {
		List<String> arrayList = new ArrayList<String>();
		if (isNotEmpty(line)) {
			for (String word : line.split(mark)) {
				if (isNotEmpty(word)) {
					arrayList.add(word);
				}
			}
		}
		return arrayList;
	}

	/**
	 * 字符串按特定符号进行分割，返回分割后不为空的字符的数组
	 * 
	 * @param line
	 *            字符串
	 * @param mark
	 *            分隔符
	 * @return List
	 */
	public static String[] getSplitArray(String line, String mark) {
		List<String> arrayList = new ArrayList<String>();
		if (isNotEmpty(line)) {
			for (String word : line.split(mark)) {
				if (isNotEmpty(word)) {
					arrayList.add(word);
				}
			}
		}
		String[] temp = new String[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			temp[i] = arrayList.get(i);
		}
		return temp;
	}

	/**
	 * 根据自动补全List
	 * 
	 * @param list
	 *            List
	 * @param length
	 *            长度
	 * @param obj
	 *            补全的Object
	 * @return List
	 */
	public static <T> List<T> complementSize(List<T> list, int length, T obj) {
		if (list.size() <= 0) {
			return list;
		}
		// 补全类别是same时，等于list的第一个
		if (COMPLEMENT_SAME.equals(obj)) {
			obj = list.get(0);
		}
		// 如果长度小于，则增加；大于则从右侧删除多余的项
		if (list.size() < length) {
			for (int i = 0; i <= length - list.size(); i++) {
				list.add(obj);
			}
		} else if (list.size() > length) {
			list.subList(0, (list.size() - length - 1));
		}
		return list;
	}

	/**
	 * 系统路径->相对路径转换
	 * <p>
	 * 【\】->【/】
	 * 
	 * @param sysPath
	 *            系统路径
	 * @return 相对路径
	 */
	public static String sysPathToPath(String sysPath) {
		sysPath = sysPath.replace("\\", "/");
		return sysPath;
	}

	/**
	 * 相对路径->系统路径转换
	 * <p>
	 * 【/】->【\】
	 * 
	 * @param path
	 *            相对路径
	 * @return 系统路径
	 */
	public static String pathToSysPath(String path) {
		path = path.replace("/", "\\");
		return path;
	}
	
	/**
	 * 取得系统发布后的完整路径
	 * <p>
	 * 系统发布后名字有可能会发生空白或者填写的情况，该方法能后将发布路径和URL进行拼接
	 * 
	 * @param contextPath 发布路径
	 * @param url 链接
	 * @return 当前URl所属的完整链接
	 */
	public static String getActionPath(String contextPath, String url){
		if(!contextPath.startsWith("/")){
			contextPath = "/" + contextPath;
		}
		if(url.startsWith("/")){
			if("/".equals(contextPath)){
				return url;
			}
			// 没有指定工程名的情况
			return contextPath + url;
		} else {
			if("/".equals(contextPath)){
				return "/" + url;
			}
			// 指定了工程名的情况
			return contextPath + "/" + url;
		}
	}

	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 * 
	 * @author Robin Chang
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		boolean flag = true;
		if (str != null && !str.equals("")) {
			if (str instanceof List) {
				return ((List<?>) str).isEmpty();
			}
			if (str instanceof Map) {
				return ((Map<?, ?>) str).isEmpty();
			}
			if (str.toString().length() > 0) {
				flag = false;
			}
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}

	/**
	 * 
	 * Map转换String
	 * 
	 * @param map
	 *            需要转换的Map
	 * @return String转换后的字符串
	 */
	public static String mapToString(Map<String, Object> map) {
		return mapToString(map, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
	}

	/**
	 * 
	 * Map转换String
	 * 
	 * @param map
	 *            需要转换的Map
	 * @return String转换后的字符串
	 */
	public static String mapToString(Map<String, Object> map, String elementSeparator, String keySeparator) {
		StringBuffer stb = new StringBuffer();
		// 遍历map
		for (String key : map.keySet()) {
			if (isEmpty(key)) {
				continue;
			}
			Object value = map.get(key);
			stb.append(key + keySeparator + toNotNullString(value));
			stb.append(elementSeparator);
		}
		return stb.toString();
	}

	/**
	 * 
	 * String转换Map
	 * 
	 * @param mapText
	 *            需要转换的字符串
	 * @return Map
	 */
	public static Map<String, Object> stringToMap(String mapText) {
		return stringToMap(mapText, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
	}

	/**
	 * 
	 * String转换Map
	 * 
	 * @param mapText
	 *            需要转换的字符串
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @param keySeparator
	 *            字符串中的分隔符每一个key与value中的分割
	 * @return Map
	 */
	public static Map<String, Object> stringToMap(String mapText, String elementSeparator, String keySeparator) {
		if (isEmpty(mapText)) {
			return new HashMap<String, Object>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 转换为数组
		String[] text = mapText.split(elementSeparator);
		for (String str : text) {
			if (isEmpty(str)) {
				continue;
			}
			// 转换key与value的数组
			String[] keyText = str.split(keySeparator);
			if (keyText.length < 2) {
				continue;
			}
			String key = keyText[0];
			String value = keyText[1];
			map.put(key, value);
		}
		return map;

	}
	
	/**
	 * 
	 * String转换Map(key与value相同)
	 * 
	 * @param mapText
	 *            需要转换的字符串
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @return Map
	 */
	public static Map<String, Object> stringToMap(String mapText, String elementSeparator) {
		if (isEmpty(mapText)) {
			return new HashMap<String, Object>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 转换为数组
		String[] text = mapText.split(elementSeparator);
		for (String str : text) {
			if (isEmpty(str)) {
				continue;
			}
			// key与value相同
			String key = str;
			String value = str;
			map.put(key, value);
		}
		return map;

	}

	/**
	 * String转换List
	 * 
	 * @param listText
	 *            需要转换的文本
	 * @param separator
	 *            分隔符
	 * 
	 * @return List<String>
	 */

	public static List<String> stringToList(String listText) {
		return stringToList(listText, SEPARATOR_ELEMENT);
	}

	/**
	 * String转换List
	 * 
	 * @param listText
	 *            需要转换的文本
	 * @param separator
	 *            分隔符
	 * @return List<String>
	 */
	public static List<String> stringToList(String listText, String separator) {
		if (isEmpty(listText)) {
			return  new ArrayList<String>();
		}
		List<String> list = new ArrayList<String>();
		String[] text = listText.split(separator);
		for (String str : text) {
			if (isEmpty(str)) {
				continue;
			}
			list.add(str);
		}
		return list;
	}

	/**
	 * List转换String
	 * 
	 * @param list
	 *            需要转换的List
	 * @param separator
	 *            分隔符
	 * @return String
	 */
	public static String listToString(List<?> list, String separator) {
		StringBuffer stb = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				String str = toNotNullString(obj);
				if (isEmpty(str)) {
					continue;
				}
				stb.append(str);
				stb.append(separator);
			}
		}
		return stb.toString();
	}

	/**
	 * String转换链接参数
	 * 
	 * @param str
	 *            字符串
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @param keySeparator
	 *            字符串中的分隔符每一个key与value中的分割
	 * @return
	 */
	public static String stringToLinkparams(String str) {
		return stringToLinkparams(str, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
	}

	/**
	 * String转换链接参数
	 * 
	 * @param str
	 *            字符串
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @param keySeparator
	 *            字符串中的分隔符每一个key与value中的分割
	 * @return
	 */
	public static String stringToLinkparams(String str, String elementSeparator, String keySeparator) {
		if (isEmpty(str)) {
			return null;
		}
		StringBuffer stb = new StringBuffer();
		// 转换为数组
		String[] elements = str.split(elementSeparator);
		for (String element : elements) {
			if (isEmpty(element)) {
				continue;
			}
			// 转换key与value的数组
			String[] keyText = element.split(keySeparator);
			if (keyText.length < 2) {
				continue;
			}
			stb.append("&" + keyText[0]);
			stb.append("=" + keyText[1]);
		}
		return stb.toString();
	}

	/**
	 * 字符串中特殊字符替换成JSON格式
	 * 
	 * @param str
	 *            字符串
	 * @return 转换后的字符串
	 */
	public static String string2Json(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
				case '\"' :
					sb.append("\\\"");
					break;
				case '\\' :
					sb.append("\\\\");
					break;
				case '/' :
					sb.append("\\/");
					break;
				case '\b' :
					sb.append("\\b");
					break;
				case '\f' :
					sb.append("\\f");
					break;
				case '\n' :
					sb.append("\\n");
					break;
				case '\r' :
					sb.append("\\r");
					break;
				case '\t' :
					sb.append("\\t");
					break;
				default :
					sb.append(c);
			}
		}
		return sb.toString();
	}
	

	/**
	 * 将Entity的属性和值转成Map格式
	 * 
	 * @param entity
	 *            实体类
	 * @return
	 * @throws SecurityException
	 *             安全异常
	 * @throws NoSuchMethodException
	 *             没有找到get方法异常
	 * @throws IllegalArgumentException
	 *             反射方法参数异常
	 * @throws IllegalAccessException
	 *             反射实体类异常
	 * @throws InvocationTargetException
	 *             反射get方法异常
	 */
	public static Map<String, Object> entityToMap(Object entity)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Field[] fileds = entity.getClass().getDeclaredFields();
		for (Field field : fileds) {
			// 获得get方法名
			String methodName = "get"
					+ Character.toUpperCase(field.getName().charAt(0))
					+ field.getName().substring(1);
			if(isNotEmpty(methodName)){
				Method getMethod = entity.getClass().getMethod(methodName);
				retMap.put(field.getName(), getMethod.invoke(entity));
			}
		}
		return retMap;
	}
	
	/**
	 * 取得entity中属性和类型的map
	 * Key：value->属性名：属性类型
	 * 
	 * @param entity 实体类
	 * @return map
	 */
	public static Map<String, Object> getFieldTypeMap(Object entity) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Field[] fileds = entity.getClass().getDeclaredFields();
		for (Field field : fileds) {
			retMap.put(field.getName(), field.getType());
		}
		return retMap;
	}

	
	/**
	 * 取得去重SQL
	 * 
	 * @param tableName 表名
	 * @param distinctFieldList 去重的list
	 * @return 去重sql
	 */
	public static String getDistinctSql(String tableName, List<String> distinctFieldList) {
		String sql = "DELETE FROM " + tableName + " WHERE ROWID NOT IN (SELECT MAX(ROWID) FROM " + tableName
				+ " group by ";
		for (String field : distinctFieldList) {
			sql += (field + ",");
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		return sql;
	}
	
	/**
	 * 获取当前时间
	 * @param format
	 * @return
	 */
	public static String getCurrentTime (String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		java.util.Date currTime = new java.util.Date();

		String curTime = formatter.format(currTime);
		return curTime;
	}
	
	/**
	 * 获取年度列表
	 * @param num 跨度（前后）
	 * @return
	 */
	public static List<String> getYearList(int num){
		List<String> yearList = new ArrayList<String>();
		Integer year = Integer.parseInt(getCurrentTime("yyyy"));
		int begain = year - num + 1;
		//int end = year + num;
		for (int i = begain; i <= year; i ++  ) {
			yearList.add(String.valueOf(i));
		}
		return yearList;
		
	}
	
	/**
	 * 
	 * String转换Options
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param defaultVal
	 *            默认值
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @param keySeparator
	 *            字符串中的分隔符每一个key与value中的分割
	 * @return Map
	 */
	public static String formatOptions(String str, String defaultVal) {
		return formatOptions(str, defaultVal, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
	}

	/**
	 * 
	 * String转换Options
	 * 
	 * @param str
	 *            需要转换的字符串
	 * @param defaultVal
	 *            默认值
	 * @param elementSeparator
	 *            字符串中每个元素的分割
	 * @param keySeparator
	 *            字符串中的分隔符每一个key与value中的分割
	 * @return Map
	 */
	public static String formatOptions(String str, String defaultVal, String elementSeparator, String keySeparator) {
		StringBuffer sf = new StringBuffer();
		String[] elems = str.split(elementSeparator);
		// 拼接下拉框
		if (isEmpty(defaultVal) || "undefined".equals(defaultVal)) {
			sf.append("<option value=\"\" selected=\"selected\">");
		}
		for (String elem : elems) {
			String[] keyValue = elem.split(keySeparator);
			if (keyValue.length < 2) {
				continue;
			}
			String key = keyValue[0];
			String value = keyValue[1];
			if (isNotEmpty(key) && key.equals(defaultVal)) {
				sf.append("<option value=\"" + key
						+ "\" selected=\"selected\">");
			} else {
				sf.append("<option value=\"" + key + "\">");
			}
			if (isNotEmpty(value)) {
				sf.append(value);
				sf.append("</option>");
			}

		}
		return sf.toString();
	}
	
	
	/**
	 * 取得UUID
	 * 
	 * @return UUID
	 */
	public static String getUUID(){
		return toString(UUID.randomUUID()).replace("-", "");
	}
	
	/**
	 * 取得JAVA属性反射方法名
	 * 
	 * @param attr
	 *            属性
	 * @param method
	 *            get/set/other/""
	 * @return 方法名
	 */
	public static String converToMethodName(String attr, String method) {
		String methodName = "";
		if (isEmpty(attr)) {
			return methodName;
		}
		if (attr.length() >= 2) {
			char first = attr.charAt(0);
			char second = attr.charAt(1);
			// mm-> Mm
			if (Character.isLowerCase(first) && Character.isLowerCase(second)) {
				return method + Character.toUpperCase(first) + attr.substring(1);
			} else {
				// MM -> MM
				// mM -> mM
				// Mm -> Mm
				return method + attr;
			}
		} else {
			return method + Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
		}
	}

	/**
	 * Map转实体类
	 * <p/>
	 * Map->Entity
	 * 
	 * 
	 * @param map
	 *            Map
	 * @param clazz
	 *            实体类Class
	 * @return 实体类
	 * @throws NoSuchMethodException
	 *             没有找到set方法异常
	 * @throws SecurityException
	 *             安全异常
	 * @throws IllegalAccessException
	 *             反射实体类异常
	 * @throws InvocationTargetException
	 *             反射set方法异常
	 * @throws IllegalArgumentException
	 *             反射方法参数异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mapToEntity(Map<String, Object> map, Class<?> clazz) throws InstantiationException,
	        IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
	        InvocationTargetException {
		T entity = (T) clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		// 循环查询出的列
		for(String key:map.keySet()){
			boolean haveMethod = false;
			Object value = map.get(key);
			for(Method method:methods){
				if(method.getName().equals(converToMethodName(key.toLowerCase(), METHOD_SET))){
					haveMethod = true;
					Class<?>[] setType = method.getParameterTypes();
					if(setType != null && setType.length >0){
						// 字符串型
						if(setType[0] == String.class){
							method.invoke(entity, ConverterUtil.toString(value));
							break;
						}
						// 整型
						if(setType[0] == int.class || setType[0] == Integer.class){
							method.invoke(entity, ConverterUtil.toInteger(value));
							break;
						}
						// 布尔型
						if(setType[0] == boolean.class || setType[0] == Integer.class){
							method.invoke(entity, ConverterUtil.toBoolean(value));
							break;
						}
						// Long型
						if(setType[0] == long.class || setType[0] == Long.class){
							method.invoke(entity, ConverterUtil.toLong(value));
							break;
						}
						// BigDecimal型
						if(setType[0] == BigDecimal.class){
							method.invoke(entity, ConverterUtil.toBigDecimal(value));
							break;
						}
						// Double型
						if(setType[0] == double.class || setType[0] == Double.class){
							method.invoke(entity, ConverterUtil.toDouble(value));
							break;
						}
						// 日期型
						if(setType[0] == Date.class){
							method.invoke(entity, ConverterUtil.toDate(value, null));
							break;
						}
						// SQL日期型
						if(setType[0] == java.sql.Date.class){
							method.invoke(entity, new java.sql.Date(ConverterUtil.toLong(value)));
							break;
						}
						// 日时型
						if(setType[0] == Timestamp.class){
							method.invoke(entity, ConverterUtil.toTimestamp(value));
							break;
						}
						break;
					} 
				}
			}
			// 如果没有set方法 按照全大写 判断是否有这个属性
			if(!haveMethod){
				for (Field field : fields) {
					// JDBC方式查询出的别名都是大写
					String attrName = field.getName().toUpperCase();
					if (attrName.equals(key)) {
						// 取得类型
						Class<?> attrClazz = field.getType();
						// 如果是私有属性需要设置可操作
						field.setAccessible(true);
						// 字符串型
						if (attrClazz == String.class) {
							field.set(entity, ConverterUtil.toString(value));
							break;
						}
						// 整型
						if (attrClazz == int.class || attrClazz == Integer.class) {
							field.set(entity, ConverterUtil.toInteger(value));
							break;
						}
						// 布尔型
						if (attrClazz == boolean.class || attrClazz == Integer.class) {
							field.set(entity, ConverterUtil.toBoolean(value));
							break;
						}
						// Long型
						if (attrClazz == long.class || attrClazz == Long.class) {
							field.set(entity, ConverterUtil.toLong(value));
							break;
						}
						// BigDecimal型
						if (attrClazz == BigDecimal.class) {
							field.set(entity, ConverterUtil.toBigDecimal(value));
							break;
						}
						// Double型
						if (attrClazz == double.class || attrClazz == Double.class) {
							field.set(entity, ConverterUtil.toDouble(value));
							break;
						}
						// 日期型
						if (attrClazz == Date.class) {
							field.set(entity, ConverterUtil.toDate(value, null));
							break;
						}
						// SQL日期型
						if (attrClazz == java.sql.Date.class) {
							field.set(entity, new java.sql.Date(ConverterUtil.toLong(value)));
							break;
						}
						// 日时型
						if (attrClazz == Timestamp.class) {
							field.set(entity, ConverterUtil.toTimestamp(value));
							break;
						}
						break;
					}
				}
			}
		}
		return entity;
	}
	
	/**
	 * Map转实体类
	 * <p/>
	 * Map->Entity
	 * 
	 * 
	 * @param map
	 *            Map
	 * @param clazz
	 *            实体类Class
	 * @return 实体类
	 * @throws NoSuchMethodException
	 *             没有找到set方法异常
	 * @throws SecurityException
	 *             安全异常
	 * @throws IllegalAccessException
	 *             反射实体类异常
	 * @throws InvocationTargetException
	 *             反射set方法异常
	 * @throws IllegalArgumentException
	 *             反射方法参数异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> listMapToListEntity(List<Map<String, Object>> list, Class<?> clazz) throws InstantiationException,
	        IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException,
	        InvocationTargetException {
		List<T> resultList = new ArrayList<T>();
		for(Map<String, Object> map:list){
			resultList.add((T) mapToEntity(map, clazz));
		}
		return resultList;
	}

	/**
	 * 字符串转码
	 * 
	 * @param str
	 *            字符串
	 * @param getChatset
	 *            取得编码
	 * @param toChatset
	 *            转成编码
	 * @return 字符串
	 * @throws UnsupportedEncodingException
	 *             转换异常
	 */
	public static String encodeStr(String str, String getChatset, String toChatset) throws UnsupportedEncodingException {
		return new String(str.getBytes(getChatset), toChatset);
	}

	/**
	 * 字符串转码
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 * @throws UnsupportedEncodingException
	 *             转换异常
	 */
	public static String encodeStr(String str) throws UnsupportedEncodingException {
		return encodeStr(toNotNullString(str), CHATSET_ISO88591, CHATSET_UTF8);
	}
	
	/**
	 * 取得N位随机验证码
	 * <p/>
	 * 1-配置长度，随机内容
	 * 
	 * @return
	 */
	public static String getCheckCode(int num) {
		// 默认6位
		int nMax = ConverterUtil.toInteger(num, 6);

		// 随机串
		String str = "0123456789";
		Random contRandom = new Random();
		StringBuffer sb = new StringBuffer();
		// 产生随机数
		for (int i = 0; i < nMax; i++) {
			int number = contRandom.nextInt(10);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
    /**
     * 按顺序转换问号
     * 
     * @param str
     *            字符串
     * @param params
     *            参数
     */
    public static String replaceMark(String str, String... params) {
        if (ConverterUtil.isEmpty(str)) {
            return "";
        }
        if (params != null) {
            for (Object obj : params) {
                str = str.replaceFirst("\\?", obj.toString());
            }
        }
        return str;
	}

	/**
	 * 把代码转换成HTML
	 * 
	 * @param str
	 * @return
	 */
	public static String toHTML(String str) {
		if (isEmpty(str)) {
			return "";
		}
		str = str.replace("&quot;", "\"");
		str = str.replace("&amp;", "&");
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		return str;
	}
	
    /**
     * 取得Object(实体Bean)所有的属性与值的键值对(K,V)
     * <p>
     * 属性必须要有get方法
     * 
     * @param entity
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> getAllFileds(Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Map<String, Object> result = new HashMap<String, Object>();
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getFields();
        // 取得属性(定义public与继承的属性)
        for (Field field : fields) {
            Except annExcept = field.getAnnotation(Except.class);
            if(null == annExcept){
                String getMethod = converToMethodName(field.getName(), METHOD_GET);
                Method method;
                try {
                    method = clazz.getMethod(getMethod);
                } catch (NoSuchMethodException e) {
                    method = null;
                }
                if (ConverterUtil.isNotEmpty(method)) {
                    Object value = method.invoke(entity);
                    // 如果属性值不为空则放到返回值中
                    if(ConverterUtil.isNotEmpty(value)){
                        result.put(field.getName(), value);
                    }
                }
            }
        }
        // 取得属性(定义的所有属性包含public)
        Field[] fields2 = clazz.getDeclaredFields();
        for (Field field : fields2) {
            Except annExcept = field.getAnnotation(Except.class);
            if(null == annExcept){
                String getMethod = converToMethodName(field.getName(), METHOD_GET);
                // 过滤出getFields没有取得的
                if (!result.containsKey(getMethod)) {
                    Method method;
                    try {
                        method = clazz.getMethod(getMethod);
                    } catch (NoSuchMethodException e) {
                        method = null;
                    }
                    if (ConverterUtil.isNotEmpty(method)) {
                        // 如果是私有属性不设置该权限会报错
                        method.setAccessible(true);
                        Object value = method.invoke(entity);
                        // 如果属性值不为空则放到返回值中
                        if(ConverterUtil.isNotEmpty(value)){
                            result.put(field.getName(), value);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * 取得SQLite使用到的参数Map(ContentValues)
     * 
     * @param entity
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static ContentValues getContentValues(Object entity) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ContentValues contentValues = new ContentValues();
        Map<String, Object> keyValue = getAllFileds(entity);
        for (String key : keyValue.keySet()) {
            Object value = keyValue.get(key);
            // 字符串型
            if (value instanceof String) {
                contentValues.put(key, ConverterUtil.toString(value));
                continue;
            }
            // 整型
            if (value instanceof Integer) {
                contentValues.put(key, ConverterUtil.toInteger(value));
                continue;
            }
            // Long型
            if (value instanceof Long) {
                contentValues.put(key, ConverterUtil.toLong(value));
                continue;
            }
            // Floag型和Double型
            if (value instanceof Float || value instanceof Double) {
                contentValues.put(key, ConverterUtil.toDouble(value));
                continue;
            }
            // Boolean型
            if (value instanceof Boolean) {
                contentValues.put(key, ConverterUtil.toBoolean(value));
                continue;
            }
            // 其他型没有用到，这里就不进行判断了
        }
        return contentValues;
    }
    
    /**
     * 取得类全部属性(继承或自定义)的get/set方法
     * 
     * @param clazz
     *            类
     * @param modifier
     *            方法修饰符
     * @return
     */
    public static Map<String, Method> getAllMethods(Class<?> clazz, String modifier) {
        Map<String, Method> result = new HashMap<String, Method>();
        Field[] fields = clazz.getFields();
        // 取得属性(定义public与继承的属性)
        for (Field field : fields) {
            String methodName = converToMethodName(field.getName(), modifier);
            Method method;
            try {
                if (METHOD_GET.equals(modifier)) {
                    method = clazz.getMethod(methodName);
                } else {
                    method = clazz.getMethod(methodName, field.getType());
                }
            } catch (NoSuchMethodException e) {
                method = null;
            }
            if (ConverterUtil.isNotEmpty(method)) {
                result.put(field.getName(), method);
            }
        }
        // 取得属性(定义的所有属性包含public)
        Field[] fields2 = clazz.getDeclaredFields();
        for (Field field : fields2) {
            String methodName = converToMethodName(field.getName(), modifier);
            // 过滤出getFields没有取得的
            if (!result.containsKey(methodName)) {
                Method method;
                try {
                    if (METHOD_GET.equals(modifier)) {
                        method = clazz.getMethod(methodName);
                    } else {
                        method = clazz.getMethod(methodName, field.getType());
                    }
                } catch (NoSuchMethodException e) {
                    method = null;
                }
                if (ConverterUtil.isNotEmpty(method)) {
                    // 如果是私有属性不设置该权限会报错
                    method.setAccessible(true);
                    // 如果属性值不为空则放到返回值中
                    result.put(field.getName(), method);
                }
            }
        }
        return result;
    }
}