package com.motoacademy.disconnectedd;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Activate extends AppCompatActivity {


    Button btnChange;

    EditText editPass;
    WifiManager mWifiManager;
    HandleProps handleProps = new HandleProps();

    String pass = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

        btnChange = (Button) findViewById(R.id.btnChangeWifi);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        editPass = (EditText) findViewById(R.id.etPassword);


        if(mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN OFF");
        }else
        if(!mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN ON");
        }

        btnChange.setOnClickListener(new View.OnClickListener() {

            public void SetWifi(boolean status) {

                Log.i("STATS_WIFI_CONTROL", String.valueOf(status));
            }
            @Override
            public void onClick(View view) {



                String password = editPass.getText().toString();

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Activate.this, "Password field cannot is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.equals(pass)){
                    handleProps.read("persist.control.wifi.service");
                    handleProps.write("persist.control.wifi.service",Boolean.toString(true));
                    handleProps.read("persist.control.wifi.service");
                    //SystemProperties.write("persist.control.wifi.service",Boolean.toString(false));
                    SetWifi(false);

                }else
                if(!password.equals(pass)){

                    handleProps.read("persist.control.wifi.service");
                    handleProps.write("persist.control.wifi.service",Boolean.toString(false));
                    handleProps.read("persist.control.wifi.service");
                    SetWifi(false);
                }

            }
        });
    }
}