package com.mohakamg.www.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    final String API_KEY = "1f473c813b0524fe0823cad50f991cce";
    final String URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    final String URL_AUTH = "&appid=";
    String city;
    String country;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {



            String response = "";

            URL url = null;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char ch = (char) data;
                    response += ch;
                    data = reader.read();
                }

                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "FAILED";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                JSONObject jsonPart = arr.getJSONObject(0);
                Log.i("Main",jsonObject.getString("main"));
                Log.i("Description",jsonObject.getString("description"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = "Roorkee";
        country = "India";

        DownloadTask task = new DownloadTask();

        String completeURL = URL+city+","+country+URL_AUTH+API_KEY;
        task.execute(completeURL);




    }
}
