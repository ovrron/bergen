package com.norway.bergen.ronny.bergen.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.res.XmlResourceParser;
import android.util.Log;

public class ParseYrForeCast {
	
	private static final String LOG_TAG = "ParseYrForeCast";
	private String errorDetails;
	
	public ParseYrForeCast() {
		errorDetails = null;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}
	
    /**
     * Henter varsel for valgt sted og online
     */
    public ForeCast getForeCast(String url) {
    	//Url objekt for å velge xml ressurs online
        URL xmlUrl = null;
        //Xmlperseren
        XmlPullParser foreCastBatch = null;
        errorDetails = null;

        try {
        	//Henter url til valgt sted
			xmlUrl = new URL(url);
			//Initierer xmlparseren
			foreCastBatch = XmlPullParserFactory.newInstance().newPullParser();
			//Henter xmlstrømmen fra yr.no
			foreCastBatch.setInput(xmlUrl.openStream(), null);
			//Parser xmlfilen (online eller offline)
			return parseForeCast(foreCastBatch);
		} 
        catch (MalformedURLException e) {
			Log.e(LOG_TAG, e.getMessage());
			errorDetails = e.getMessage();
		} 
        catch (XmlPullParserException e) {
			Log.e(LOG_TAG, e.getMessage());
			errorDetails = e.getMessage();
		} 
        catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
			errorDetails = e.getMessage();
		}
        return null;
    }
    
  /**
   * Parser valgt værdata
   * Velger å ha try-catch her istedenfor å kaste unntakene videre.
   * På den måten kan jeg fortsette til neste del av xml filen om noe skulle krasje underveis.
   * @param varsel, xml parseren
   */
    private ForeCast parseForeCast(XmlPullParser xmlForeCast) {
    	ForeCast foreCast = new ForeCast();
    	//Type tag
		int eventType = -1;
		
        //Så lenge vi ikke har nådd slutten av xml filen
	     while ( eventType != XmlResourceParser.END_DOCUMENT) { 
	    	 //Hvis starttag
	    	 if (eventType == XmlResourceParser.START_TAG) { 
	    		 String tagName = xmlForeCast.getName();
	    		 
	    		 /*
	    		  * Stedsinformasjon
	    		  */
	    		 if (tagName.equals("location")) { 
	    			 try {
	    				 processLocation(xmlForeCast, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 } 
	    			 catch (IOException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 } 
	    		 } 
	    		 
	    		 /*
	    		  * Krediteringsinformasjon
	    		  */
	    		 else if (tagName.equals("credit")) { 
	    			 try {
	    				 processCredit(xmlForeCast, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 } 
	    			 catch (IOException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 }  
	    		 } 
	    		 
	    		 /*
	    		  * Oppdateringsinformasjon
	    		  */
	    		 else if (tagName.equals("meta")) { 
	    			 try {
	    				 processMeta(xmlForeCast, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 } 
	    			 catch (IOException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 }  
	    		 } 	    		 

	    		 /*
	    		  * Solinformasjon
	    		  */
	    		 else if (tagName.equals("sun")) { 
	    			 //Tekstlig info
	    			 foreCast.setSunRise(xmlForeCast.getAttributeValue(null, "rise"));
					//Url
	    			 foreCast.setSunSet(xmlForeCast.getAttributeValue(null, "set"));
	    		 } 	    		 

	    		 /*
	    		  * Selve værvarselet
	    		  */
	    		 else if (tagName.equals("forecast")) { 
	    			 try {
	    				 processForeCast(xmlForeCast, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 } 
	    			 catch (IOException e) {
	    				 Log.e(LOG_TAG, e.getMessage());
	    			 }
	    		 }
	         } 
	         try {
	        	 eventType=xmlForeCast.next();
	         } 
	         catch (XmlPullParserException e) {
	        	 Log.e(LOG_TAG, e.getMessage());
	         } 
	         catch (IOException e) {
	        	 Log.e(LOG_TAG, e.getMessage());
	         } 
	     }
	     return foreCast;
	}
    
    /**
     * Behandler lokasjonsinformasjon
     * @param xmlForeCast
     * @param tab
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void processLocation(XmlPullParser xmlForeCast, ForeCast foreCast) throws XmlPullParserException, IOException  { 
    	//Ferdig med å parse denne delen
        boolean done = false; 
        //Tagtype
        int eventType=-1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!done) { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) { 
            	String tagName = xmlForeCast.getName();
            	
            	//Tag for navn på sted
                if (tagName.equals("name")) {
                	foreCast.setPlaceName(xmlForeCast.nextText().trim());
                }
                
                //Tag for type sted (Tettsted/By)
                else if (tagName.equals("type")) {
                	foreCast.setPlaceType(xmlForeCast.nextText().trim());
                }
                
                //Tag for land
                else if (tagName.equals("country")) {
                	foreCast.setPlaceCountry(xmlForeCast.nextText().trim());
                }
                
                //Tag for tidssone
                else if (tagName.equals("timezone")) {
                	foreCast.setPlaceTimezone(xmlForeCast.getAttributeValue(null, "id").trim());                	
                }
                
                //Tag for koordinatinfo
                else if (tagName.equals("location")) {
                	foreCast.setPlaceAltitude(xmlForeCast.getAttributeValue(null, "altitude").trim());
                	foreCast.setPlaceLatitude(xmlForeCast.getAttributeValue(null, "latitude").trim());
                	foreCast.setPlaceLongitude(xmlForeCast.getAttributeValue(null, "longitude").trim());
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) { 
            	String tagNameNext = xmlForeCast.getName(); 
                if (tagNameNext.equals("location")) done = true; 
            } 
            eventType = xmlForeCast.next(); 
        } 
	}    

    private void processMeta(XmlPullParser xmlForeCast, ForeCast foreCast) throws XmlPullParserException, IOException { 
    	//Ferdig med å parse denne delen
        boolean done = false; 
        //Tagtype
        int eventType = -1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!done) { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) { 
            	String tagName = xmlForeCast.getName(); 
            	
                if (tagName.equals("lastupdate")) {
                	foreCast.setLastUpdated(xmlForeCast.nextText().trim());
                }
                else if (tagName.equals("nextupdate")) {
                	foreCast.setNextUpdate(xmlForeCast.nextText().trim());
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) { 
            	String tagNameNext = xmlForeCast.getName(); 
                if (tagNameNext.equals("meta")) done=true; 
            } 
            if(!done) {
            	eventType = xmlForeCast.next(); 
            }
        } 
	}

    
    /**
     * Behandler krediteringsinformasjonen
     * @param xmlForeCast
     * @param tab1
     * @param tab2
     * @param tab3
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void processCredit(XmlPullParser xmlForeCast, ForeCast foreCast) throws XmlPullParserException, IOException { 
    	//Ferdig med å parse denne delen
        boolean done = false; 
        //Tagtype
        int eventType = -1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!done ) { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) { 
            	String tagName = xmlForeCast.getName(); 
            	
            	//Tag for creditinfo
                if (tagName.equals("link")) {
                	//Tekstlig info
                	foreCast.setCreditText(xmlForeCast.getAttributeValue(null, "text"));
                	//Url
                	foreCast.setCreditUrl(xmlForeCast.getAttributeValue(null, "url"));
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) { 
            	String tagNameNext = xmlForeCast.getName(); 
                if (tagNameNext.equals("credit")) done =true; 
            } 
            if(!done ) {
            	eventType = xmlForeCast.next(); 
            }
        } 
	}

    /**
     * Behanlder selve værvarselet
     * 
     * @param varsel, xmlparseren
     * @param tekstTab, tab for den tekstlige v�rvarslingen
     * @param detaljTab, tab for den detaljerte v�rvarslingen
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void processForeCast(XmlPullParser xmlForeCast, ForeCast foreCast) throws XmlPullParserException, IOException {
    	//Ferdig med å parse denne delen
        boolean done = false; 
        //Tagtype
        int eventType = -1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!done) {
        	//Start tag
          	if (eventType == XmlResourceParser.START_TAG) { 
            	String tagName = xmlForeCast.getName();
            	
            	/*
            	 * Den tekstlige delen av varselet
            	 */
                if (tagName.equals("text")) {
                	//behandleTekstVarsel(varsel,tekstTab);
                }
                
                /*
                 * Den detaljerte delen av varselet
                 */
                else if(tagName.equals("tabular")) {
                	processDetailForeCast(xmlForeCast, foreCast);
                }
            }
          	//Slutt tag
          	else if (eventType==XmlResourceParser.END_TAG) { 
          		if (xmlForeCast.getName().equals("forecast")) done=true; 
          	} 
          	if (!done) eventType = xmlForeCast.next();
        }
    }    
    

    /**
     * Behandler den detaljerte værmeldingen
     * @param xmlForeCast
     * @param detaljTab
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void processDetailForeCast(XmlPullParser xmlForeCast, ForeCast foreCast) throws XmlPullParserException, IOException {
    	ForeCastDay day = null;
       	String from = ""; //Fra dato-tidspunkt, hentes fra xml
    	String to = ""; //Til dato-tidspunkt, hentes fra xml
    	String when = ""; //min benevnelse på periode, beregnes
    	String period = ""; //Døgnet deles i 4 perioder, perioden hentes fra xml
    	int per = 0; //periode som integer, beregnes
    	String symbol = ""; //Symbolid (for grafisk visning), hentes fra xml
    	int sym=0; //symbol som integer, beregnes
    	String symbolName = ""; //Symbolid'ens tilhørende beskrivelse, hentes fra xml, brukes for øyeblikket ikke
    	String windFrom = ""; //Hvor vinden kommer fra, hentes fra xml
    	String windSpeedDescripton = ""; //Vindhastighetbenevnelse, hentes fra xml
    	String windSpeed = ""; //Vindhastigheten i mps, hentes fra xml
    	String temperature = ""; //Temperatur i grader C, hentes fra xml 
    	String temperatureUnit = "";
    	String pressure = "";
    	String pressureUnit = "";
    	String rain = ""; //Millimeter nedbør, hentes fra xml
    	Date date = null; //Dato varselet gjelder for, omgjøres fra string til dato
    	Date previousDate = null; //Tar vare på forrige dato, brukes til å sjekke om jeg skal skrive ny overskrift i tabellen
        boolean done = false; //Ferdigparset 
        int eventType = -1; //Tagtype
     
        //Så lenge vi ikke er ferdig med denne delen av xml
        while (!done) {
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) { 
            	String tagName = xmlForeCast.getName();
            	
            	//Tag for en periode
                if (tagName.equals("time")) {
                	from = xmlForeCast.getAttributeValue(null, "from");
                   	to = xmlForeCast.getAttributeValue(null, "to");
        			period = xmlForeCast.getAttributeValue(null, "period");
        			per = Integer.valueOf(period);
                }
                
                //Tag for symbol og benevnelse
                else if(tagName.equals("symbol")) {
                	symbol = xmlForeCast.getAttributeValue(null, "number");
                   	symbolName = xmlForeCast.getAttributeValue(null, "name");
           			sym = Integer.valueOf(symbol);
                }
                
                //Tag for nedbør
                else if(tagName.equals("precipitation")) {
                	rain = xmlForeCast.getAttributeValue(null, "value");
                }
                
                //Tag for vindretning
                else if(tagName.equals("windDirection")) {
                	windFrom = xmlForeCast.getAttributeValue(null, "name");
                }
                
                //Tag for vindhastighet og benevnelse
                else if(tagName.equals("windSpeed")) {
                	
                	windSpeedDescripton = xmlForeCast.getAttributeValue(null, "name");
                	windSpeed = xmlForeCast.getAttributeValue(null, "mps");
                }
                
                //Tag for temperatur
                else if(tagName.equals("temperature")) {
                	temperature = xmlForeCast.getAttributeValue(null, "value");
                	temperatureUnit = xmlForeCast.getAttributeValue(null, "unit");
                }         
                //Tag for temperatur
                else if(tagName.equals("pressure")) {
                	pressure = xmlForeCast.getAttributeValue(null, "value");
                	pressureUnit = xmlForeCast.getAttributeValue(null, "unit");
                }                            
            } 
        	
        	//Slutt tag
          	else if (eventType==XmlResourceParser.END_TAG) { 
          		String tagNameNext = xmlForeCast.getName(); 
          		
          		//Ferdig med denne delen av xml
          		if (tagNameNext.equals("tabular")) {
          			 done=true; 
          		}
          		
          		//Ferdig med en periode, på tide å skrive resultat til tabellen
          		else if(tagNameNext.equals("time")) {
          			//Beregner naarPaaDagen og dato
                   	switch (per) {
					case 0:
						when = "Natt til ";
						date =  toDate(to,true);
						break;
					case 1:
						when = "Formiddag ";
						date =  toDate(to,true);
						break;
					case 2:
						when = "Ettermiddag ";
						date =  toDate(to,true);
						break;
					case 3:
						when = "Kveld ";
						date =  toDate(from,true);
						break;
					default:
						break;
					}
                   	
                   	//Hvis første instans på denne dagen, lager overskrift
          			if(per==0||previousDate==null||!previousDate.equals(date)) {
          				previousDate = date;
          				String dag = getDay(date);
          		        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
          				//leggTilDetaljOverskrift(dag + " " + sdf.format(dato)  , detaljTab);
          		        if(day!=null) {
          		        	foreCast.addDay(day);
          		        }
          		        day = new ForeCastDay(dag, date);
          			}
          			
          			//Skriver detaljert værdata til tabellen
          			String fromTime = from.substring(from.indexOf("T")+1, from.length()-3);
          			String toTime = to.substring(to.indexOf("T")+1, to.length()-3);
          			ForeCastDetail detail = new ForeCastDetail(when, fromTime, toTime, per, sym, temperature, temperatureUnit, rain, windFrom, windSpeed, windSpeedDescripton, pressure, pressureUnit);
          			day.addDetail(detail);
          		}
          	} 
          	if (!done) eventType = xmlForeCast.next();
        }
        if(day!=null) {
        	if (!foreCast.containsDay(day)) {
        		foreCast.addDay(day);
        	}
        }
    }
    
    /**
     * Henter navn på dag fra dato
     * @param d som Date
     * @return Navn på dag som String
     */
    private String getDay(Date d) {
    	String day = "";
    	String dayNames[] = {"Søndag","Mandag","Tirsdag","Onsdag","Torsdag","Fredag","Lørdag"}; 
    	
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    Date toDay = calendar.getTime();
	    
		if(d.equals(toDay)) {
			day = "I dag";
		}
		else {
			calendar.add(Calendar.DATE,1);
			Date toMorrow = calendar.getTime();
			if(d.equals(toMorrow)) {
				day = "I morgen";	
			}
			else {
				day = dayNames[d.getDay()];	
			}
		}
    	return day;
    }
    
    /**
     * Konverterer fra string til dato
     * @param date som String
     * @return dato som Date
     */
    public Date toDate(String date, boolean ignoreTime) {
    	Date d = null;
    	SimpleDateFormat df = null;
    	String dateToParse = null;
    	
    	if(ignoreTime) {
    		df = new SimpleDateFormat("yyyy-MM-dd");
    		dateToParse = date.substring(0,date.indexOf("T"));
    	}
    	else {
    		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		dateToParse = date;
    	}
    	
    	try {
    		d = df.parse(dateToParse);
		} 
    	catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
    }
}
