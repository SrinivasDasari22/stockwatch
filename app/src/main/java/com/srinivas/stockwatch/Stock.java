package com.srinivas.stockwatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Stock implements Comparable<Stock>, Serializable {

    private static final DecimalFormat df = new DecimalFormat("0.00");


    private String stockSymbol;
    private String companyName;
    private double price;
    private double priceChange;
    private double changePercent;

    public Stock(String stockSymbol, String companyName, double price, double priceChange, double changePercent) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.price = Double.parseDouble(df.format(price));
        this.priceChange = Double.parseDouble(df.format(priceChange));
        this.changePercent = Double.parseDouble(df.format(changePercent));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("stockSymbol",stockSymbol);
        obj.put("companyName",companyName);
        obj.put("price",0.00);
        obj.put("priceChange",0.00);
        obj.put("changePercent",0.00);


        return obj;

    }

    public static Stock createFromJSON(JSONObject jsonObject) throws JSONException {
        String stockSymbol = jsonObject.getString("stockSymbol");
        String companyName = jsonObject.getString("companyName");
        double price = jsonObject.getDouble("price");
        double priceChange = jsonObject.getDouble("priceChange");
        double changePercent = jsonObject.getDouble("changePercent");

        return new Stock(stockSymbol, companyName, price,priceChange,changePercent);

    }


    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    @Override
    public int compareTo(Stock stock) {

        return stockSymbol.compareTo(stock.stockSymbol);
    }
}
