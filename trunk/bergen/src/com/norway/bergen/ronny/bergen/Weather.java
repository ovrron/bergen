/**
 * Weather.java
 * @author Ronny Øvereng
 * Opprettet 2011.02.21
 *
 */
package com.norway.bergen.ronny.bergen;

import java.util.Date;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
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
import com.norway.bergen.ronny.bergen.helper.ParseYrForeCast;

public class Weather extends Activity 
{	
	private static final String LOG_TAG = "Weather";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(LOG_TAG, "Starter Weather");
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather);
        
        TableLayout detailTable = (TableLayout) findViewById(R.id.TableLayout_Detalj);
    	CommunicationTask comTask = new CommunicationTask();
        comTask.execute(this, detailTable, getResources().getString(R.string.weather_url));
    }
    
    private class CommunicationTask extends AsyncTask<Object, Object, Boolean> {
    	private int noOfDetailRows = 0;
    	private Context context;
    	private ProgressDialog waitDialog = new ProgressDialog(Weather.this);
    	private TableLayout detailTable;
    	private ForeCast foreCast;
        private float textSizeSmall;
        private float textSizeSuperSmall;
        private float textSizeMedium;
        private ParseYrForeCast foreCastParser = new ParseYrForeCast();

        
		@Override
		protected Boolean doInBackground(Object... params) {
			context = (Context) params[0];
			detailTable = (TableLayout) params[1];
			publishProgress(foreCastParser.getForeCast((String)params[2]));
			return null;
		}
        
        @Override
        protected void onPostExecute(Boolean result) {
            waitDialog.hide();
            setValues();
        }
        
        @Override
        protected void onPreExecute() {
            waitDialog.setTitle(R.string.weather_wait_dialog_title);
            waitDialog.setMessage(getResources().getString(R.string.weather_wait_dialog_message));
            waitDialog.show();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
        	if(values[0]!=null) {
        		foreCast = (ForeCast) values[0];
        		detailTable.removeAllViews();
        		setValues();
        	}
        	else {
        		String errorDetails = foreCastParser.getErrorDetails();
        		if(errorDetails==null) {
        			errorDetails = getResources().getString(R.string.weather_toast_bad_unknown_error);
        		}
        		Toast.makeText(getBaseContext(), getResources().getString(R.string.weather_toast_bad) + " " + errorDetails, Toast.LENGTH_LONG).show();
        	}
        }
        
        private void setValues() {
        	
            textSizeSmall = getResources().getDimension(R.dimen.small_size);
            textSizeSuperSmall = getResources().getDimension(R.dimen.supersmall_size);
            textSizeMedium = getResources().getDimension(R.dimen.medium_size);

            Button header = (Button) findViewById(R.id.buttonHeader);
            header.setText(getResources().getText(R.string.start_button_weather) + " i " + foreCast.getPlaceName());
            
        	Date dateLastUpdated = foreCastParser.toDate(foreCast.getLastUpdated(),false);
        	Date dateNextUpdate = foreCastParser.toDate(foreCast.getNextUpdate(),false);
        	
        	TextView textViewCredit = (TextView) findViewById(R.id.textViewCredit);
        	textViewCredit.setText(foreCast.getCreditText() + ".\n"  +foreCast.getCreditUrl() + "\nSist oppdatert " + dateLastUpdated.toLocaleString() + ".\nNeste oppdatering tilgjengelig " + dateNextUpdate.toLocaleString() + ".");  
        	Pattern pattern = Pattern.compile(foreCast.getCreditUrl());
        	Linkify.addLinks(textViewCredit, pattern, "http://");
        	
        	for(ForeCastDay day:foreCast.getDays()) {
        		String date = day.getDate().toLocaleString();
        		date = date.substring(0, date.length()-8).trim();
        		addDetailHeader(day.getDay() + ", " + date, detailTable);
        		for(ForeCastDetail detail:day.getDetails()) {
        			addDetailRow(detailTable, detail.getWhen()+"\n"+detail.getFromTime()+"-"+detail.getToTime() , detail.getPeriod(), detail.getSymbol(), detail.getTemperature(), detail.getRain(), detail.getWindSpeedName()+"\n"+detail.getWindSpeed()+"\n"+detail.getWindDirection());
        		}
        	}
        }
        
        /**
         * Legger til en linje i detaljert v�rdata tab'en
         * @param table
         * @param when
         * @param period
         * @param symbol
         * @param temperature
         * @param rain
         * @param wind
         */
        private void addDetailRow(TableLayout table, String when, int period, 
        		int symbol, String temperature, String rain, String wind) {

        	TableRow newRow = new TableRow(context);
        	
        	//id til symbolgrafikk
        	int id = 0;
        	noOfDetailRows++;
        	
        	//Legger bakgrunnsfarge på raden
        	if((noOfDetailRows % 2) == 0) {
        		newRow.setBackgroundColor(getResources().getColor(R.color.background_even));
        	}
        	else {
        		newRow.setBackgroundColor(getResources().getColor(R.color.background_odd));
        	}
        	
        	/*
        	 * Når på dagen
        	 */
        	TextView textView = new TextView(context);
            textView.setTextSize(textSizeSmall);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            //CENTER_VERTICAL fungerer ikke, blir nødt til å legge på et linjeskift:
            textView.setText(when);
            newRow.addView(textView);
            
            /*
             * Symbol (bilde)
             */
            //Forsøker å beregne navn på bildefil
        	String drawableName = String.valueOf(symbol);
        	String drawableNamePlus = ""; 
        	if(symbol<10) {
        		drawableName = "0"+drawableName;
        	}
        	if(period==0 || period==3) {
        		drawableNamePlus = "n"+drawableName;
        	}
        	else {
        		drawableNamePlus = "d"+drawableName;
        	}
        	id = getResources().getIdentifier(drawableNamePlus, "drawable", "com.norway.bergen.ronny.bergen");
        	if(id==0) {
        		drawableNamePlus = "f"+drawableName;
        		id = getResources().getIdentifier(drawableNamePlus, "drawable", "com.norway.bergen.ronny.bergen");
        	}
        	
        	if(id!=0) {
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
            int temp = Integer.valueOf(temperature);
            if(temp<1) {
            	textView.setTextColor(getResources().getColor(R.color.blue));
            }
            else {
            	textView.setTextColor(getResources().getColor(R.color.red));
            }
            textView.setText("\n"+temperature+"\u00B0");
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            newRow.addView(textView);
            
            
            /*
             * Nedbør
             */
        	textView = new TextView(context);
            textView.setTextSize(textSizeSmall);
            textView.setText("\n "+rain+" mm   ");
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            newRow.addView(textView);

            /*
             * Vind
             */
        	textView = new TextView(context);
            textView.setTextSize(textSizeSuperSmall);
            textView.setText(wind);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            newRow.addView(textView);
            
            table.addView(newRow);   
        }
        
        /**
         * Legg til overskrift i detaljtab'en
         * @param text
         * @param table
         */
        private void addDetailHeader(String text, TableLayout table) {
        	TextView textView = new TextView(context);
            textView.setTextSize(textSizeMedium);
            textView.setTypeface(null,Typeface.BOLD);
            textView.setBackgroundResource(R.drawable.weather_detail_header);
            textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            textView.setText(text);
            table.addView(textView);
        }
    }
}