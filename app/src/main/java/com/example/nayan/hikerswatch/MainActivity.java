package com.example.nayan.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            }
        }

    }

    public void updatedata(Location location){

        TextView textView1 = (TextView)findViewById(R.id.textView1);
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        TextView textView3 = (TextView)findViewById(R.id.textView3);
        TextView textView4 = (TextView)findViewById(R.id.textView4);
       // TextView textView5 = (TextView)findViewById(R.id.textView5);

        textView1.setText("Latitude: " + location.getLatitude());
        textView2.setText("Longitude: " + location.getLongitude());
        textView3.setText("Accuracy: "+ location.getAccuracy());
        textView4.setText("Altitude: "+ location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address ="Could not find address" ;

            List<Address> listaddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listaddress!= null &&listaddress.size()>0){

                address="Address: \n";
                if (listaddress.get(0).getSubThoroughfare()!= null){
                    address+= listaddress.get(0).getSubThoroughfare() + " ";
                }
                if (listaddress.get(0).getThoroughfare()!= null){
                    address+= listaddress.get(0).getThoroughfare() + "\n";
                }
                if (listaddress.get(0).getLocality()!= null){
                    address+= listaddress.get(0).getLocality() + "\n";
                }
                if (listaddress.get(0).getPostalCode()!= null){
                    address+= listaddress.get(0).getPostalCode() + "\n";
                }
                if (listaddress.get(0).getCountryName()!= null){
                    address+= listaddress.get(0).getCountryName() + " ";
                }
            }

            TextView textView5 = (TextView)findViewById(R.id.textView5);
            textView5.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updatedata(location);

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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location!= null) {
                updatedata(location);
            }
        }

    }
}
