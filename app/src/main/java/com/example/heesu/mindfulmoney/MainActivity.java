package com.example.heesu.mindfulmoney;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.reimaginebanking.api.java.Constants.TransactionType;
import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.Main;
import com.reimaginebanking.api.java.NessieErrorHandler;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.NessieType;
import com.reimaginebanking.api.java.models.Customer;
import com.reimaginebanking.api.java.models.Purchase;
import com.reimaginebanking.api.java.models.RequestResponse;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private static String key = "f42694af69bea32185ab6de531cff8ab";
    NessieClient nessieClient;
    private static String customer = "56c66be6a73e492741507536";
    private static String customerCard = "56fef93d480cf02f0f88a56d";
    private static String exampleVendor = "56c66be6a73e492741507624";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public final static String EXTRA_VENDOR = "com.example.heesu.mindfulmoney.VENDOR";
    public final static String EXTRA_AVERAGE = "com.example.heesu.mindfulmoney.AVERAGE";
    public static String average;
    public static ArrayList<Purchase> buys;

    private void setBuys(ArrayList<Purchase> buy){
        buys = buy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nessieClient = NessieClient.getInstance();
        nessieClient.setAPIKey(key);



        for (int i = 1; i < 10; i++) {
            Purchase.Builder nB = new Purchase.Builder();
            Purchase p = nB.amount(i + 0.01)
                    .description("for java")
                    .medium("balance")
                    .purchase_date("2016-04-02")
                    .status("pending")
                    .merchant(exampleVendor)
                    .build();
            nessieClient.createPurchase(customerCard, p, new NessieResultsListener() {
                    @Override
                    public void onSuccess(Object result, NessieException e) {
                    System.err.println("success creating purchase!");
                }
            });


        }

        System.err.println("this is 5");


        ArrayList<Purchase> buys = new ArrayList<Purchase>();
        nessieClient.getPurchases(customerCard, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result, NessieException e) {
                if (e == null) {
                    //There is no error, do whatever you need here.
                    // Cast the result object to the type that you are requesting and you are good to go
                    setBuys((ArrayList<Purchase>) result);
                } else {
                    //There was an error. Handle it here
                    Log.e("Error", e.toString());
                }
            }
        });
        if (buys.size() > 0) System.err.println("non-zero arraylist");
        System.err.println("hi");
        System.err.println(buys.size());
        //Creates the list of purchase history.
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.root);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        for (Purchase x : buys) {
            TextView newText = (TextView) new TextView(this);
            StringBuilder purchase = new StringBuilder();
            purchase.append("\nDate: "+ x.getPurchase_date() +"\n");
            purchase.append("Cost: " + x.getAmount() + "\n");
            purchase.append("Purchased from: " + x.getMerchant_id() + "\n");
            newText.setText(purchase.toString());
            newText.setTextColor(Color.WHITE);

            newText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPage = new Intent(MainActivity.this, DisplayStats.class);
                    newPage.putExtra(EXTRA_VENDOR, ((TextView) view).getText());
                    newPage.putExtra(EXTRA_AVERAGE, average);
                    startActivity(newPage);
                }
            });
            list.addView(newText);

            View dividerLine = new View(this);
            dividerLine.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 2));
            dividerLine.setBackgroundColor(Color.WHITE);
            list.addView(dividerLine);

        }
        myRoot.addView(list);

        //formatting
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorPrimary)));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
