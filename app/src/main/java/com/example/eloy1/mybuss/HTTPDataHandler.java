package com.example.eloy1.mybuss;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by eloy1 on 27/12/2017.
 */
public class HTTPDataHandler {
    static String stream=null;

    public HTTPDataHandler(){

    }
    public String GetHTTPData(String urlString ){
        URL url = null;
        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();

            //revisa el estado de la conexion
            if(urlConnection.getResponseCode()==200)
            {
                //if response code = 200 ¬ HTTP.OK
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r= new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line=r.readLine())!=null){
                    sb.append(line);
                    stream = sb.toString();
                    urlConnection.disconnect();

                }
            }
            else{

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PostHTTPData(String urlString, String json){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.connect();
            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }
            InputStream response = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PutHTTPData(String urlString, String newValue){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);

            byte[] out = newValue.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type","application/json: charset=UTF-8");
            urlConnection.connect();
            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }
            InputStream response = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void DeleteHTTPData(String urlString, String json){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type","application/json: charset=UTF-8");
            urlConnection.connect();
            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }
            InputStream response = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
