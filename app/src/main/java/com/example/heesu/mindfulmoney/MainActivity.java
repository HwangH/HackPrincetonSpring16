package com.example.heesu.mindfulmoney;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public final static String EXTRA_VENDOR = "com.mycompany.myfirstapp.VENDOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates the list of purchase history.
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.root);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 100; i++) {
            TextView newText = (TextView) new TextView(this);
            StringBuilder purchase = new StringBuilder();
            purchase.append("\nDate: \n");
            purchase.append("Cost: \n");
            purchase.append("Purchased from: \n");
            newText.setText(purchase.toString());
            newText.setTextColor(Color.WHITE);

            newText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPage = new Intent(MainActivity.this, DisplayStats.class);
                    newPage.putExtra(EXTRA_VENDOR, ((TextView) view).getText());
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

        //sets up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
