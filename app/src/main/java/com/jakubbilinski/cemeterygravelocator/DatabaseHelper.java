package com.jakubbilinski.cemeterygravelocator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilek on 04.01.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_CURRENT_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase.db";

    private final String TABLE_GRAVES = "graves";
    private final String GRAVES_ID = "id";
    private final String GRAVES_NAME = "name";
    private final String GRAVES_BIRTH = "birth";
    private final String GRAVES_DEATH = "death";
    private final String GRAVES_LATITUDE = "lati";
    private final String GRAVES_LONGITUDE = "long";
    private final String GRAVES_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_GRAVES + " (" + GRAVES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GRAVES_NAME + " TEXT, " + GRAVES_BIRTH + " TEXT, " + GRAVES_DEATH + " TEXT, "
                + GRAVES_LATITUDE + " TEXT, " + GRAVES_LONGITUDE + " TEXT, "
                + GRAVES_PHOTO + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // There is only one version possible. If version is other than default, reset database.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRAVES);
        onCreate(db);
    }

    private byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap bytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void insertGrave(String name, String birth, String death, double latitude, double longitude, Bitmap photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(GRAVES_NAME,name);
        contentValues.put(GRAVES_BIRTH,birth);
        contentValues.put(GRAVES_DEATH,death);
        contentValues.put(GRAVES_LATITUDE,String.valueOf(latitude));
        contentValues.put(GRAVES_LONGITUDE,String.valueOf(longitude));
        contentValues.put(GRAVES_PHOTO,bitmapToBytes(photo));

        db.insert(TABLE_GRAVES, null, contentValues);
    }

    private Cursor getGravesCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_GRAVES, null);
        return res;
    }

    public List<Grave> getAllGraves() {
        Cursor cursor = getGravesCursor();

        if (cursor.getCount() == 0)
            return new ArrayList<>();

        List<Grave> graves = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            graves.add(new Grave(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
        }

        return graves;
    }

    private Cursor getGravesCursorById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_GRAVES + " WHERE " + GRAVES_ID + " = " + id, null);
        return res;
    }

    public Bundle getOneGrave(int id) {
        Bundle bundle = new Bundle();
        Cursor res = getGravesCursorById(id);
        res.moveToNext();

        Bitmap bitmap = bytesToBitmap(res.getBlob(6));
        bundle.putParcelable(Tags.PHOTO, bitmap);

        bundle.putDouble(Tags.MAP_LATITUDE,res.getDouble(4));
        bundle.putDouble(Tags.MAP_LONGITUDE,res.getDouble(5));

        return bundle;
    }
}
