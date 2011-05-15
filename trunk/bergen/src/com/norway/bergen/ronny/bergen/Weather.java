/**
 * Weather.java
 * @author Ronny Øvereng
 * Opprettet 2011.02.21
 *
 */
package com.norway.bergen.ronny.bergen;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.norway.bergen.ronny.bergen.helper.ForeCast;
import com.norway.bergen.ronny.bergen.helper.ForeCastDay;
import com.norway.bergen.ronny.bergen.helper.ForeCastDetail;

public class Weather extends Activity 
{	
	
    private float textSizeNormal;
    private float textSizeSmall;
    private float textSizeSuperSmall;
    private float textSizeMedium;
    
	//Antall linjer i detaljtabellen, brukes for å kunne vise ulik bakgrunn på par-/oddetallslinjene
	private int antallDetaljRader = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather);

        textSizeNormal = getResources().getDimension(R.dimen.normal_size);
        textSizeSmall = getResources().getDimension(R.dimen.small_size);
        textSizeSuperSmall = getResources().getDimension(R.dimen.supersmall_size);
        textSizeMedium = getResources().getDimension(R.dimen.medium_size);
        
        TableLayout detaljTable = (TableLayout) findViewById(R.id.TableLayout_Detalj);
    	CommunicationTask comTask = new CommunicationTask();
        comTask.execute(this, detaljTable, getResources().getString(R.string.weather_url));
        //hentVarsel(getResources().getString(R.string.weather_url), detaljTable);
        //setValues();
    }
    
    
    
    private class CommunicationTask extends AsyncTask<Object, Object, Boolean> {

    	Context context;
    	ProgressDialog waitDialog = new ProgressDialog(Weather.this);
    	TableLayout detaljTable;
    	ForeCast foreCast;

		@Override
		protected Boolean doInBackground(Object... params)
		{
			context = (Context) params[0];
			detaljTable = (TableLayout) params[1];
			publishProgress(hentVarsel((String)params[2]));
			
			return null;
		}
        
        @Override
        protected void onPostExecute(Boolean result) 
        {
            waitDialog.hide();
            setValues();
        }
        
        @Override
        protected void onPreExecute() 
        {
            waitDialog.setTitle(R.string.weather_wait_dialog_title);
            waitDialog.setMessage(getResources().getString(R.string.weather_wait_dialog_message));
            waitDialog.show();

        }

        @Override
        protected void onProgressUpdate(Object... values)
        {
        	if(values[0]!=null) {
        		foreCast = (ForeCast) values[0];
        		detaljTable.removeAllViews();
        		setValues();
        	}
        }
        
        
        private void setValues()
        {
            Button header = (Button) findViewById(R.id.buttonHeader);
            header.setText(getResources().getText(R.string.start_button_weather) + " i " + foreCast.getPlaceName());
            
        	Date dato = tilDato(foreCast.getLastUpdated(),false);
        	Date next = tilDato(foreCast.getNextUpdate(),false);
        	
        	TextView textViewCredit = (TextView) findViewById(R.id.textViewCredit);
        	textViewCredit.setText(foreCast.getCreditText() + ".\n"  +foreCast.getCreditUrl() + "\nSist oppdatert " + dato.toLocaleString() + ".\nNeste oppdatering tilgjengelig " + next.toLocaleString() + ".");  
        	Pattern pattern = Pattern.compile(foreCast.getCreditUrl());
        	Linkify.addLinks(textViewCredit, pattern, "http://");
        	
        	for(ForeCastDay day:foreCast.getDays()) {
        		String date = day.getDate().toLocaleString();
        		date = date.substring(0, date.length()-8).trim();
        		leggTilDetaljOverskrift(day.getDay() + ", " + date, detaljTable);
        		for(ForeCastDetail detail:day.getDetails()) {
        			leggTilDetaljLinje(detaljTable, detail.getWhen()+"\n"+detail.getFromTime()+"-"+detail.getToTime() , detail.getPeriod(), detail.getSymbol(), detail.getTemperature(), detail.getRain(), detail.getWindSpeedName()+"\n"+detail.getWindSpeed()+"\n"+detail.getWindDirection());
        		}
        	}
        }

        
        /**
         * Legger til en linje i detaljert v�rdata tab'en
         * @param tab
         * @param naarPaaDagen
         * @param periode
         * @param symbol
         * @param temperatur
         * @param nedbør
         * @param vind
         */
        private void leggTilDetaljLinje(TableLayout tab, String naarPaaDagen, int periode, int symbol, 
        		String temperatur, String nedbør, String vind) 
        {
        	//Oppretter en ny rad
        	TableRow newRow = new TableRow(context);
        	
        	//id til symbolgrafikk
        	int id = 0;
        	//øker antall rader med 1
        	antallDetaljRader++;
        	
        	//Legger bakgrunnsfarge på raden
        	if((antallDetaljRader % 2) == 0)
        	{
        		newRow.setBackgroundColor(getResources().getColor(R.color.background_even));
        	}
        	else
        	{
        		newRow.setBackgroundColor(getResources().getColor(R.color.background_odd));
        	}
        	
        	/*
        	 * Når på dagen
        	 */
        	TextView textView = new TextView(context);
            textView.setTextSize(textSizeSmall);
            //textView.setTypeface(null,Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            //CENTER_VERTICAL fungerer ikke, blir nødt til å legge på et linjeskift:
            textView.setText(naarPaaDagen);
            newRow.addView(textView);
            
            /*
             * Symbol (bilde)
             */
            //Forsøker å beregne navn på bildefil
        	String drawableName = String.valueOf(symbol);
        	String drawableNamePlus = ""; 
        	if(symbol<10)
        	{
        		drawableName = "0"+drawableName;
        	}
        	if(periode==0 || periode==3)
        	{
        		drawableNamePlus = "n"+drawableName;
        	}
        	else
        	{
        		drawableNamePlus = "d"+drawableName;
        	}
        	id = getResources().getIdentifier(drawableNamePlus, "drawable", "com.norway.bergen.ronny.bergen");
        	if(id==0)
        	{
        		drawableNamePlus = "f"+drawableName;
        		id = getResources().getIdentifier(drawableNamePlus, "drawable", "com.norway.bergen.ronny.bergen");
        	}
        	
        	if(id!=0)
        	{
            	ImageView imageView = new ImageView(context);
            	imageView.setImageResource(id);
            	
            	newRow.addView(imageView);
        	}
            
            /*
             * Temperatur
             */
            textView = new TextView(context);
            textView.setTextSize(textSizeMedium);
            textView.setTypeface(null,Typeface.BOLD);
            int temp = Integer.valueOf(temperatur);
            if(temp<1)
            {
            	textView.setTextColor(getResources().getColor(R.color.blue));
            }
            else
            {
            	textView.setTextColor(getResources().getColor(R.color.red));
            }
            textView.setText("\n"+temperatur+"\u00B0");
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            newRow.addView(textView);
            
            
            /*
             * Nedbør
             */
        	textView = new TextView(context);
            textView.setTextSize(textSizeSmall);
            //textView.setTypeface(null,Typeface.BOLD);
            textView.setText("\n "+nedbør+" mm   ");
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
//            LayoutParams layoutParams = new LayoutParams();
//            layoutParams.setMargins(1,0,1,0);
//            textView.setLayoutParams(layoutParams);
            newRow.addView(textView);

            /*
             * Vind
             */
        	textView = new TextView(context);
            textView.setTextSize(textSizeSuperSmall);
            //textView.setTypeface(null,Typeface.BOLD);
            textView.setText(vind);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            newRow.addView(textView);
            
            /*
             * Legger til den ny raden
             */
            tab.addView(newRow);   
        }
        
        /**
         * Legg til overskrift i detaljtab'en
         * @param text
         * @param tab
         */
        private void leggTilDetaljOverskrift(String text, TableLayout tab) 
        {
        	TextView textView = new TextView(context);
            textView.setTextSize(textSizeMedium);
            textView.setTypeface(null,Typeface.BOLD);
            textView.setBackgroundResource(R.drawable.weather_detail_header);
//            textView.setBackgroundColor(getResources().getColor(R.color.background_title));
            textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            textView.setText(text);
            tab.addView(textView);
        }
        
    }
    
    
    /**
     * Henter varsel for valgt sted og online
     */
    private ForeCast hentVarsel(String url)
    {
    	//Url objekt for å velge xml ressurs online
        URL xmlUrl = null;
        //Xmlperseren
        XmlPullParser varselBatch = null;
        String errorDetail = "";

        try
		{
        	//Henter url til valgt sted
			xmlUrl = new URL(url);
			//Initierer xmlparseren
			varselBatch = XmlPullParserFactory.newInstance().newPullParser();
			//Henter xmlstrømmen fra yr.no
			varselBatch.setInput(xmlUrl.openStream(), null);
			//Parser xmlfilen (online eller offline)
			return parseVarsel(varselBatch);
		} 
        catch (MalformedURLException e)
		{
			e.printStackTrace();
			errorDetail = e.getMessage();
			
		} 
        catch (XmlPullParserException e)
		{
        	e.printStackTrace();
			errorDetail = e.getMessage();
		} 
        catch (IOException e)
		{
			e.printStackTrace();
			errorDetail = e.getMessage();
		}
        Toast.makeText(getBaseContext(), getResources().getString(R.string.weather_toast_bad) + " " + errorDetail, Toast.LENGTH_LONG).show();
        return null;
    }
    
  /**
   * Parser valgt værdata
   * Velger å ha try-catch her istedenfor å kaste unntakene videre.
   * På den måten kan jeg fortsette til neste del av xml filen om noe skulle krasje underveis.
   * @param varsel, xml parseren
   */
    private ForeCast parseVarsel(XmlPullParser varsel)
    {
    	ForeCast foreCast = new ForeCast();
    	//Type tag
		int eventType = -1;
		
        //Så lenge vi ikke har nådd slutten av xml filen
	     while ( eventType != XmlResourceParser.END_DOCUMENT) 
	     { 
	    	 //Hvis starttag
	    	 if (eventType == XmlResourceParser.START_TAG) 
	    	 { 
	    		 String strName = varsel.getName();
	    		 
	    		 /*
	    		  * Stedsinformasjon
	    		  */
	    		 if (strName.equals("location")) 
	    		 { 
	    			 try
	    			 {
	    				 behandleSted(varsel, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e)
	    			 {
	    				 e.printStackTrace();
	    			 } 
	    			 catch (IOException e)
	    			 {
	    				 e.printStackTrace();
	    			 } 
	    		 } 
	    		 
	    		 /*
	    		  * Krediteringsinformasjon
	    		  */
	    		 else if (strName.equals("credit")) 
	    		 { 
	    			 try
	    			 {
	    				 behandleKreditering(varsel, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e)
	    			 {
	    				 e.printStackTrace();
	    			 } 
	    			 catch (IOException e)
	    			 {
	    				 e.printStackTrace();
	    			 }  
	    		 } 
	    		 
	    		 /*
	    		  * Oppdateringsinformasjon
	    		  */
	    		 else if (strName.equals("meta")) 
	    		 { 
	    			 try
	    			 {
	    				 behandleMeta(varsel, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e)
	    			 {
	    				 e.printStackTrace();
	    			 } 
	    			 catch (IOException e)
	    			 {
	    				 e.printStackTrace();
	    			 }  
	    		 } 	    		 

	    		 /*
	    		  * Solinformasjon
	    		  */
	    		 else if (strName.equals("sun")) 
	    		 { 
	    			 //Tekstlig info
	    			 foreCast.setSunRise(varsel.getAttributeValue(null, "rise"));
					//Url
	    			 foreCast.setSunSet(varsel.getAttributeValue(null, "set"));
	    		 } 	    		 

	    		 /*
	    		  * Selve værvarselet
	    		  */
	    		 else if (strName.equals("forecast")) 
	    		 { 
	    			 try
	    			 {
	    				 behandleVarsel(varsel, foreCast);
	    			 } 
	    			 catch (XmlPullParserException e)
	    			 {
	    				 e.printStackTrace();
	    			 } 
	    			 catch (IOException e)
	    			 {
	    				 e.printStackTrace();
	    			 }
	    		 }
	         } 
	         try
	         {
	        	 eventType=varsel.next();
	         } 
	         catch (XmlPullParserException e)
	         {
	        	 e.printStackTrace();
	         } 
	         catch (IOException e)
	         {
	        	 e.printStackTrace();
	         } 
	     }
	     return foreCast;
	}
    
    /**
     * Behandler lokasjonsinformasjon
     * @param varsel
     * @param tab
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void behandleSted(XmlPullParser varsel, ForeCast foreCast) throws XmlPullParserException, IOException  

    { 
    	String tekst = ""; 
    	//Ferdig med å parse denne delen
        boolean ferdig=false; 
        //Tagtype
        int eventType=-1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!ferdig) 
        { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) 
            { 
            	String strName = varsel.getName();
            	
            	//Tag for navn på sted
                if (strName.equals("name"))
                {
                	foreCast.setPlaceName(varsel.nextText().trim());
                }
                
                //Tag for type sted (Tettsted/By)
                else if (strName.equals("type"))
                {
                	foreCast.setPlaceType(varsel.nextText().trim());
                }
                
                //Tag for land
                else if (strName.equals("country"))
                {
                	foreCast.setPlaceCountry(varsel.nextText().trim());
                }
                
                //Tag for tidssone
                else if (strName.equals("timezone"))
                {
                	foreCast.setPlaceTimezone(varsel.getAttributeValue(null, "id").trim());                	
                }
                
                //Tag for koordinatinfo
                else if (strName.equals("location"))
                {
                	
                	foreCast.setPlaceAltitude(varsel.getAttributeValue(null, "altitude").trim());
                	foreCast.setPlaceLatitude(varsel.getAttributeValue(null, "latitude").trim());
                	foreCast.setPlaceLongitude(varsel.getAttributeValue(null, "longitude").trim());
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) 
            { 
            	String strName = varsel.getName(); 
                if (strName.equals("location")) ferdig=true; 
            } 
            eventType = varsel.next(); 
        } 
	}    

    private void behandleMeta(XmlPullParser varsel, ForeCast foreCast) throws XmlPullParserException, IOException
    { 
    	String tekst = ""; 
    	//Ferdig med å parse denne delen
        boolean ferdig=false; 
        //Tagtype
        int eventType=-1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!ferdig) 
        { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) 
            { 
            	String strName = varsel.getName(); 
            	
                if (strName.equals("lastupdate"))
                {
                	foreCast.setLastUpdated(varsel.nextText().trim());
                }
                else if (strName.equals("nextupdate"))
                {
                	foreCast.setNextUpdate(varsel.nextText().trim());
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) 
            { 
            	String strName = varsel.getName(); 
                if (strName.equals("meta")) ferdig=true; 
            } 
            if(!ferdig)
            {
            	eventType = varsel.next(); 
            }
        } 
	}

   
    
    /**
     * Behandler krediteringsinformasjonen
     * @param varsel
     * @param tab1
     * @param tab2
     * @param tab3
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void behandleKreditering(XmlPullParser varsel, ForeCast foreCast) throws XmlPullParserException, IOException
    { 
    	String tekst = ""; 
    	//Ferdig med å parse denne delen
        boolean ferdig=false; 
        //Tagtype
        int eventType=-1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!ferdig) 
        { 
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) 
            { 
            	String strName = varsel.getName(); 
            	
            	//Tag for creditinfo
                if (strName.equals("link"))
                {
                	//Tekstlig info
                	foreCast.setCreditText(varsel.getAttributeValue(null, "text"));
                	//Url
                	foreCast.setCreditUrl(varsel.getAttributeValue(null, "url"));
                }
            } 
        	
        	//Slutt tag
            else if (eventType==XmlResourceParser.END_TAG) 
            { 
            	String strName = varsel.getName(); 
                if (strName.equals("credit")) ferdig=true; 
            } 
            if(!ferdig)
            {
            	eventType = varsel.next(); 
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
    private void behandleVarsel(XmlPullParser varsel, ForeCast foreCast) throws XmlPullParserException, IOException
    {
    	//Ferdig med å parse denne delen
        boolean ferdig=false; 
        //Tagtype
        int eventType=-1;
        
        //Så lenge denne delen av xml filen er ferdigparset
        while (!ferdig) 
        {
        	//Start tag
          	if (eventType == XmlResourceParser.START_TAG) 
            { 
            	String strName = varsel.getName();
            	
            	/*
            	 * Den tekstlige delen av varselet
            	 */
                if (strName.equals("text"))
                {
                	//behandleTekstVarsel(varsel,tekstTab);
                }
                
                /*
                 * Den detaljerte delen av varselet
                 */
                else if(strName.equals("tabular"))
                {
                	behandleDetaljVarsel(varsel, foreCast);
                }
            }
          	//Slutt tag
          	else if (eventType==XmlResourceParser.END_TAG) 
          	{ 
          		if (varsel.getName().equals("forecast")) ferdig=true; 
          	} 
          	if (!ferdig) eventType = varsel.next();
        }
    }    
    

    /**
     * Behandler den detaljerte værmeldingen
     * @param varsel
     * @param detaljTab
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void behandleDetaljVarsel(XmlPullParser varsel, ForeCast foreCast) throws XmlPullParserException, IOException
    {
    	ForeCastDay day = null;
       	String fra = ""; //Fra dato-tidspunkt, hentes fra xml
    	String til = ""; //Til dato-tidspunkt, hentes fra xml
    	String periode = ""; //Døgnet deles i 4 perioder, perioden hentes fra xml
    	String naarPaaDagen = ""; //min benevnelse på periode, beregnes
    	int per=0; //periode som integer, beregnes
    	String symbol = ""; //Symbolid (for grafisk visning), hentes fra xml
    	int sym=0; //symbol som integer, beregnes
    	String symbolName = ""; //Symbolid'ens tilhørende beskrivelse, hentes fra xml, brukes for øyeblikket ikke
    	String vindFra = ""; //Hvor vinden kommer fra, hentes fra xml
    	String vindHastighetBeskrivelse = ""; //Vindhastighetbenevnelse, hentes fra xml
    	String vindHastighetMps = ""; //Vindhastigheten i mps, hentes fra xml
    	String temperatur = ""; //Temperatur i grader C, hentes fra xml 
    	String temperaturUnit = "";
    	String pressure = "";
    	String pressureUnit = "";
    	String nedbør = ""; //Millimeter nedbør, hentes fra xml
    	Date dato = null; //Dato varselet gjelder for, omgjøres fra string til dato
    	Date forrigeDato = null; //Tar vare på forrige dato, brukes til å sjekke om jeg skal skrive ny overskrift i tabellen
        boolean ferdig=false; //Ferdigparset 
        int eventType=-1; //Tagtype
     
        //Så lenge vi ikke er ferdig med denne delen av xml
        while (!ferdig) 
        {
        	//Start tag
        	if (eventType == XmlResourceParser.START_TAG) 
            { 
            	String strName = varsel.getName();
            	
            	//Tag for en periode
                if (strName.equals("time"))
                {
                	fra = varsel.getAttributeValue(null, "from");
                   	til = varsel.getAttributeValue(null, "to");
        			periode = varsel.getAttributeValue(null, "period");
        			per = Integer.valueOf(periode);
                }
                
                //Tag for symbol og benevnelse
                else if(strName.equals("symbol"))
                {
                	symbol = varsel.getAttributeValue(null, "number");
                   	symbolName = varsel.getAttributeValue(null, "name");
           			sym = Integer.valueOf(symbol);
                }
                
                //Tag for nedbør
                else if(strName.equals("precipitation"))
                {
                	nedbør = varsel.getAttributeValue(null, "value");
                }
                
                //Tag for vindretning
                else if(strName.equals("windDirection"))
                {
                	vindFra = varsel.getAttributeValue(null, "name");
                }
                
                //Tag for vindhastighet og benevnelse
                else if(strName.equals("windSpeed"))
                {
                	
                	vindHastighetBeskrivelse = varsel.getAttributeValue(null, "name");
                	vindHastighetMps = varsel.getAttributeValue(null, "mps");
                }
                
                //Tag for temperatur
                else if(strName.equals("temperature"))
                {
                	temperatur = varsel.getAttributeValue(null, "value");
                	temperaturUnit = varsel.getAttributeValue(null, "unit");
                }         
                //Tag for temperatur
                else if(strName.equals("pressure"))
                {
                	pressure = varsel.getAttributeValue(null, "value");
                	pressureUnit = varsel.getAttributeValue(null, "unit");
                }                            
            } 
        	
        	//Slutt tag
          	else if (eventType==XmlResourceParser.END_TAG) 
          	{ 
          		String strName = varsel.getName(); 
          		
          		//Ferdig med denne delen av xml
          		if (strName.equals("tabular"))
          		{
          			 ferdig=true; 
          		}
          		
          		//Ferdig med en periode, på tide å skrive resultat til tabellen
          		else if(strName.equals("time"))
          		{
          			//Beregner naarPaaDagen og dato
                   	switch (per)
					{
					case 0:
						naarPaaDagen = "Natt til ";
						dato =  tilDato(til,true);
						break;
					case 1:
						naarPaaDagen = "Formiddag ";
						dato =  tilDato(til,true);
						break;
					case 2:
						naarPaaDagen = "Ettermiddag ";
						dato =  tilDato(til,true);
						break;
					case 3:
						naarPaaDagen = "Kveld ";
						dato =  tilDato(fra,true);
						break;
					default:
						break;
					}
                   	
                   	//Hvis første instans på denne dagen, lager overskrift
          			if(per==0||forrigeDato==null||!forrigeDato.equals(dato))
          			{
          				forrigeDato = dato;
          				String dag = hentDag(dato);
          		        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
          				//leggTilDetaljOverskrift(dag + " " + sdf.format(dato)  , detaljTab);
          		        if(day!=null) {
          		        	foreCast.addDay(day);
          		        }
          		        day = new ForeCastDay(dag, dato);

          			}
          			
          			//Skriver detaljert værdata til tabellen
          			String fraKl = fra.substring(fra.indexOf("T")+1, fra.length()-3);
          			String tilKl = til.substring(til.indexOf("T")+1, til.length()-3);
//          			leggTilDetaljLinje(detaljTab, naarPaaDagen + "\n" + fraKl + "-" + tilKl, 
//          					per,sym, temperatur, nedbør, vindHastighetBeskrivelse + "\n" + 
//          					vindHastighetMps + " mps" + "\nfra " + vindFra);
          			ForeCastDetail detail = new ForeCastDetail(naarPaaDagen, fraKl, tilKl, per, sym, temperatur, temperaturUnit, nedbør, vindFra, vindHastighetMps, vindHastighetBeskrivelse, pressure, pressureUnit);
          			day.addDetail(detail);
          		}
          	} 
          	if (!ferdig) eventType = varsel.next();
        }
        if(day!=null) {
        	if (!foreCast.containsDay(day)) {
        		foreCast.addDay(day);
        	}
        }
    }
    

    
   
    /**
     * Konverterer fra string til dato
     * @param dato som String
     * @return dato som Date
     */
    private Date tilDato(String dato, boolean ignoreTime)
    {
    	Date d = null;
    	SimpleDateFormat df = null;
    	String dateToParse = null;
    	
    	if(ignoreTime)
    	{
    		df = new SimpleDateFormat("yyyy-MM-dd");
    		dateToParse = dato.substring(0,dato.indexOf("T"));
    	}
    	else
    	{
    		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		dateToParse = dato;
    	}
    	
    	try
		{
    		d = df.parse(dateToParse);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return d;
    }
    
    /**
     * Henter navn på dag fra dato
     * @param d som Date
     * @return Navn på dag som String
     */
    private String hentDag(Date d)
    {
    	String dag = "";
    	String dagNavn[] = {"Søndag","Mandag","Tirsdag","Onsdag","Torsdag","Fredag","Lørdag"}; 
    	
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    Date iDag = calendar.getTime();
	    
		if(d.equals(iDag))
		{
			dag = "I dag";
		}
		else
		{
			calendar.add(Calendar.DATE,1);
			Date iMorgen = calendar.getTime();
			if(d.equals(iMorgen))
			{
				dag = "I morgen";	
			}
			else
			{
				dag = dagNavn[d.getDay()];	
			}
		}
    	return dag;
    }
}
