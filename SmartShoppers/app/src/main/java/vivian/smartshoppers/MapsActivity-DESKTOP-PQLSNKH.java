package vivian.smartshoppers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.json.*;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import vivian.smartshoppers.AccessFacade.GroceryStoreConnection;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private int which_one = 0 ;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public String userName = "";
    public String helperName = "";
    public String payerName = "";
    public String helperStoreID = "";
    public String payerStoreID = "";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    HashMap<String, String> storeIDBundle;
    HashMap<String, String> storeNameBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        storeIDBundle = new HashMap<>();
        storeNameBundle= new HashMap<>();
        sp = getSharedPreferences(getString(R.string.SPName), 0);
        edit = sp.edit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        //create the locationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)  //10 seconds
                .setFastestInterval(1 * 1000);  // 1 second
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private void setUpMap() {
        which_one = 0;
        mMap.addMarker(new MarkerOptions().position(new LatLng(39.956581, -75.187723)).title("You are here!"));
        mMap.setOnInfoWindowClickListener(this);

        float zoomLevel = (float) 13.2; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.956581, -75.187723), zoomLevel));


        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        LatLng hold = marker.getPosition();
        String latlng = Double.toString(hold.latitude)+Double.toString(hold.longitude);
        edit.putString(getString(R.string.store_id), helperStoreID);
        helperStoreID = storeIDBundle.get(latlng);

        if(which_one == 2){
            HashMap<String, String> accessBundle = new HashMap();
            accessBundle.put("store_id", helperStoreID);
            accessBundle.put("user_id", sp.getString(getString(R.string.user_id), "1"));
            Log.d("user_id", sp.getString(getString(R.string.user_id), "l"));
            accessBundle.put("tag", "users_available_add");
            try {
                String json = new AsyncConnection("UserAvailable").execute(accessBundle).get();
                Log.d("js", json);
            }
            catch(Exception e){
                Log.d("lll", "STILL WRONG");
            }
            Intent intent = new Intent(this, PayerListActivity.class);
            edit.putString(getString(R.string.store_id), helperStoreID);
            edit.commit();
            startActivity(intent);
        }
        else if(which_one == 1){

            Intent intentBundle = new Intent(MapsActivity.this, HelperListActivity.class);
            Bundle bundle = new Bundle();
            startActivity(intentBundle);
        }
        else{
            Log.d("map", "I'm here is clicked");
        }

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        Intent intent = new Intent(this, ConfirmPageActivity.class);
        startActivity(intent);
        return false;
    }

    private void handleNewLocation(Location location) {
        Log.d("mao", "I'm in handle new location!");

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        float zoomLevel = (float) 16.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        Log.d("map", "I got a new location in handle new location!");
    }

    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng Philadelphia = new LatLng(39.952584, -75.165222);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(Philadelphia).title("Marker in Philadelphia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Philadelphia));
    }
    */

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Map", "I'm very close");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.d("map", "location is null");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
        else {
            Log.d("map", "Now I will call handle new location");
            handleNewLocation(location);
        }
        ;

        Log.d("Map", "I passed the check!");

        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("HERE", "DESTROY");
        HashMap<String, String> removeNameBundle = new HashMap<>();
        String user_id = sp.getString(getString(R.string.user_id), "");
        removeNameBundle.put("tag", "users_remove_all");
        removeNameBundle.put("user_id", user_id);
        removeNameBundle.put("store_id", "");
        try {
            new AsyncConnection("UsersAvailable").execute(removeNameBundle);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Grocery_List(View view) throws Exception{
        which_one = 1;
        mMap.clear();
        String json = "l";
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("lat", "39.956581");
        accessBundle.put("lng", "-75.187723");
        try {
            json = new AsyncConnection("GroceryStore").execute(accessBundle).get();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        JSONArray locations = new JSONArray(json);
        for(int i=0;i<locations.length();i++)
        {
            JSONObject index = locations.getJSONObject(i);
            double lat = index.getDouble("lat");
            double lng = index.getDouble("lng");
            String grocer = index.getString("name");
            String address = index.getString("address");
            String store_id = index.getString("store_id");
            String latlng = Double.toString(lat)+Double.toString(lng);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(grocer + ", " + address).snippet("Click to request a helper"));
            storeIDBundle.put(latlng, store_id);
            storeNameBundle.put(latlng, grocer);
        }
        mMap.setOnInfoWindowClickListener(this);
    }
    public void confirm_page(View view) throws Exception {
        which_one = 2;

        mMap.clear();
        String json = "l";
        HashMap<String, String> accessBundle = new HashMap<>();
        accessBundle.put("lat", "39.956581");
        accessBundle.put("lng", "-75.187723");
        try {
            json = new AsyncConnection("GroceryStore").execute(accessBundle).get();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        JSONArray locations = new JSONArray(json);
        for(int i=0;i<locations.length();i++)
        {
            JSONObject index = locations.getJSONObject(i);
            double lat = index.getDouble("lat");
            double lng = index.getDouble("lng");
            String grocer = index.getString("name");
            String address = index.getString("address");
            String store_id = index.getString("store_id");
            String latlng = Double.toString(lat)+Double.toString(lng);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(grocer+", "+address).snippet("Click herer to go to this store"));
            storeIDBundle.put(latlng,store_id);
            storeNameBundle.put(latlng, grocer);        }

    }
}
