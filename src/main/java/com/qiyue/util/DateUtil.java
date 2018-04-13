package com.qiyue.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.qiyue.constant.Constant;

public class DateUtil {
	public static String getSystemTime(String formatar){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(formatar);
        return formatter.format(currentTime);
	}
	
	public static String getSystemTime(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(Constant.DATE_FORMATER_NO_INTERVAL);
        return formatter.format(currentTime);
	}
	
	public static Date parseDate(String str,String formater) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.parse(str);
	}
	
	public static String formatDate(Date date,String formater) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(date);
	}
	
	public static int compareDate(String str1,String str2) throws ParseException {
		Date date1 = parseDate(str1,Constant.DATE_FORMATER_NO_INTERVAL);
		Date date2 = parseDate(str2,Constant.DATE_FORMATER_NO_INTERVAL);
		return compareDate(date1,date2);
	}
	
	public static String formatDate(String str,String inputFormater,String outputFormater) throws ParseException {
		Date date1 = parseDate(str,inputFormater);
		return formatDate(date1,outputFormater);
	}
	
	public static int compareDate(Date date1,Date date2) {
		return date1.compareTo(date2);
	}
	
	/**
	 * 获取指定日期的前后任意天数的日期
	 * @param inputDate 初始日期
	 * @param beforeOrAfter 往过去还是未来计算 
	 * @param days 天数
	 * @param inputDateFormat 输入日期格式
	 * @param outputDateFormat 数输出日期格式
	 * @return
	 * @throws Exception 
	 */
	public static String dateHandle(String inputDate,boolean beforeOrAfter,int days,String inputDateFormat,String outputDateFormat) throws Exception{
		SimpleDateFormat inputSdf = new SimpleDateFormat(inputDateFormat);
		SimpleDateFormat outputSdf = new SimpleDateFormat(outputDateFormat);
		Date oneDay = null;
		Date anyDate = null;
		try {
			oneDay = inputSdf.parse(inputDate);
			Calendar oneCalendar = Calendar.getInstance();
			oneCalendar.setTime(oneDay);
			int day = oneCalendar.get(Calendar.DATE);
			if (beforeOrAfter){
				oneCalendar.set(Calendar.DATE, day - days);
			} else {
				oneCalendar.set(Calendar.DATE, day + days);
			}
			anyDate = oneCalendar.getTime();
			String anyDay = outputSdf.format(anyDate);
			return anyDay;
		} catch (ParseException e) {
			throw new Exception("日期格式不正确");
		}
	}
}
