package com.example.practical_dawd_appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText enterCityNameEdt;
    private TextView tempTview;
    private TextView feelsLikeTview;
    private TextView tempMinTview;
    private TextView tempMaxTview;
    private TextView pressureTview;
    private TextView tumidityTview;

    public void GetWeather(View view){
        try{
            DownloadTask task = new DownloadTask();

            String encodedCityNane = URLEncoder.encode(enterCityNameEdt.getText().toString(), "UTF-8");
            String urlRequest = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityNane + "&appid=e884a9d1405ed3d3291b00819931ff32";
            task.execute(urlRequest);
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(enterCityNameEdt.getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterCityNameEdt = findViewById(R.id.enterCityNameEdt);
        tempTview = findViewById(R.id.tempTview);
        feelsLikeTview = findViewById(R.id.feelsLikeTview);
        tempMinTview = findViewById(R.id.tempMinTview);
        tempMaxTview = findViewById(R.id.tempMaxTview);
        pressureTview = findViewById(R.id.pressureTview);
        tumidityTview = findViewById(R.id.tumidityTview);
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return null;

            }
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weatherInfo " , weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                String message = "";
                for (int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                   String main = jsonPart.getString("main");
                   String description = jsonPart.getString("description");
                    Log.i("main " , main);
                   if(!main.equals("") && !description.equals("")){
                       message += main + ": " + description + "\r\n";
                    }
               }
                if(!message.equals("")){
                    feelsLikeTview.setText(message);

                }else{
                    Toast.makeText(getApplicationContext(), "Could not find weather :(", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Could not find weather :(", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}