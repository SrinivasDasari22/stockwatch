package com.srinivas.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder{


    TextView symbol;
    TextView companyName;
    TextView priceChange;
    TextView price;


    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        symbol = itemView.findViewById(R.id.symbol);
        price = itemView.findViewById(R.id.price);
        priceChange = itemView.findViewById(R.id.priceChange);
        companyName = itemView.findViewById(R.id.companyName);

    }
}
