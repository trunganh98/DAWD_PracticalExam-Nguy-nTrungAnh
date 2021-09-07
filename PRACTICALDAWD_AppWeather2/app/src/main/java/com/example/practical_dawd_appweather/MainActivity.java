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
    private TextView humidityTview;

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
        humidityTview = findViewById(R.id.tumidityTview);
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
                JSONObject jsonMain = jsonObject.getJSONObject("main");
                double temp = jsonMain.getDouble("temp") - 273.15;
                double feelsLike = jsonMain.getDouble("feels_like") - 273.15;
                float pressure = jsonMain.getInt("pressure");
                int humidity = jsonMain.getInt("humidity");
                double tempMin = jsonMain.getDouble("temp_min")- 273.15;
                double tempMax = jsonMain.getDouble("temp_max")- 273.15;

                if(!jsonMain.equals("")){
                    tempTview.setText("Temp: " + temp + "°C" );
                    feelsLikeTview.setText("Feel like:"+ feelsLike + "°C");
                    tempMinTview.setText("Temp Min: " + tempMin + "°C");
                    tempMaxTview.setText("Temp Max: " + tempMax + "°C");
                    pressureTview.setText("Pressure: " + pressure + "Hpa");
                    humidityTview.setText("Tumidity: " + humidity + "%");

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