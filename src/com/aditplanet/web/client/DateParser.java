package com.aditplanet.web.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser 
{
	public static Date parseDate(String dateStr)
	{		
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	    Date result = null;
		try {
			result = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}  	    
	  
	    return result;
	}

}
