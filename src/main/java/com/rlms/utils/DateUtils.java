package com.rlms.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String convertDateToStringWithoutTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String s = formatter.format(inputDate);
		return s;
	}
}
