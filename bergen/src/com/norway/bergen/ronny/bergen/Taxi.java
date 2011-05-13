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


public class Taxi extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.taxi);
        initButtons();
    }
    
    private void initButtons() {
    	Button buttonBergenTaxiCall = (Button) findViewById(R.id.buttonBergeTaxiCall);
    	buttonBergenTaxiCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_bergentaxi_tlf)));
		        startActivity(i);
		    }
		});
    	Button buttonBergenTaxiSMS = (Button) findViewById(R.id.buttonBergenTaxiSMS);
    	buttonBergenTaxiSMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		SmsManager sm = SmsManager.getDefault();
        		sm.sendTextMessage(getResources().getString(R.string.taxi_bergentaxi_tlf), null, getResources().getString(R.string.taxi_bergentaxi_smscode), null, null);
			}
		});
    	Button buttonNorgesTaxiCall = (Button) findViewById(R.id.buttonNorgesTaxi);
    	buttonNorgesTaxiCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_norgestaxi_tlf)));
		        startActivity(i);
			}
		});
    	Button buttonBryggenTaxiCall = (Button) findViewById(R.id.buttonBryggenTaxi);
    	buttonBryggenTaxiCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_bryggentaxi_tlf)));
		        startActivity(i);
			}
		});    	
    	Button buttonTaxi1Call = (Button) findViewById(R.id.buttonTaxi1);
    	buttonTaxi1Call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.taxi_taxi1_tlf)));
		        startActivity(i);
			}
		});    	
    }
}