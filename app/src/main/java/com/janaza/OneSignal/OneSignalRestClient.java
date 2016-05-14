package com.janaza.OneSignal;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class OneSignalRestClient {

    public static class ResponseHandler {
        public void onSuccess(String response) {
        }

        public void onFailure(int statusCode, String response, Throwable throwable) {
        }
    }

    private static final String BASE_URL = "https://onesignal.com/api/v1/";
    private static final int TIMEOUT = 120000;
    public static String REST_API_KEY = "";
    public static String APP_ID = "";

    public static void put(final String url, final JSONObject jsonBody, final ResponseHandler responseHandler) {

        new Thread(new Runnable() {
            public void run() {
                makeRequest(url, "PUT", jsonBody, responseHandler);
            }
        }).start();
    }

    public static void post(final String url, final JSONObject jsonBody, final ResponseHandler responseHandler) {
        new Thread(new Runnable() {
            public void run() {
                makeRequest(url, "POST", jsonBody, responseHandler);
            }
        }).start();
    }

    public static void getSync(final String url, final ResponseHandler responseHandler) {
        makeRequest(url, null, null, responseHandler);
    }

    public static void putSync(String url, JSONObject jsonBody, ResponseHandler responseHandler) {
        makeRequest(url, "PUT", jsonBody, responseHandler);
    }

    public static void postSync(String url, JSONObject jsonBody, ResponseHandler responseHandler) {
        makeRequest(url, "POST", jsonBody, responseHandler);
    }

    private static void makeRequest(String url, String method, JSONObject jsonBody, ResponseHandler responseHandler) {
        HttpURLConnection con = null;
        int httpResponse = -1;
        String json = null;
        try {
            jsonBody.put("app_id", APP_ID);
            Log.d("OneSignalRestClient ", BASE_URL + url);
            con = (HttpURLConnection) new URL(BASE_URL + url).openConnection();
            con.setUseCaches(false);
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            if (jsonBody != null)
                con.setDoInput(true);

            if (method != null) {
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic " + REST_API_KEY);
                con.setRequestMethod(method);
                con.setDoOutput(true);
            }

            if (jsonBody != null) {
                String strJsonBody = jsonBody.toString();
                Log.d("OneSignalRestClient ", method + " SEND JSON: " + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);
            }

            httpResponse = con.getResponseCode();

            InputStream inputStream;
            Scanner scanner;
            if (httpResponse == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
                scanner = new Scanner(inputStream, "UTF-8");
                json = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
                Log.d("OneSignalRestClient ", method + " RECEIVED JSON: " + json);

                if (responseHandler != null)
                    responseHandler.onSuccess(json);
            } else {
                inputStream = con.getErrorStream();
                if (inputStream == null)
                    inputStream = con.getInputStream();

                if (inputStream != null) {
                    scanner = new Scanner(inputStream, "UTF-8");
                    json = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                    Log.d("OneSignalRestClient ", method + " RECEIVED JSON: " + json);
                } else
                    Log.d("OneSignalRestClient ", method + " HTTP Code: " + httpResponse + " No response body!");

                if (responseHandler != null)
                    responseHandler.onFailure(httpResponse, json, null);
            }
        } catch (Throwable t) {
            if (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException)
                Log.d("OneSignalRestClient ", "Could not send last request, device is offline. Throwable: " + t.getClass().getName());
            else
                Log.d("OneSignalRestClient ", method + " Error thrown from network stack. ", t);

            if (responseHandler != null)
                responseHandler.onFailure(httpResponse, null, t);
        } finally {
            if (con != null)
                con.disconnect();
        }
    }
}
