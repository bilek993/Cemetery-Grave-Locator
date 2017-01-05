package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;

public class AddGraveActivity extends AppCompatActivity {

    private final int MAP_REQUEST_ID = 73;
    private final int IMAGE_REQUEST_ID = 92;

    private double latitude = 0;
    private double longitude = 0;

    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private EditText editTextName;
    private EditText editTextBirth;
    private EditText editTextDeath;
    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grave);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextBirth = (EditText) findViewById(R.id.editTextBirth);
        editTextDeath = (EditText) findViewById(R.id.editTextDeath);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        setButton();
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
            if (checkEnteredData(editTextName.getText().toString(),editTextBirth.getText().toString(), editTextDeath.getText().toString())) {
                DatabaseHelper db = new DatabaseHelper(this);
                Bitmap bitmap = ((BitmapDrawable) imageViewPhoto.getDrawable()).getBitmap();
                db.insertGrave(editTextName.getText().toString(), editTextBirth.getText().toString(), editTextDeath.getText().toString(), latitude, longitude, bitmap);

                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkEnteredData(String name, String data1, String data2) {
        if (name.isEmpty() || data1.isEmpty() || data2.isEmpty()) {
            showWrongDataDialog(getString(R.string.error_empty_text_boxes));
            return false;
        }
        if (!data1.matches("[0-9]{2}[.][0-9]{2}[.][0-9]{2}")) {
            showWrongDataDialog(getString(R.string.error_birth_bad_format));
            return false;
        }
        if (!data2.matches("[0-9]{2}[.][0-9]{2}[.][0-9]{2}")) {
            showWrongDataDialog(getString(R.string.error_death_bad_format));
            return false;
        }

        return true;
    }

    private void showWrongDataDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.saving_error));
        builder.setMessage(message);

        builder.setPositiveButton(R.string.ok_capital, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setButton() {
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
