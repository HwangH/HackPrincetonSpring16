package com.example.heesu.mindfulmoney;

import com.google.gson.Gson;

public class Purchases {

    String _id, merchant_id, medium, purchase_date, status, description, type, payer_id;

    int amount;

    public Purchases(String idPurchase, String merchant_id, String medium, String purchase_date,
                     int amount, String status, String description, String type, String payer_id) {
        super();
        this._id = idPurchase;
        this.merchant_id = merchant_id;
        this.purchase_date = purchase_date;
        this.amount = amount;
        this.status = status;
        this.description = description;
        this.type = type;
        this.payer_id = payer_id;
    }

    @Override
    public String toString() {
        return "\nModelObject [ \n _id=" + _id + ", \nmerchant_id=" + merchant_id + ", \nmedium=" + medium +
                ", \npurchase_date=" + purchase_date + ", \namount=" + amount + ", \nstatus=" + status +
                ", \ndescription=" + description + ", \ntype=" + type + ", \npayer_id=" + payer_id + "\n]";
    }

    public static void main(String[] args) {
        final Gson gson = new Gson();

        String json = args[0];

        System.out.println("fromJson----");
        // getting object from json representation
        System.out.println("Original JSON string is : " + json);
        // converting json to object
        Purchases modelPurchase1 = gson.fromJson(json, Purchases.class);
        System.out.println("Converted Java object : " + modelPurchase1);
    }

}
