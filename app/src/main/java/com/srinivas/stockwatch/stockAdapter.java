package com.srinivas.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class stockAdapter extends RecyclerView.Adapter<StockViewHolder>{


    private MainActivity mainActivity;
    private ArrayList<Stock> stockArrayList;

    public stockAdapter(MainActivity mainActivity, ArrayList<Stock> stockArrayList) {
        this.mainActivity = mainActivity;
        this.stockArrayList = stockArrayList;
    }


    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_entry,parent,false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        Stock stock = stockArrayList.get(position);

        String pc;
        if(stock.getPriceChange()>=0.00) {
            holder.symbol.setTextColor(Color.GREEN);
            holder.companyName.setTextColor(Color.GREEN);
            holder.price.setTextColor(Color.GREEN);
            holder.priceChange.setTextColor(Color.GREEN);
            pc = "▲ " + stock.getPriceChange() + " (" + stock.getChangePercent() + "%)";
        } else{
            holder.symbol.setTextColor(Color.RED);
            holder.companyName.setTextColor(Color.RED);
            holder.price.setTextColor(Color.RED);
            holder.priceChange.setTextColor(Color.RED);

            pc = "▼ " + stock.getPriceChange() + " (" + stock.getChangePercent() + "%)";

        }

        holder.symbol.setText(stock.getStockSymbol());

        holder.companyName.setText(stock.getCompanyName());
        holder.price.setText(String.valueOf(stock.getPrice()));
        holder.priceChange.setText(pc);


    }



    @Override
    public int getItemCount() {
        return stockArrayList.size();
    }



}
