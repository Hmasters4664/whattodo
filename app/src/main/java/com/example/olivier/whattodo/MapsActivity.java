package com.example.olivier.whattodo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import android.location.Geocoder;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.GeoApiContext;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private final static String KEY_LOCATION = "location";
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private static final int REQUEST_GETMYLOCATION = 0;

    private static final String[] PERMISSION_GETMYLOCATION = new String[] {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};
    private LocationManager mLocationManagerService;
    private boolean mIsListening = false;
    private boolean mIsLocationFound = false;
    private Context mContext;
    MarkerOptions MKO;
    Marker marker;

    /**
     * Settings for the tracker
     */
    //private TrackerSettings mTrackerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManagerService= (LocationManager) getSystemService(LOCATION_SERVICE);

        MKO=new MarkerOptions().position(new LatLng(0,0)).title("Current Location");
       // marker= new Marker(MKO);
        if(Build.VERSION.SDK_INT >=23 && !isPermissionGranted()){
            requestPermissions(PERMISSION_GETMYLOCATION,REQUEST_GETMYLOCATION);
        }else{
            reqLocation();
        }

        if(!isLocationEnabled())
        {
            showAlert(1);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // MapActivityPermissionsDispatcher.getMyLocationWithPermissionCheck(this);
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // MapDemoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng mCoordinates=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mCoordinates));;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCoordinates));
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

    private void reqLocation(){
        Criteria criteria= new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider= mLocationManagerService.getBestProvider(criteria,true);
        mLocationManagerService.requestLocationUpdates(provider,5000,10,this);
    }

    private boolean isPermissionGranted(){

        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED  )
        {
            return true;
        } else
        {
            return false;
        }

    }

    private boolean isLocationEnabled()
    {
        return mLocationManagerService.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManagerService.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void showAlert(final int status)
    {
        String message,title, btnText;
        if (status== 1)
        {
            message="Your Location Settings are off.\n Inorder to use this feature " +
                    "please  enable these settings";
            title="Enable location";
            btnText="Location Settings";
        } else{
            message="Please allow the app to access location";
            title="Permission";
            btnText="Location Permissions";

        }

        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title).setMessage(message)
                .setPositiveButton(btnText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(status==1){
                            Intent intents = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                            startActivity(intents);
                        }else {
                            requestPermissions(PERMISSION_GETMYLOCATION,REQUEST_GETMYLOCATION);;
                        }
                    }

                })
        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();

    }


}
