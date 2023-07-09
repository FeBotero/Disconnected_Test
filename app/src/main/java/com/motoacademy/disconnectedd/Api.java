package com.motoacademy.disconnectedd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonSubmit;

    HandleProps handleProps = new HandleProps();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                if (!email.isEmpty()) {
                    new PostRequestTask().execute(email);
                } else {
                    Toast.makeText(Api.this, "Digite um email válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class PostRequestTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String email = params[0];
            String response = "";

            try {
                URL url = new URL("https://motoacademyserver.onrender.com/client/check");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String requestBody = "{\"email\": \"" + email + "\"}";

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.close();

                int statusCode = connection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response += line;
                    }
                    reader.close();
                } else {
                    response = "Erro na solicitação: " + statusCode;
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                response = "Erro na solicitação: " + e.getMessage();
            }
            try {
                return new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }



        @Override
        protected void onPostExecute(JSONObject responseObject) {
            try {
                boolean isActive = responseObject.getBoolean("isActive");
                String pass = responseObject.getString("pass");

               if(isActive){
                    handleProps.read("persist.control.wifi.service");
                    handleProps.write("persist.control.wifi.service",Boolean.toString(true));
                    /*try {
                        Runtime.getRuntime().exec("adb shell svc wifi disable");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }*/
                }
                Toast.makeText(Api.this, "isActive: " + isActive + ", pass: " + pass, Toast.LENGTH_SHORT).show();

                // Salve os valores em variáveis da classe, se necessário
                // handleProps.setIsActive(isActive);
                // handleProps.setPass(pass);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Api.this, "Erro ao analisar a resposta JSON", Toast.LENGTH_SHORT).show();
            }


        }
    }
}