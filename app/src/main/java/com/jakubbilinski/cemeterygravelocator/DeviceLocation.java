package com.jakubbilinski.cemeterygravelocator;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by bilek on 02.01.2017.
 */

public class DeviceLocation implements LocationListener {

    private LocationManager locationManager;
    private Marker locationMarker;
    private boolean locationSaved;
    private ProgressDialog loadingDialog;

    private IMapController mapController;
    private MapView mapView;
    private Context context;
    private Activity activity;

    public DeviceLocation(IMapController mapController, MapView mapView, Context context, Activity activity) {
        this.mapController = mapController;
        this.mapView = mapView;
        this.context = context;
        this.activity = activity;

        locationSaved = false;
    }

    public void getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        boolean requestedLocation = false;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestedLocation = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            requestedLocation = true;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        if (!requestedLocation) {
            showDialogLocationProviderError();
        } else {
            showLoading();
        }
    }

    private void showDialogLocationProviderError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(context.getString(R.string.no_location_services_title));
        builder.setMessage(context.getString(R.string.no_location_services));

        builder.setPositiveButton(context.getString(R.string.ok_capital), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void drawMarker(GeoPoint geoPoint) {
        if (locationMarker == null)
            locationMarker = new Marker(mapView);

        locationMarker.setPosition(geoPoint);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(locationMarker);
        locationMarker.setTitle(context.getString(R.string.set_location));

        mapView.invalidate();
    }

    private void showLoading() {
        if (loadingDialog == null)
            loadingDialog = ProgressDialog.show(activity, "", context.getString(R.string.wait_loading), true);
    }

    private void hideLoading() {
        loadingDialog.cancel();
    }

    public GeoPoint getMarkerPosition() {
        if (locationMarker == null) {
            return null;
        } else {
            return locationMarker.getPosition();
        }
    }

    public void removeUpdates() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint myPointPosition = new GeoPoint(location);

        if (!locationSaved) {
            drawMarker(myPointPosition);
            mapController.setCenter(myPointPosition);
            hideLoading();
            locationSaved = true;
        } else {
            mapController.animateTo(myPointPosition);
            removeUpdates();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
