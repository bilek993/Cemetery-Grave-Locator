package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class AddGraveActivity extends AppCompatActivity {

    private final int MAP_REQUEST_ID = 73;
    private final int IMAGE_REQUEST_ID = 92;

    private double latitude = 0;
    private double longitude = 0;

    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grave);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        setButtons();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_grave,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save_and_add) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setButtons() {
        Button buttonEditLocation = (Button) findViewById(R.id.buttonEditLocation);
        buttonEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapSelectActivity.class);
                startActivityForResult(intent,MAP_REQUEST_ID);
            }
        });

        Button buttonEditImage = (Button) findViewById(R.id.buttonEditPhoto);
        buttonEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void updateUI() {
        textViewLatitude.setText(getString(R.string.latitude) + " " + latitude);
        textViewLongitude.setText(getString(R.string.longitude) + " " + longitude);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_img)), IMAGE_REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MAP_REQUEST_ID) {
                latitude = data.getDoubleExtra(Tags.MAP_LATITUDE, 0);
                longitude = data.getDoubleExtra(Tags.MAP_LONGITUDE, 0);
            } else if (requestCode == IMAGE_REQUEST_ID) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    Picasso.with(this).load(imageUri).fit().centerInside().into(imageViewPhoto);
                }
            }
        }

        updateUI();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
