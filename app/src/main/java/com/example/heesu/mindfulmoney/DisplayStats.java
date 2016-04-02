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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorPrimary)));
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_VENDOR);
        TextView textView = (TextView) findViewById(R.id.stats_text);
        StringBuilder yourAverage = new StringBuilder();
        yourAverage.append("Your average purchase from VENDOR cost: \n"+message);
        textView.setText(yourAverage.toString());
        textView.setTextColor(Color.WHITE);


        TextView allData = (TextView) findViewById(R.id.all_data);
        allData.setText("The average consumer's purchase from VENDOR costs: \n");
        allData.setTextColor(Color.WHITE);


        TextView rec = (TextView) findViewById(R.id.recommend);
        rec.setText("We suggest that you \n");
        rec.setTextColor(Color.WHITE);


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
