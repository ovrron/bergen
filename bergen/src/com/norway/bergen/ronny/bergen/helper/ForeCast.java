package com.norway.bergen.ronny.bergen.helper;

import java.util.List;
import java.util.ArrayList;

public class ForeCast {
	String placeName;
	String placeType;
	String placeCountry;
	String placeTimezone;
	String placeAltitude;
	String placeLatitude;
	String placeLongitude;

	String lastUpdated;
	String nextUpdate;
	
	String sunRise;
	String sunSet;
	
	String creditText;
	String creditUrl;
	List<ForeCastDay> days;
	
	public ForeCast() {
		placeName = null;
		placeType = null;
		placeCountry = null;
		placeTimezone = null;
		placeAltitude = null;
		placeLatitude = null;
		placeLongitude = null;

		lastUpdated = null;
		nextUpdate = null;
		
		sunRise = null;
		sunSet = null;
		
		creditText = null;
		creditUrl = null;
		days = new ArrayList<ForeCastDay>();
	}
	
	public ForeCast(String placeName,String placeType,String placeCountry,String placeTimezone,String placeAltitude,String placeLatitude,String placeLongitude,String lastUpdated,String nextUpdate,String sunRise,String sunSet,String creditText, String creditUrl) {
		this.placeName = placeName;
		this.placeType = placeType;
		this.placeCountry = placeCountry;
		this.placeTimezone = placeTimezone;
		this.placeAltitude = placeAltitude;
		this.placeLatitude = placeLatitude;
		this.placeLongitude = placeLongitude;

		this.lastUpdated = lastUpdated;
		this.nextUpdate = nextUpdate;
		
		this.sunRise = sunRise;
		this.sunSet = sunRise;
		
		this.creditText = creditText;
		this.creditUrl = creditText;
		days = new ArrayList<ForeCastDay>();
	}
	
	public void addDay(ForeCastDay day) {
		days.add(day);
	}
	
	public boolean containsDay(ForeCastDay day) {
		for(ForeCastDay d:days) {
			if(d.equals(day)) {
				return true;
			}
		}
		return false;
	}

	public String getPlaceName()
	{
		return placeName;
	}

	public void setPlaceName(String placeName)
	{
		this.placeName = placeName;
	}

	public String getPlaceType()
	{
		return placeType;
	}

	public void setPlaceType(String placeType)
	{
		this.placeType = placeType;
	}

	public String getPlaceCountry()
	{
		return placeCountry;
	}

	public void setPlaceCountry(String placeCountry)
	{
		this.placeCountry = placeCountry;
	}

	public String getPlaceTimezone()
	{
		return placeTimezone;
	}

	public void setPlaceTimezone(String placeTimezone)
	{
		this.placeTimezone = placeTimezone;
	}

	public String getPlaceAltitude()
	{
		return placeAltitude;
	}

	public void setPlaceAltitude(String placeAltitude)
	{
		this.placeAltitude = placeAltitude;
	}

	public String getPlaceLatitude()
	{
		return placeLatitude;
	}

	public void setPlaceLatitude(String placeLatitude)
	{
		this.placeLatitude = placeLatitude;
	}

	public String getPlaceLongitude()
	{
		return placeLongitude;
	}

	public void setPlaceLongitude(String placeLongitude)
	{
		this.placeLongitude = placeLongitude;
	}

	public String getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public String getNextUpdate()
	{
		return nextUpdate;
	}

	public void setNextUpdate(String nextUpdate)
	{
		this.nextUpdate = nextUpdate;
	}

	public String getSunRise()
	{
		return sunRise;
	}

	public void setSunRise(String sunRise)
	{
		this.sunRise = sunRise;
	}

	public String getSunSet()
	{
		return sunSet;
	}

	public void setSunSet(String sunSet)
	{
		this.sunSet = sunSet;
	}

	public String getCreditText()
	{
		return creditText;
	}

	public void setCreditText(String creditText)
	{
		this.creditText = creditText;
	}

	public String getCreditUrl()
	{
		return creditUrl;
	}

	public void setCreditUrl(String creditUrl)
	{
		this.creditUrl = creditUrl;
	}

	public List<ForeCastDay> getDays()
	{
		return days;
	}

}

