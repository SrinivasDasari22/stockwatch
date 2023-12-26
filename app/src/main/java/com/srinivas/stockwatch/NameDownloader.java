package com.srinivas.stockwatch;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractQueue;
import java.util.HashMap;

public class NameDownloader {
    private static RequestQueue queue;
    private static MainActivity mainActivity;

    private static final String DATA_URL = "https://cloud.iexapis.com/stable/ref-data/symbols";
    private static final String api ="pk_92e56ddeb51344e1ae05b60d808479a2";

    public static final HashMap<String,String> namesHash = new HashMap<String,String>();


    public static void getSourceData(MainActivity mainActivity1) {
        mainActivity = mainActivity1;
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        buildURL.appendQueryParameter("token", api);

        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONArray> listener =
                response -> parseJSON(response);

        Response.ErrorListener error =
                error1 -> parseJSON(null);


        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    public static HashMap<String ,String> getNamesHash(){
        return namesHash;
    }
    private static void parseJSON(JSONArray js)  {

        try {
            if(js.equals(null)){
                return;
            }


            for (int i = 0; i < js.length(); i++) {
                String stockSymbol;
                String companyName;
                JSONObject jStock = (JSONObject) js.get(i);
                stockSymbol = jStock.getString("symbol");
                companyName = jStock.getString("name");
                if(!stockSymbol.equals("") || !companyName.equals("")){
                    namesHash.put(stockSymbol,companyName);
                }

            }
            System.out.println("ssss:  "+ namesHash.size());

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
