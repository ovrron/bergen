package com.norway.bergen.ronny.bergen;

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
import android.widget.ImageButton;
import android.widget.Toast;


public class Taxi extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.taxi);
        
        Button header = (Button) findViewById(R.id.buttonHeader);
        header.setText(getResources().getText(R.string.start_button_taxi));
        
        initButtons();
    }
    
    private void initButtons() {
    	ImageButton buttonBergenTaxi = (ImageButton) findViewById(R.id.imageButtonBergenTaxi);
    	buttonBergenTaxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Lytter for dialogknappene
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                switch (which) {
		                	case DialogInterface.BUTTON_POSITIVE:
		        		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_bergentaxi_tlf)));
		        		        startActivity(i);
		                		break;

		                	case DialogInterface.BUTTON_NEGATIVE:
		                		SmsManager sm = SmsManager.getDefault();
		                		sm.sendTextMessage(getResources().getString(R.string.taxi_bergentaxi_tlf), null, getResources().getString(R.string.taxi_bergentaxi_smscode), null, null);
		                		Toast.makeText(getBaseContext(), getResources().getString(R.string.taxi_toast_sms_ok), Toast.LENGTH_LONG).show();
		                		break;
		                }
		            }
		        };
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Taxi.this);
				builder.setTitle(getResources().getString(R.string.taxi_bergentaxi_question_title));
				builder.setMessage(getResources().getString(R.string.taxi_bergentaxi_question_message));
				builder.setPositiveButton(getResources().getString(R.string.taxi_bergentaxi_question_positive), dialogClickListener);
				builder.setNegativeButton(getResources().getString(R.string.taxi_bergentaxi_question_negative), dialogClickListener);
				AlertDialog dialog = builder.create();
				dialog.setIcon(R.drawable.question_icon);
				dialog.show();
		    }
		});
    	
    	ImageButton buttonNorgesTaxi = (ImageButton) findViewById(R.id.imageButtonNorgesTaxi);
    	buttonNorgesTaxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_norgestaxi_tlf)));
		        startActivity(i);
			}
		});

    	ImageButton buttonBryggenTaxi = (ImageButton) findViewById(R.id.imageButtonBryggenTaxi);
    	buttonBryggenTaxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_bryggentaxi_tlf)));
		        startActivity(i);
			}
		});    	
    	
    	ImageButton buttonTaxi1 = (ImageButton) findViewById(R.id.imageButtonTaxi1);
    	buttonTaxi1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_taxi1_tlf)));
		        startActivity(i);
			}
		});    	
    }
}