package com.main.garden_planner.models;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.garden_planner.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GeocodingClient {
    private static final String API_BASE_URL =  "https://maps.googleapis.com/maps/api/geocode/json?";

    private static final String ACCESS_KEY = BuildConfig.MAPS_KEY;
    private AsyncHttpClient client;

    public GeocodingClient()
    {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void forwardGeocoding(String address, JsonHttpResponseHandler handler)  {
        try {
            String url = getApiUrl("address="+URLEncoder.encode(address, "utf-8")+"&key="+ACCESS_KEY);
            client.get(url, handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void reverseGeocoding(double latitude, double longitude, JsonHttpResponseHandler handler) {
            String url = getApiUrl("latlng="+latitude+","+longitude+"&key="+ACCESS_KEY);
            client.get(url, handler);
    }
}
