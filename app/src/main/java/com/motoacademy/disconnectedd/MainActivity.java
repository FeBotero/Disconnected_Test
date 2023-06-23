package com.motoacademy.disconnectedd;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnChange ;
    WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btnChange = (Button) findViewById(R.id.btnChangeWifi);

        if(mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN OFF");
        }else
        if(!mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN ON");
        }



        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWifiManager.isWifiEnabled()){
                    btnChange.setText("WIFI TURN ON");
                    mWifiManager.setWifiEnabled(false);
                }else
                if(!mWifiManager.isWifiEnabled()){
                    btnChange.setText("WIFI TURN OFF");
                    mWifiManager.setWifiEnabled(true);
                }
            }
        });

    }
}