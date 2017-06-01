package com.ligthartproductions.joost.swapiapp.service;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Joost on 31-5-2017.
 */

public class HttpRequest {

    private static final String TAG = HttpRequest.class.getSimpleName();

    public HttpRequest() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            // Er wordt een url opgehaald en een connectie ermee gelegd.
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Er wordt een GET methode gevraagd
            conn.setRequestMethod("GET");

            // De binnengekomen data opgevangen en de convertStreamToString wordt geroepen.
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        //String wordt teruggestuurd naar MainActivity
        return response;
    }

    private String convertStreamToString(InputStream is) {
        // De binnengekomen data wordt omgezet in een string.
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
