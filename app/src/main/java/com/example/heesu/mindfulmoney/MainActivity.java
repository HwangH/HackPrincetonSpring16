package com.example.heesu.mindfulmoney;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.reimaginebanking.api.java.Constants.TransactionType;
import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.Main;
import com.reimaginebanking.api.java.NessieErrorHandler;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.NessieType;
import com.reimaginebanking.api.java.models.*;
import com.reimaginebanking.api.java.models.RequestResponse;

import java.util.ArrayList;
import java.util.List;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    protected static String key = "f42694af69bea32185ab6de531cff8ab";
    NessieClient nessieClient = NessieClient.getInstance();
    private static String customer = "56c66be6a73e492741507536";
    private static String customerCard = "56c66be7a73e492741508225";
    private static String[] exampleVendor = {"56c66be6a73e492741507624","56c66be6a73e492741507627",
        "56c66be6a73e492741507628", "56c66be6a73e492741507629", "56c66be6a73e49274150762a",
        "56d1c8bc480cf02f0f8880cd", "56d1c8bc480cf02f0f8880d0", "56d1c8bc480cf02f0f8880d5",
        "56d1c8bc480cf02f0f8880d6", "56c66be6a73e4927415076b3"};

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public final static String EXTRA_VENDOR = "com.example.heesu.mindfulmoney.VENDOR";
    public final static String EXTRA_CUSTOMER = "com.example.heesu.mindfulmoney.CUSTOMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nessieClient.setAPIKey(key);

        //formatting
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent)));


        for (int i = 1; i < 10; i++) {
            Purchase.Builder nB = new Purchase.Builder();
            Purchase p = nB.amount(i + 0.01)
                    .description("for java")
                    .medium("balance")
                    .purchase_date("2016-04-02")
                    .status("pending")
                    .merchant(exampleVendor[i])
                    .build();
            nessieClient.createPurchase(customerCard, p, new NessieResultsListener() {
                    @Override
                    public void onSuccess(Object result, NessieException e) {
                        if (e == null) { }
                        else { //There was an error. Handle it here
                             Log.e("Error", e.toString());
                        }
                    }
            });
        }

        nessieClient.getMerchants(new NessieResultsListener() {
            @Override
            public void onSuccess(Object result, NessieException e) {
                if (e == null) {
                    List<Merchant> a = (List<Merchant>) result;
                    ArrayList<Merchant> merchants = new ArrayList<Merchant>();
                    for (Merchant x : a) {
                        merchants.add(x);
                    }
                    ListView test = (ListView) findViewById(R.id.left_drawer);
                    test.setTag(merchants);
                    nessieClient.getPurchases(customerCard, new NessieResultsListener() {
                        @Override
                        public void onSuccess(Object result, NessieException e) {
                            if (e == null) {
                                List<Purchase> a = (List<Purchase>) result;

                                LinearLayout myRoot = (LinearLayout) findViewById(R.id.root);
                                LinearLayout list = new LinearLayout(MainActivity.this);
                                list.setOrientation(LinearLayout.VERTICAL);

                                ListView test = (ListView) findViewById(R.id.left_drawer);
                                ArrayList<Merchant> copy = (ArrayList<Merchant>) test.getTag();
                                for (int i = a.size()-1; i >= 0; i--) {
                                    Purchase x = a.get(i);
                                    Merchant merch = getMerchantName(copy, x.getMerchant_id());

                                    TextView newText = (TextView) new TextView(MainActivity.this);
                                    StringBuilder purchase = new StringBuilder();
                                    purchase.append("\nDate: " + x.getPurchase_date() + "\n");
                                    purchase.append("Cost: " + x.getAmount() + "\n");
                                    purchase.append("Purchased from: " + merch.getName() + "\n");
                                    newText.setText(purchase.toString());
                                    newText.setTextColor(Color.WHITE);
                                    newText.setTag(merch);

                                    newText.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent newPage = new Intent(MainActivity.this, DisplayStats.class);

                                            Gson converter = new Gson();
                                            newPage.putExtra(EXTRA_VENDOR, converter.toJson((Merchant) view.getTag()));
                                            newPage.putExtra(EXTRA_CUSTOMER, customerCard);
                                            startActivity(newPage);
                                        }
                                    });
                                    list.addView(newText);

                                    View dividerLine = new View(MainActivity.this);
                                    dividerLine.setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 2));
                                    dividerLine.setBackgroundColor(Color.WHITE);
                                    list.addView(dividerLine);

                                }
                                myRoot.addView(list);
                            } else {
                                //There was an error. Handle it here
                                Log.e("Error", e.toString());
                            }
                        }
                    });
                } else {
                    //There was an error. Handle it here
                    Log.e("Error", e.toString());
                }
            }
        });

    }

    public Merchant getMerchantName(ArrayList<Merchant> merchants, String merchId) {
        for (Merchant x : merchants) {
            if (x.get_id().equals(merchId)) return x;
        }
        return null;
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
