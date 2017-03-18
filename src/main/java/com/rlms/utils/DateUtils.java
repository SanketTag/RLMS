package com.rlms.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String convertDateToStringWithoutTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithoutTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date addDaysToDate(Date date, int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date); // Now use today date.
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public static boolean isBeforeToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isBeforeOrEqualToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else if(beforeDate.equals(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterOrEqualTo(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else if(afterDate.equals(beforeDate)){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	public static Integer daysBetween(Date d1, Date d2){
		 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
