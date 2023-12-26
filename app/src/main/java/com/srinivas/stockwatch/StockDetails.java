package com.srinivas.stockwatch;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

public class StockDetails {

    private static RequestQueue queue;
    private static MainActivity mainActivity;

    private static final String DATA_URL = "https://cloud.iexapis.com/stable/stock/";
    private static final String api ="pk_92e56ddeb51344e1ae05b60d808479a2";



    public static void getSourceData(MainActivity mainActivity1,String s) {
        mainActivity = mainActivity1;
//        s = s.split("-")[0];
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        buildURL.appendPath(s.trim());
        buildURL.appendPath("quote");
        buildURL.appendQueryParameter("token", api);

        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response,mainActivity);

        Response.ErrorListener error =
                error1 -> parseJSON(null,mainActivity);



        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse, null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

//    mainActivity.notifyAdapter();

    }


    private static void parseJSON(JSONObject js,MainActivity m)  {

        try {
            if(js.equals(null)){
                return;
            } else{
                 String stockSymbol = js.getString("symbol");
                 String companyName = js.getString("companyName");
                 double price = js.getDouble("latestPrice");
                 double priceChange = js.getDouble("change");
                 double changePercent = js.getDouble("changePercent");

                 MainActivity.stockList.add(new Stock(stockSymbol,companyName,price,priceChange,changePercent));
                Collections.sort(MainActivity.stockList);
                 mainActivity.FillJson();
                mainActivity.notifyAdapter();





            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
