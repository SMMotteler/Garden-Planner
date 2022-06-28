package com.example.garden_planner.models;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class HardinessZoneClient {
    private static final String API_BASE_URL = "https://phzmapi.org/";
    private AsyncHttpClient client = new AsyncHttpClient();

    public HardinessZoneClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl + "json";
    }

    public void getHardinessZone(String zipCode, JsonHttpResponseHandler handler){
        String url = getApiUrl(zipCode);
        client.get(url, handler);
    }
}
