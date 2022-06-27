package com.example.garden_planner.models;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FrostDateClient {
    private static final String API_BASE_URL = "https://api.farmsense.net/v1/frostdates";
    private AsyncHttpClient client = new AsyncHttpClient();

    public FrostDateClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void getStations(final double latitude, final double longitude, JsonHttpResponseHandler handler){
            String url = getApiUrl("/stations/?lat="+latitude+"&lon="+longitude);
            client.get(url, handler);
    }
}
