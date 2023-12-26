package com.srinivas.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {


    private static  final String TAG ="MainActivity";

    private RecyclerView recyclerView;

    public static final ArrayList<Stock> stockList = new ArrayList<>();

    private stockAdapter sAdapter;
    private SwipeRefreshLayout swiper;
    private Stock currentStock;
    private EditText symbol2;

    private String text;

    private HashMap<String,String> hashMap;

    private static ArrayList<String> selectedRecords = new ArrayList<>();

    private String stockURL ="https://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recylerView);

        sAdapter = new stockAdapter( this,stockList);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);



        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(this::doRefresh);

        try {
            readJson();
            sAdapter.notifyItemRangeChanged(0,stockList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        NameDownloader.getSourceData(this);
        hashMap = NameDownloader.getNamesHash();


//        for(Stock )
//        StockDetails.getSourceData(this,);

        doRefresh();
        sAdapter.notifyItemRangeChanged(0,stockList.size());

        System.out.println("Dssdasd: " +stockList.size());
        System.out.println(".");





    }





    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void doRefresh() {

        if(hasNetworkConnection()) {

            ArrayList<Stock> tempList = new ArrayList<>();
            tempList.addAll(stockList);
            stockList.clear();

            for (Stock s : tempList) {
                StockDetails.getSourceData(MainActivity.this, s.getStockSymbol());
//                stockList.remove(s);
                notifyAdapter();
            }
            tempList.clear();

//            Toast.makeText(this, "Refreshing Page", Toast.LENGTH_SHORT).show();
            swiper.setRefreshing(false);

        } else{
            AlertDialog.Builder builderS1 = new AlertDialog.Builder(this);
            builderS1.setTitle("No Network Connection");
            builderS1.setMessage("Stocks Can't be updated without a network connection");
            builderS1.show();
            swiper.setRefreshing(false);

        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        try {
            FillJson();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.add_stock,menu);
        return true;

    }



    public boolean onOptionsItemSelected(MenuItem item){


        selectedRecords.clear();


        if(item.getItemId() ==R.id.addStock) {

            if (hasNetworkConnection()) {

                //            Toast.makeText(this , "Adding stock", Toast.LENGTH_SHORT).show();
                //            Intent intent = new Intent(this,AboutActivity.class);
                //            startActivity(intent);
                symbol2 = new EditText(this);
                symbol2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

                symbol2.setGravity(Gravity.CENTER);


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("ok", (dialog, which) -> {

                    text = symbol2.getText().toString();

                    for (Map.Entry<String, String> record : hashMap.entrySet()) {
                        if (record.getKey().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || record.getValue().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                            selectedRecords.add(record.getKey() + " - " + record.getValue());
                        }
                    }
                    System.out.println("ddddd: " + selectedRecords.size());

                    if(selectedRecords.size()==0){
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                        builder4.setTitle("Symbol not found : "+ text);
                        builder4.setMessage("No data for Stock Symbol");
                        builder4.show();
                        return;
                    }
                    else if (selectedRecords.size() == 1) {
                        String t =selectedRecords.get(0);

                        t = t.split("-")[0];
                        t = t.trim();

                        for(Stock tempStock : stockList){
                            if(t.toLowerCase(Locale.ROOT).equals(tempStock.getStockSymbol().toLowerCase(Locale.ROOT))){
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                                builder3.setTitle("Duplicate Stock");
                                builder3.setMessage("Stock Symbol "+tempStock.getStockSymbol()+" is already displayed");
                                builder3.setIcon(R.drawable.duplicate_icon_foreground);
                                builder3.show();
                                return;
                            }
                        }
                        StockDetails.getSourceData(MainActivity.this,t);

                    } else {

                        //                    String temp ="";
                        CharSequence[] cs = selectedRecords.toArray(new CharSequence[selectedRecords.size()]);
                        // setup the alert builder
                        AlertDialog.Builder builderS = new AlertDialog.Builder(this);
                        builderS.setTitle("Make a selection");

                        builderS.setItems(cs, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int poi) {


                                String s  = selectedRecords.get(poi);
                                s = s.split("-")[0];
                                s =s.trim();
                                for(Stock tempStock : stockList){
                                    tempStock.getStockSymbol();
                                    if(s.toLowerCase(Locale.ROOT).equals(tempStock.getStockSymbol().toLowerCase(Locale.ROOT))){
                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                                        builder3.setTitle("Duplicate Stock");
                                        builder3.setMessage("Stock Symbol "+tempStock.getStockSymbol()+" is already displayed");
                                        builder3.setIcon(R.drawable.duplicate_icon_foreground);
                                        builder3.show();
                                        return;
                                    }
                                }

                                StockDetails.getSourceData(MainActivity.this, s);
                                notifyAdapter();
                                System.out.println("dsdsds: " + stockList.size());

                            }
                        });

                        builderS.setNegativeButton("NeverMind", (dialog1, p) -> {
                            dialog1.dismiss();
                        });

                        AlertDialog dialog1 = builderS.create();
                        dialog1.show();

                    }


                });

                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.setView(symbol2);
                builder.setTitle("STOCK SELECTION");
                builder.setMessage("Please enter a Stock Symbol");
                builder.show();
                return true;

            } else{
                AlertDialog.Builder builderS1 = new AlertDialog.Builder(this);
                builderS1.setTitle("No Network Connection");
                builderS1.setMessage("Stocks Can't be added without a network connection");
                builderS1.show();
                return true;
            }

        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void notifyAdapter(){
        sAdapter.notifyItemRangeChanged(0,stockList.size());

    }


    @Override
    public void onClick(View v) {

        int position = recyclerView.getChildLayoutPosition(v);
        Stock temp1 = stockList.get(position);
        System.out.println("dsdsds: "+stockURL+temp1.getStockSymbol());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(stockURL+temp1.getStockSymbol()));

        intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);
//        }

    }

    @Override
    public boolean onLongClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);

        currentStock = stockList.get(position);


        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        builder.setPositiveButton("DELETE",(dialog, which) -> {


            stockList.remove(position);

            sAdapter.notifyItemRemoved(position);
            Collections.sort(stockList);
//            Toast.makeText(this, "Item deleted ", Toast.LENGTH_LONG).show();
            try{
                FillJson();
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        builder.setNegativeButton("NO",(dialog, which) -> {
            dialog.dismiss();
        });

        builder.setTitle(" Delete Stock Symbol  '"+ currentStock.getStockSymbol()+"?" );
        builder.setIcon(R.drawable.delete_icon_foreground);

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    public  void readJson() throws IOException, JSONException {
        FileInputStream fis = getApplicationContext().openFileInput("JSONText.json");
        StringBuilder fileContent = new StringBuilder();

        byte[] buffer = new byte[1024];
        int n;
        while ((n = fis.read(buffer)) != -1)
        {
            fileContent.append(new String(buffer, 0, n));
        }
        JSONArray jsonArray = new JSONArray(fileContent.toString());
        Log.d(TAG, "readFromFile: ");
        stockList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            stockList.add(Stock.createFromJSON(jsonArray.getJSONObject(i)));
            sAdapter.notifyItemInserted(i);
        }
        Collections.sort(stockList);
//        notifyAdapter();

//        sAdapter.notifyItemRangeChanged(0,stockList.size());

        Log.d(TAG, "readFromFile: ");


    }

    public void FillJson() throws JSONException,IOException {
        JSONArray jsonArray = new JSONArray();

        for (Stock stock : stockList) {
            jsonArray.put(stock.toJSON());

        }
        stockList.size();
        FileOutputStream fos = getApplicationContext().openFileOutput("JSONText.json", MODE_PRIVATE);
        PrintWriter pr = new PrintWriter(fos);
        pr.println(jsonArray);
        pr.close();
        fos.close();
//        setTitle("Android Notes ["+noteList.size()+"]");

    }



}