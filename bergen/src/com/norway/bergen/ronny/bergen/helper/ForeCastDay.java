package com.norway.bergen.ronny.bergen.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForeCastDay
{
	String day;
	Date date;
	List<ForeCastDetail> details;
	
	public ForeCastDay() {
		day = null;
		date = null;
		details = new ArrayList<ForeCastDetail>();
	}
	
	public ForeCastDay(String day, Date date) {
		this.day = day;
		this.date = date;
		details = new ArrayList<ForeCastDetail>();
	}
	
	public void addDetail(ForeCastDetail detail) {
		details.add(detail);
	}
	
	public String getDay() {
		return day;
	}
	
	public Date getDate() {
		return date;
	}
	
	public List<ForeCastDetail> getDetails() {
		return details;
	}

	@Override
	public boolean equals(Object o)
	{
		DateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
	    Date d1;
	    Date d2;
		try
		{
			d1 = df.parse(date.toString());
		    d2 = df.parse(o.toString());
		    if (d1.equals(d2)) {
		    	return true;
		    }
		    else {
		    	return false;
		    }
		} 
		catch (ParseException e)
		{
			return false;
		}
	}
	
	
}
