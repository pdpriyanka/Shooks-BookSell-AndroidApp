package com.cs442.project.team13;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

public class DisplayBookLocationActivity extends FragmentActivity {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_display_book_location);

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = supportMapFragment.getMap();

        supportMapFragment.getView().setClickable(false);
        googleMap.clear();
        double lat = getIntent().getDoubleExtra("latitude", 0);
        double longi = getIntent().getDoubleExtra("longitude",0);

        LatLng latLng = new LatLng(lat, longi);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        markerOptions.title(getLocationAddress(latLng));
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


    private String getLocationAddress(LatLng latLng){

        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        StringBuffer addressBuff = new StringBuffer("");
        try{
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

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

}