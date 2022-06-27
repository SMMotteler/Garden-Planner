package com.example.garden_planner.models;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.garden_planner.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GeocodingClient {
    private static final String API_BASE_URL = "http://api.positionstack.com/v1/";
    private static final String ACCESS_KEY = BuildConfig.API_ACCESS_KEY;
    private AsyncHttpClient client;

    public GeocodingClient()
    {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void forwardGeocoding(String address, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("forward?"+ACCESS_KEY+"&");
            client.get(url + URLEncoder.encode(address, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void reverseGeocoding(double latitude, double longitude, JsonHttpResponseHandler handler){
        try {
            String url = getApiUrl("reverse?"+ACCESS_KEY+"&");
            client.get(url + URLEncoder.encode(latitude+","+longitude, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
