package com.jakubbilinski.cemeterygravelocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class MapSelectActivity extends AppCompatActivity implements MapEventsReceiver {

    private MapView map;
    private IMapController mapController;
    private MapEventsOverlay mapEventsOverlay;
    private DeviceLocation deviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);

        PermissionChecker.checkForPermissions(this);
        enableMaps();
        setOverlay();

        deviceLocation = new DeviceLocation(mapController,map,this,this);
        deviceLocation.getLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceLocation.removeUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_select,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_set_location) {
            Intent resultData = new Intent();

            GeoPoint geoReturnPoint = deviceLocation.getMarkerPosition();
            if (geoReturnPoint == null) {
                resultData.putExtra("Lat", 0);
                resultData.putExtra("Lon", 0);
            } else {
                resultData.putExtra(Tags.MAP_LATITUDE, geoReturnPoint.getLatitude());
                resultData.putExtra(Tags.MAP_LONGITUDE, geoReturnPoint.getLongitude());
            }

            setResult(Activity.RESULT_OK, resultData);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setOverlay() {
        mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);
    }

    private void enableMaps() {
        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(20);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        InfoWindow.closeAllInfoWindowsOn(map);
        deviceLocation.drawMarker(geoPoint);

        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        return false;
    }
}
