package com.norway.bergen.ronny.bergen;

import com.norway.bergen.ronny.bergen.helper.IPAddressHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class Start extends Activity {

	private String ipAddress = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        ipAddress = new IPAddressHelper().getLocalIpAddress();
        initButtons();
    }
    
    private void initButtons() {
    	Button buttonParking = (Button) findViewById(R.id.buttonParking);
    	buttonParking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ipAddress!=null) {
					startActivity(new Intent(v.getContext(),Parking.class));	
				}
				else {
					//Lytter for dialogknappene
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                switch (which) {
			                	case DialogInterface.BUTTON_POSITIVE:
			                		SmsManager sm = SmsManager.getDefault();
			                		sm.sendTextMessage(getResources().getString(R.string.parking_sms_no), null, getResources().getString(R.string.parking_sms_message), null, null);
			                		Toast.makeText(getBaseContext(), getResources().getString(R.string.parking_toast_sms_ok), Toast.LENGTH_LONG).show();
			                		break;
			                }
			            }
			        };
					
					//Oppretter dialog med spørsmål om du vil avslutte
					AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
					builder.setTitle(getResources().getString(R.string.parking_sms_question_title));
					builder.setMessage(getResources().getString(R.string.parking_sms_question_message));
					builder.setPositiveButton(getResources().getString(R.string.parking_sms_question_positive), dialogClickListener);
					builder.setNegativeButton(getResources().getString(R.string.parking_sms_question_negative), dialogClickListener);
					AlertDialog dialog = builder.create();
					dialog.setIcon(R.drawable.damn_icon);
					dialog.show();
				}
			}
		});
    	
    	Button buttonWeather = (Button) findViewById(R.id.buttonWeather);
    	buttonWeather.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ipAddress!=null) {
					startActivity(new Intent(v.getContext(),Weather.class));	
				}
				else {
					//Lytter for dialogknappene
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                switch (which) {
			                	//Bruker har valgt å avslutte spillet
			                	case DialogInterface.BUTTON_POSITIVE:
			                		break;
			                }
			            }
			        };
					
					AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
					builder.setTitle(getResources().getString(R.string.weather_no_connection_title));
					builder.setMessage(getResources().getString(R.string.weather_no_connection_message));
					builder.setPositiveButton(getResources().getString(R.string.weather_no_connection_positive), dialogClickListener);
					AlertDialog dialog = builder.create();
					dialog.setIcon(R.drawable.damn_icon);
					dialog.show();
				}
			}
		});
 
    	Button buttonCinema = (Button) findViewById(R.id.buttonCinema);
    	buttonCinema.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ipAddress!=null) {
					Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.cinema_url))); 
					startActivity(browser); 
					//startActivity(new Intent(v.getContext(),Cinema.class));	
				}
				else {
					//Lytter for dialogknappene
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                switch (which) {
			                	case DialogInterface.BUTTON_POSITIVE:
			                		break;
			                }
			            }
			        };
					
					AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
					builder.setTitle(getResources().getString(R.string.weather_no_connection_title));
					builder.setMessage(getResources().getString(R.string.weather_no_connection_message));
					builder.setPositiveButton(getResources().getString(R.string.weather_no_connection_positive), dialogClickListener);
					AlertDialog dialog = builder.create();
					dialog.setIcon(R.drawable.damn_icon);
					dialog.show();
				}
			}
		});    	
    	
    	Button buttonConcert = (Button) findViewById(R.id.buttonConcerts);
    	buttonConcert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ipAddress!=null) {
					Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.concert_url))); 
					startActivity(browser); 
					//startActivity(new Intent(v.getContext(),Concert.class));	
				}
				else {
					//Lytter for dialogknappene
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                switch (which) {
			                	//Bruker har valgt å avslutte spillet
			                	case DialogInterface.BUTTON_POSITIVE:
			                		break;
			                }
			            }
			        };
					
					AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
					builder.setTitle(getResources().getString(R.string.weather_no_connection_title));
					builder.setMessage(getResources().getString(R.string.weather_no_connection_message));
					builder.setPositiveButton(getResources().getString(R.string.weather_no_connection_positive), dialogClickListener);
					AlertDialog dialog = builder.create();
					dialog.setIcon(R.drawable.damn_icon);
					dialog.show();
				}
				
			}
		});  

    	
    	Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
    	buttonPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ipAddress!=null) {
					Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_url))); 
					startActivity(browser); 
					//startActivity(new Intent(v.getContext(),Play.class));	
				}
				else {
					//Lytter for dialogknappene
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                switch (which) {
			                	//Bruker har valgt å avslutte spillet
			                	case DialogInterface.BUTTON_POSITIVE:
			                		break;
			                }
			            }
			        };
					
					AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
					builder.setTitle(getResources().getString(R.string.weather_no_connection_title));
					builder.setMessage(getResources().getString(R.string.weather_no_connection_message));
					builder.setPositiveButton(getResources().getString(R.string.weather_no_connection_positive), dialogClickListener);
					AlertDialog dialog = builder.create();
					dialog.setIcon(R.drawable.damn_icon);
					dialog.show();
				}
			}
		});  

    	Button buttonTaxi = (Button) findViewById(R.id.buttonTaxi);
    	buttonTaxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(),Taxi.class));	
			}
		});    	
    }
}