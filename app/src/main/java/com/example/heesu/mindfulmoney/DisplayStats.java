package com.example.heesu.mindfulmoney;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.models.Merchant;
import com.reimaginebanking.api.java.models.Purchase;
import com.reimaginebanking.api.java.requests.NessieService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DisplayStats extends AppCompatActivity {

    public Merchant merch;
    NessieClient nessieClient = NessieClient.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent)));

        Intent intent = getIntent();
        String v = intent.getStringExtra(MainActivity.EXTRA_VENDOR);
        Gson gs = new Gson();
        merch = gs.fromJson(v, Merchant.class);
        String customerCard = intent.getStringExtra(MainActivity.EXTRA_CUSTOMER);


        nessieClient.setAPIKey(MainActivity.key);
        nessieClient.getPurchases(customerCard, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result, NessieException e) {
                if (e == null) {
                    List<Purchase> a = (List<Purchase>) result;
                    //Creates the list of purchase history.
                    float sum = 0;
                    long count = 0;
                    for (Purchase x : a) {
                        if (x.getMerchant_id() == merch.get_id()) {
                            sum += x.getAmount();
                            count++;
                        }
                    }
                    float indivAverage = sum / count;

                    TextView test = (TextView) findViewById(R.id.stats_text);
                    test.setTag(indivAverage);

                    System.err.println(merch.toString());

                    nessieClient.getPurchasem(merch.get_id(), new NessieResultsListener() {
                        @Override
                        public void onSuccess(Object result, NessieException f) {
                            if (f == null) {
                                List<Purchase> a = (List<Purchase>) result;
                                float sum = 0;
                                for (Purchase x : a) {
                                    sum+= x.getAmount();
                                }
                                float overallAv = sum / a.size();





                                TextView textView = (TextView) findViewById(R.id.stats_text);
                                float indivAverage = (float) textView.getTag();
                                StringBuilder yourAverage = new StringBuilder();
                                yourAverage.append("Your average purchase from " + merch.getName() + " cost: " + indivAverage);
                                textView.setText(yourAverage.toString());
                                textView.setTextColor(Color.WHITE);


                                TextView allData = (TextView) findViewById(R.id.all_data);
                                allData.setText("The average consumer's purchase from "+ merch.getName()
                                                    + " costs: " + overallAv + "\n");
                                allData.setTextColor(Color.WHITE);


                                TextView rec = (TextView) findViewById(R.id.recommend);
                                if (overallAv >= indivAverage)
                                    rec.setText("We suggest that you spend less on "+merch.getName()+"!\n");
                                else
                                    rec.setText("Good spending habits :)");
                                rec.setTextColor(Color.WHITE);
                            } else {
                                //There was an error. Handle it here
                                Log.e("Error", f.toString());
                            }
                        }
                    });

                }
                else {
                }
            }
        });
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
