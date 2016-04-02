package com.example.heesu.mindfulmoney;




import java.lang.StringBuilder;
import java.util.ArrayList;

import com.google.gson.Gson;

public class PurchasesList {

    private ArrayList<Purchases> purchasesList;

    public PurchasesList(String list) {
        this.purchasesList = new ArrayList<Purchases>();

        int bracket1, bracket2;
        String subList;

        StringBuilder fullList = new StringBuilder(list);

        while (fullList.indexOf("{") >= 0) {

            bracket1 = fullList.indexOf("{");
            bracket2 = fullList.indexOf("}");

            subList = fullList.substring(bracket1, bracket2+1);
            purchasesList.add(create(subList));

            fullList = fullList.delete(0, bracket2+1);
        }

    }

    public boolean upload(Purchases purchase) {

        boolean success = false;

        //NOT WORKINGGGGGG

        return success;
    }

    public Purchases create(String json) {
        final Gson gson = new Gson();
        Purchases newPurchase = gson.fromJson(json, Purchases.class);
        return newPurchase;
    }

    public int uploadAll() {

        int nodes = 0;
        boolean success;

        for (Purchases i : purchasesList) {
            success = upload(i);
            if (success) nodes++;
        }
        return nodes;
    }

    public ArrayList<Purchases> getList(){

        return purchasesList;
    }

    public static void main(String[] args) {

    }
}
