package com.jakubbilinski.cemeterygravelocator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewDate;
    private TextView textViewNote;
    private ImageView imageViewPhoto;
    private FloatingActionButton floatingActionButtonMap;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDate = (TextView) findViewById(R.id.textViewDates);
        textViewNote = (TextView) findViewById(R.id.textViewNoteContent);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        setData();
        floatingButton();
    }

    private void floatingButton() {
        floatingActionButtonMap = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddToMap);
        floatingActionButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + getIntent().getExtras().getString(Tags.NAME) + ")";;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    private void setData() {
        DatabaseHelper db = new DatabaseHelper(this);
        Bundle bundle = db.getOneGrave(getIntent().getExtras().getInt(Tags.ID));

        textViewName.setText(getIntent().getExtras().getString(Tags.NAME));
        textViewDate.setText(getIntent().getExtras().getString(Tags.DATE));
        textViewNote.setText(bundle.getString(Tags.NOTE));

        latitude = bundle.getDouble(Tags.MAP_LATITUDE);
        longitude = bundle.getDouble(Tags.MAP_LONGITUDE);

        Bitmap bitmap = bundle.getParcelable(Tags.PHOTO);
        imageViewPhoto.setImageBitmap(bitmap);
    }
}
