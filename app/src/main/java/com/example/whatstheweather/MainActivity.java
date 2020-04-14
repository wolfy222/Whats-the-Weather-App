package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static java.net.URLEncoder.encode;

public class MainActivity extends AppCompatActivity
{

    EditText editText;
    TextView resultTextView;
    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            }

            catch (Exception e)
            {
                Toast.makeText(MainActivity.this, "asx", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                //Log.i("Exxxxx",e.toString());
                return null;

            }

        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String weatherTemp = jsonObject.getString("main");

                Log.i("info",weatherTemp);
                JSONArray arr1 = new JSONArray(weatherInfo);
               JSONObject arr2 = new JSONObject(weatherTemp);

                String message = "";
          //      Log.i("th",weatherTemp);
                String message2="";

                for(int i=0 ; i < arr1.length() ; i++)
                {
                    JSONObject jsonPart = arr1.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if(!main.equals("") && !description.equals(""))
                    {
                        message += main + " : " + description + "\r\n";
                    }
                }
                 String temp = arr2.getString("temp");
                 // String temp_min = arr2.getString("temp_min");
                 //String temp_max = arr2.getString("temp_max");
                 message2    = "Temperature : "+ temp;
                 message += message2;
                if(!message.equals("") )
                {
                    resultTextView.setText(message);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "asd", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e)
            {
               // Log.i("Exxxxx",e.toString());
                Toast.makeText(MainActivity.this, "Could not find Weather :(", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void getWeather(View view)
    {
        //Toast.makeText(MainActivity.this, "asdsss", Toast.LENGTH_SHORT).show();

        try
        {
            DownloadTask task = new DownloadTask();
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            task.execute("https://openweathermap.org/data/2.5/weather?q=" +encodedCityName+  "&appid=439d4b804bc8187953eb36d2a8c26a02");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
           // Log.i("Exxxxx",e.toString());
            Toast.makeText(MainActivity.this, "asdsss", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.editText);
        resultTextView =(TextView) findViewById(R.id.resultTtextView);

    }
}
