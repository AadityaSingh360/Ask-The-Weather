package com.example.asktheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView result1;
    Button go;

    public void findweather(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
        try {
            String x = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + x + "&APPID=1a669465c2388875bf8e080bbfac6d3a").get();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG);
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char c = (char) data;
                    result += c;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String final1 = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weather);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject part = arr.getJSONObject(i);
                    final1 += part.getString("main") + " : " + part.getString("description") + "\n";
                }
                String other=jsonObject.getString("main");
                JSONArray arr1=new JSONArray(other);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject part=arr1.getJSONObject(i);
                    final1+="Pressure:"+part.getString("pressure")+"\n"+"Humidity: "+part.getString("humidity");
                } 

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG);

            }


            result1.setText(final1);

            Log.i("Weather content", result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        go = (Button) findViewById(R.id.button2);
        result1 = (TextView) findViewById(R.id.result);

    }
}
