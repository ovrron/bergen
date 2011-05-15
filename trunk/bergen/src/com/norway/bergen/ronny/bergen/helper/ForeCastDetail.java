package com.norway.bergen.ronny.bergen.helper;

public class ForeCastDetail
{
	String when;
	String fromTime;
	String toTime;
	int period;
	int symbol;
	String temperature;
	String temperatureUnit;
	String rain;
	String windDirection;
	String windSpeed;
	String windSpeedName;
	String pressure;
	String pressureUnit;
	
	public ForeCastDetail() {
		when = null;
		fromTime = null;
		toTime = null;
		period = -1;
		symbol = -1;
		temperature = null;
		temperatureUnit = null;
		rain = null;
		windDirection = null;
		windSpeed = null;
		windSpeedName = null;
		pressure = null;
		pressureUnit = null;		
	}
	
	public ForeCastDetail(String when, String fromTime, String toTime, int period, int symbol, String temperature, String temperatureUnit, String rain, String windDirection, String windSpeed, String windSpeedName, String pressure, String pressureUnit) {
		this.when = when;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.period = period;
		this.symbol = symbol;
		this.temperature = temperature;
		this.temperatureUnit = temperatureUnit;
		this.rain = rain;
		this.windDirection = windDirection;
		this.windSpeed = windSpeed;
		this.windSpeedName = windSpeedName;
		this.pressure = pressure;
		this.pressureUnit = pressureUnit;	
	}
	
	public String getWhen()
	{
		return when;
	}

	public void setWhen(String when)
	{
		this.when = when;
	}

	public String getFromTime()
	{
		return fromTime;
	}

	public void setFromTime(String fromTime)
	{
		this.fromTime = fromTime;
	}

	public String getToTime()
	{
		return toTime;
	}

	public void setToTime(String toTime)
	{
		this.toTime = toTime;
	}

	public int getPeriod()
	{
		return period;
	}

	public void setPeriod(int period)
	{
		this.period = period;
	}

	public int getSymbol()
	{
		return symbol;
	}

	public void setSymbol(int symbol)
	{
		this.symbol = symbol;
	}

	public String getTemperature()
	{
		return temperature;
	}

	public void setTemperature(String temperature)
	{
		this.temperature = temperature;
	}

	public String getTemperatureUnit()
	{
		return temperatureUnit;
	}

	public void setTemperatureUnit(String temperatureUnit)
	{
		this.temperatureUnit = temperatureUnit;
	}

	public String getRain()
	{
		return rain;
	}

	public void setRain(String rain)
	{
		this.rain = rain;
	}

	public String getWindDirection()
	{
		return windDirection;
	}

	public void setWindDirection(String windDirection)
	{
		this.windDirection = windDirection;
	}

	public String getWindSpeed()
	{
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed)
	{
		this.windSpeed = windSpeed;
	}

	public String getWindSpeedName()
	{
		return windSpeedName;
	}

	public void setWindSpeedName(String windSpeedName)
	{
		this.windSpeedName = windSpeedName;
	}

	public String getPressure()
	{
		return pressure;
	}

	public void setPressure(String pressure)
	{
		this.pressure = pressure;
	}

	public String getPressureUnit()
	{
		return pressureUnit;
	}

	public void setPressureUnit(String pressureUnit)
	{
		this.pressureUnit = pressureUnit;
	}		
	

}
