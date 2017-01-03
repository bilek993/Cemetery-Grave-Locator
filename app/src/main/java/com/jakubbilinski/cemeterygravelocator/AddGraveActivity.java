package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddGraveActivity extends AppCompatActivity {

    private final int MAP_REQUEST_ID = 73;

    private double latitude = 0;
    private double longitude = 0;

    private TextView textViewLatitude;
    private TextView textViewLongitude;

    Button buttonEditLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grave);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);

        setButtons();
        updateUI();
    }

    private void setButtons() {
        buttonEditLocation = (Button) findViewById(R.id.buttonEditLocation);
        buttonEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapSelectActivity.class);
                startActivityForResult(intent,MAP_REQUEST_ID);
            }
        });
    }

    private void updateUI() {
        textViewLatitude.setText(getString(R.string.latitude) + " " + latitude);
        textViewLongitude.setText(getString(R.string.longitude) + " " + longitude);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                latitude = data.getDoubleExtra(Tags.MAP_LATITUDE,0);
                longitude = data.getDoubleExtra(Tags.MAP_LONGITUDE,0);
            }
        }

        updateUI();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
