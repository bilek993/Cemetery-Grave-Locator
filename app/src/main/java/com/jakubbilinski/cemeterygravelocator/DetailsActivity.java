package com.jakubbilinski.cemeterygravelocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDate = (TextView) findViewById(R.id.textViewDates);
        setData();
    }

    private void setData() {
        textViewName.setText(getIntent().getExtras().getString(Tags.NAME));
        textViewDate.setText(getIntent().getExtras().getString(Tags.DATE));
    }
}
