package com.example.reto1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATIONP_CODE = 757;


    private GoogleMap mMap;
    private Boolean mLocationPermissionSuccessful = false;
    private FusedLocationProviderClient mFLPC;
    private LocationManager lmanager;
    private LocationListener llistener;
    private Marker tempMarker;
    private Marker myLocation;
    private Button btnAddMarker;
    private EditText txtNearMarker;
    private EditText txtDistance;
    private ArrayList<Marker> markers;
    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tempMarker = new Marker();
        myLocation = new Marker();
        myLocation.setName("My device");
        markers = new ArrayList<>();
        btnAddMarker = findViewById(R.id.btnAddMarker);
        txtNearMarker = findViewById(R.id.txtNearMarker);
        txtDistance = findViewById(R.id.txtDistance);
        geocoder = new Geocoder(MapsActivity.this);
        btnAddMarker.setOnClickListener(
                (v) -> {
                    Intent i = new Intent(this, AddMarkerActivity.class);
                    i.putExtra("tempMarker", tempMarker);
                    startActivityForResult(i, 11);

                }
        );
        lmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        llistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        if (mLocationPermissionSuccessful) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);

        }
    }

    public List<Address> geoLocate(LatLng location){
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(location.latitude,location.longitude,1);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return addresses;
    }

    public void addNewMarker(){
        LatLng location = new LatLng(tempMarker.getLatitude(),tempMarker.getLongitude());
        Marker x = new Marker();
        x.setName(tempMarker.getName());
        x.setLatitude(tempMarker.getLatitude());
        x.setLongitude(tempMarker.getLongitude());
        double distance = x.calculateDistance(myLocation);
        mMap.addMarker(new MarkerOptions().position(location).title("Name: "+ tempMarker.getName() + "- Distance: " + distance + " k.m."));
        markers.add(x);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionSuccessful = true;
                lmanager.requestLocationUpdates("gps", 5000, 0, llistener);
            }
            else{
                ActivityCompat.requestPermissions(this, permissions,LOCATIONP_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions,LOCATIONP_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionSuccessful =  false;
        switch(requestCode){
            case LOCATIONP_CODE:{
                if(grantResults.length > 0){
                    for (int i  = 0; i< grantResults.length; i++){
                        if( grantResults[i] == PackageManager.PERMISSION_DENIED){
                            return;
                        }
                    }
                    mLocationPermissionSuccessful = true;
                }
            }

        }
    }

    private void getDeviceLocation(){
        mFLPC = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionSuccessful){
                final Task location = mFLPC.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("Map: ", "Found location");
                            Location current = (Location) task.getResult();

                            myLocation.setLatitude(current.getLatitude());
                            myLocation.setLongitude(current.getLongitude());
                            LatLng me = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                            List<Address> e = geoLocate(me);
                            mMap.addMarker(new MarkerOptions().position(me).title(e.get(0).toString()).visible(false));
                            moveCamera(new LatLng(current.getLatitude(),current.getLongitude()),15);
                        }else{
                            Log.d("Map: ", "Location is null");
                            Toast.makeText(MapsActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.d("Map: ", "Security Exception: " + e.getMessage());
        }
    }

    private void moveCamera (LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK){
            tempMarker = (Marker) data.getExtras().getSerializable("tempMarker");
            addNewMarker();
        }
    }

    public Object[] calculateNearestMarker(){
        Object[] parameters = new Object[2];
        if(markers.size() == 0){
            parameters [0]= myLocation;
            parameters[1]=0;
            return parameters;
        }
        else{
            double min = Double.MAX_VALUE;
            Marker temp = null;
            for (int i = 0; i<markers.size();i++){
                double distance = markers.get(i).calculateDistance(myLocation);
                if(distance<min){
                    temp = markers.get(i);
                    min = distance;
                }
            }
            parameters[0] = temp;
            parameters[1] = min;
            return parameters;
        }
    }
    public void setNearestMarker(){
        Object[] param = calculateNearestMarker();
        Marker n = (Marker)param[0];
        txtNearMarker.setText("El marcador más cercano es: " + n.getName());
        txtDistance.setText("Se encuentra a una distancia de: " + param[1] + " k.m.");
    }

    public void updateMap(){
        mMap.clear();
        for (Marker m: markers) {
            LatLng location = new LatLng(m.getLatitude(),m.getLongitude());
            double distance = m.calculateDistance(myLocation);
            mMap.addMarker(new MarkerOptions().position(location).title("Name: "+ m.getName() + "- Distance: " + distance + " k.m."));
        }


        final Task location = mFLPC.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d("Map: ", "Found location");
                    Location current = (Location) task.getResult();
                    myLocation.setLatitude(current.getLatitude());
                    myLocation.setLongitude(current.getLongitude());

                }else{
                    Log.d("Map: ", "Location is null");
                    Toast.makeText(MapsActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LatLng me = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        List<Address> e = geoLocate(me);
        mMap.addMarker(new MarkerOptions().position(me).title(e.get(0).getAddressLine(0)).visible(true));
        setNearestMarker();

    }


}
