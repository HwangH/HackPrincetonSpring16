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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String key = "key=f42694af69bea32185ab6de531cff8ab";
    private static String customer = "56c66be7a73e492741508223";
    private static String exampleVendor = "56c66be7a73e492741508223";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public final static String EXTRA_VENDOR = "com.example.heesu.mindfulmoney.VENDOR";
    public final static String EXTRA_AVERAGE = "com.example.heesu.mindfulmoney.AVERAGE";
    public static String average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 5; i++) {
            try {ServerCall call = new ServerCall(customer, exampleVendor, 5);
                call.execute();
            }
            catch (Exception E) {
                System.err.println("still network error?");
                System.err.println(E);
            }
        }


        ArrayList<Purchases> allPurchases = new ArrayList<Purchases>();
        String purchases;
        try {
            purchases = Purchase.getPurchase_Account(key);
            PurchasesList make = new PurchasesList(purchases);
            allPurchases = make.getList();
        } catch (Exception ex) {
              System.err.println(ex);
        }

        average = "" + 5;


        //Creates the list of purchase history.
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.root);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        for (Purchases x : allPurchases) {
            TextView newText = (TextView) new TextView(this);
            StringBuilder purchase = new StringBuilder();
            purchase.append("\nDate: "+ x.purchase_date +"\n");
            purchase.append("Cost: " + x.amount + "\n");
            purchase.append("Purchased from: " + x.merchant_id + "\n");
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


    private class ServerCall extends AsyncTask<Void, Void, Boolean> {

        private final String a;
        private final String b;
        private final int c;

        ServerCall(String a, String b, int c) {
            this.a = a; this.b = b; this.c =c;
        }


        protected Boolean doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            try {
                System.err.println(Purchase.putPurchase(a, b, c));
            } catch (Exception E) {
                System.err.println(E);
                return false;
            }
            return true;
        }


        protected void onPostExecute(final Boolean success) {
            return;
        }

        protected void onCancelled() {
            return;
        }
    }





}
