package com.jakubbilinski.cemeterygravelocator;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewDate;
    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDate = (TextView) findViewById(R.id.textViewDates);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        setData();
    }

    private void setData() {
        DatabaseHelper db = new DatabaseHelper(this);
        Bundle bundle = db.getOneGrave(getIntent().getExtras().getInt(Tags.ID));

        textViewName.setText(getIntent().getExtras().getString(Tags.NAME));
        textViewDate.setText(getIntent().getExtras().getString(Tags.DATE));

        Bitmap bitmap = bundle.getParcelable(Tags.PHOTO);
        imageViewPhoto.setImageBitmap(bitmap);
    }
}
