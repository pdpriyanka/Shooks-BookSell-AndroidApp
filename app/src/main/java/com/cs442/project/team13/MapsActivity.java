package com.cs442.project.team13;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener {
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String CURRENTLOCATION = "currentLocation";
    GoogleMap googleMap;
    double latitude;
    double longitude;
    boolean currentLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (!isGooglePlayServicesAvailable()) {
            finish();
        }


        setContentView(R.layout.activity_google_map);


        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                setMarker(latLng);
                currentLocation = false;
            }
        });
        if(getIntent()!=null && savedInstanceState == null){
            latitude = getIntent().getDoubleExtra(LATITUDE,0);
            longitude = getIntent().getDoubleExtra(LONGITUDE,0);
            currentLocation = false;
            setMarker(new LatLng(latitude,longitude));
        }

        if(savedInstanceState!=null)
        {
            if(savedInstanceState.containsKey(LATITUDE) && savedInstanceState.containsKey(LONGITUDE)) {
                setMarker(new LatLng(savedInstanceState.getDouble(LATITUDE),savedInstanceState.getDouble(LONGITUDE)));
            }

            if(savedInstanceState.containsKey(CURRENTLOCATION)) {
                currentLocation = savedInstanceState.getBoolean(CURRENTLOCATION);
            }
        }

        if(currentLocation == true) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);

            String bestProvider = locationManager.getBestProvider(criteria, true);

            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }

        }
        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Defining button click event listener for the find button
        View.OnClickListener findClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);

                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };

        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(currentLocation == true) {
            setMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        }

    }

    public void setMarker(LatLng latLng){
        latitude = latLng.latitude;
        longitude = latLng.longitude;

        googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        String address = getLocationAddress();

        if(address!=null && !address.isEmpty()){
            markerOptions.title(address);
        }
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    public void saveLocation(View v){
        getIntent().putExtra("latitude",latitude);
        getIntent().putExtra("longitude",longitude);
        this.setResult(RESULT_OK, getIntent());
        finish();
    }

    public void cancel(View v){
        this.finish();
    }

    private String getLocationAddress(){
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        StringBuffer addressBuff = new StringBuffer("");
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();

                List<String> addressList = new ArrayList<String>();
                if (address != null && !address.isEmpty()) {
                    addressList.add(address);
                }

                if (city != null && !city.isEmpty()) {
                    addressList.add(city);
                }
                if (state != null && !state.isEmpty()) {
                    addressList.add(state);
                }
                if (country != null && !country.isEmpty()) {
                    addressList.add(country);
                }
                /*if (postalCode != null && !postalCode.isEmpty()) {
                    addressList.add(postalCode);
                }*/

                addressBuff = new StringBuffer("");
                if (!addressList.isEmpty()) {
                    for (int i = 0; i < addressList.size(); i++) {
                        if (i == (addressList.size() - 1)) {
                            addressBuff.append(addressList.get(i));
                        } else {
                            addressBuff.append(addressList.get(i) + ", ");
                        }
                    }
                }
            }
        }catch(IOException e){
            Log.e("Shooks", "Could not get Geocoder data", e);
        }

        return addressBuff.toString();
    }


    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {

                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_LONG).show();
            } else {
                Address address = (Address) addresses.get(0);
                setMarker(new LatLng(address.getLatitude(), address.getLongitude()));
                currentLocation = false;
            }
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble(LATITUDE, latitude);
        savedInstanceState.putDouble(LONGITUDE, longitude);
        savedInstanceState.putBoolean(CURRENTLOCATION, currentLocation);
    }


}