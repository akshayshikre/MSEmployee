package com.aks.mindspace.mindspace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.aks.mindspace.mindspace.SchoolListActivity.schoolArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE=99;

    LocationManager lm;
    EditText et1,et2;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    int iter=0;
    int listsize=0;
    FloatingActionButton fab1,fab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);

        fab1= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab2= (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerOptions mo;
                LatLng latlng,clatlng;
                if(iter<listsize)
                    try {
                        iter++;
                        mMap.clear();
                        String hospitalName = schoolArrayList.get(iter).schoolname.toString();
                        double lattitude = Double.parseDouble(schoolArrayList.get(iter).latti.toString());
                        double longitude = Double.parseDouble(schoolArrayList.get(iter).longi.toString());
                        Log.e("latlong", lattitude + " " + longitude);
                        mo = new MarkerOptions();
                        latlng = new LatLng(lattitude, longitude);
                        mo.position(latlng);
                        mo.title(hospitalName);
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        mMap.addMarker(mo);
                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        //--our location
                        mo = new MarkerOptions();
                        clatlng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                        mo.position(clatlng);
                        mo.title("Current Location");
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMap.addMarker(mo);
                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(clatlng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        Toast.makeText(MapsActivity.this,hospitalName,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerOptions mo;
                LatLng latlng,clatlng;
                if(iter>0)
                    try {
                        iter--;
                        mMap.clear();
                        String hospitalName = schoolArrayList.get(iter).schoolname.toString();
                        double lattitude = Double.parseDouble(schoolArrayList.get(iter).latti.toString());
                        double longitude = Double.parseDouble(schoolArrayList.get(iter).longi.toString());
                        Log.e("latlong", lattitude + " " + longitude);
                        mo = new MarkerOptions();
                        latlng = new LatLng(lattitude, longitude);
                        mo.position(latlng);
                        mo.title(hospitalName);
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        mMap.addMarker(mo);
                        // mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        //--our location
                        mo = new MarkerOptions();
                        clatlng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                        mo.position(clatlng);
                        mo.title("Current Location");
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMap.addMarker(mo);
                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(clatlng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                        Toast.makeText(MapsActivity.this,hospitalName,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        });
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Log.e("ourcode","support");

        mapFragment.getMapAsync(this);


    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {

            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    //permission granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(client==null)
                        {
                            buildGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else  //permission denied
                {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();

                }
                return;
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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        if(getIntent().getBooleanExtra("single",false)) {
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
            iter=getIntent().getIntExtra("iter",0);
            listsize=schoolArrayList.size();
        }
        MarkerOptions mo;
        LatLng latlng;
        if(getIntent().getBooleanExtra("single",false)) {
            Log.e("ourcode", "start");
            try {
                String hospitalName = getIntent().getStringExtra("hname");
                double lattitude = Double.parseDouble(getIntent().getStringExtra("latti"));
                double longitude = Double.parseDouble(getIntent().getStringExtra("longi"));
                Log.e("firstlatlong", lattitude + " " + longitude);
                mo = new MarkerOptions();
                latlng = new LatLng(lattitude, longitude);
                mo.position(latlng);
                mo.title(hospitalName);
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mMap.addMarker(mo);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                /****/mMap.clear();
                for (int k = 0; k < schoolArrayList.size(); k++) {
                    Log.e("List item" + k, schoolArrayList.get(k).schoolname.toString() + " : "
                            + schoolArrayList.get(k).longi.toString() + " : "
                            + schoolArrayList.get(k).latti.toString());
                    String hospitalName = schoolArrayList.get(k).schoolname.toString();
                    double lattitude = Double.parseDouble(schoolArrayList.get(k).latti.toString());
                    double longitude = Double.parseDouble(schoolArrayList.get(k).longi.toString());
                    Log.e("latlong", lattitude + " " + longitude);
                    mo = new MarkerOptions();
                    latlng = new LatLng(lattitude, longitude);

                    mo.position(latlng);
                    mo.title(hospitalName);
                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                    Log.e("ourcode", "end");
                }
            } catch (Exception e) {
                Log.e("ourcode", "endcatch" + e.getMessage());
                e.printStackTrace();
            }

        }
    }



    protected synchronized void buildGoogleApiClient()
    {
        client=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }



    public void doSomething(View view)
    {

        Object dataTransfer[]=new Object[2];
        GetNearByPlacesData getNearByPlacesData=new GetNearByPlacesData();


        switch(view.getId()) {
            case R.id.button6:
            {
                mMap.clear();

                List<Address> addressList = null;
                String location = et1.getText().toString();
                MarkerOptions mo = new MarkerOptions();
                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < addressList.size(); i++) {
                        Address myaddress = addressList.get(i);
                        latitude=myaddress.getLatitude();
                        longitude=myaddress.getLongitude();
                        LatLng latlng = new LatLng(latitude, longitude);

                        mo.position(latlng);
                        mo.title(et1.getText().toString());
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        mMap.addMarker(mo);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                        //  mMap.clear();
                        String hospital = et2.getText().toString();

                        String url = getUrl(latitude, longitude, hospital);
                        //  Toast.makeText(MapsActivity.this, ""+lastLocation.getLatitude()+","+lastLocation.getLongitude(), Toast.LENGTH_LONG).show();  //toast

                        dataTransfer[0] = mMap;
                        dataTransfer[1] = url;

                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(MapsActivity.this, "Showing near by Places", Toast.LENGTH_LONG).show();  //toast

                    }


                }

            }

            break;


            case R.id.B_Nurs:
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                mMap.clear();
                MarkerOptions mo = new MarkerOptions();

                LatLng latlng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                mo.position(latlng);
                mo.title("your current place");
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(mo);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                String url = getUrl(lastLocation.getLatitude(), lastLocation.getLongitude(), "playgroup");
                //  Toast.makeText(MapsActivity.this, ""+lastLocation.getLatitude()+","+lastLocation.getLongitude(), Toast.LENGTH_LONG).show();  //toast
                Log.e("map",url);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearByPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing near by playgroup", Toast.LENGTH_LONG).show();  //toast
                break;


            case R.id.B_Schools:
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                mMap.clear();
                mo = new MarkerOptions();
                latlng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                mo.position(latlng);
                mo.title("your current place");
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(mo);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                String url2 = getUrl(lastLocation.getLatitude(), lastLocation.getLongitude(), "school");
                Log.e("map",url2);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url2;

                getNearByPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing near by Schools", Toast.LENGTH_LONG).show();  //toast
                break;


            case R.id.B_Coll:
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                mMap.clear();
                mo = new MarkerOptions();
                latlng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                mo.position(latlng);
                mo.title("your current place");
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(mo);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                String url3 = getUrl(lastLocation.getLatitude(), lastLocation.getLongitude(), "college");
                Log.e("map",url3);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url3;

                getNearByPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing near by colleges", Toast.LENGTH_LONG).show();
                break;

        }





    }

    private String getUrl(double latitude,double longitude,String nearbyPlace)
    {
        Log.i("geturl","in start");
        StringBuilder googlePlaceUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        //  Toast.makeText(MapsActivity.this, "hi"+latitude+","+longitude, Toast.LENGTH_LONG).show();  //toast
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        //  Toast.makeText(MapsActivity.this, "hi"+nearbyPlace, Toast.LENGTH_LONG).show();   //toast
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyC5htpl3OYasbw9nZpAfNxDUeyrA6_naJs");

        return (googlePlaceUrl.toString());


    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation=location;

        if(currentLocationMarker!=null)
        {
            currentLocationMarker.remove();
        }

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentLocationMarker=mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client !=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;
        }
        else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
