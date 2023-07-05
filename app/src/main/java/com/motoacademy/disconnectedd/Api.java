package com.motoacademy.disconnectedd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonSubmit;

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

    private class PostRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
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

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(Api.this, response, Toast.LENGTH_SHORT).show();
        }
    }
}