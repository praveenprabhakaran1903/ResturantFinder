package com.praveen.restaurant_find;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResultActivity extends AppCompatActivity {
    public static LatLng loc=MainActivity.pickerLoc;
    public SupportMapFragment mapFragment = null;
    public static long count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(MainActivity.data.getResturauntName());

        TextView rating = (TextView) findViewById(R.id.rating);

        double rate=Double.valueOf(MainActivity.data.getRating());


            rating.setText("Rating " + MainActivity.data.getRating() + ", " + MainActivity.data.getReviewcount() + " Reviews");


        final ImageView fav = (ImageView) findViewById(R.id.favorite);

        fav.setClickable(true);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setImageResource(R.drawable.filled_heart);
                DBHelper db = new DBHelper(ResultActivity.this);
                count = db.insertFav(MainActivity.data.getResturauntName(),
                        MainActivity.data.getUrl(),
                        MainActivity.data.Rating,
                        MainActivity.data.Address,
                        MainActivity.data.phoneNo,
                        String.valueOf(MainActivity.data.getReviewcount()),
                        String.valueOf(MainActivity.data.getLoc().latitude),
                        String.valueOf(MainActivity.data.getLoc().longitude));
            }
        });

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setClickable(true);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(MainActivity.data.Address);
        TextView phone = (TextView) findViewById(R.id.phone);
        phone.setText("Call : "+MainActivity.data.getphoneNo());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(ResultActivity.this);



        showMap(MainActivity.data.getLoc());
    }

    private void showMap(final LatLng loc) {
        final GoogleMap googleMap = mapFragment.getMap();

        // Setting a click event handler for the map

                // Creating a marker
                //  while(iLoc==null);
                LatLng iLatLng = new LatLng(loc.latitude, loc.longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(iLatLng)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(18)                   // Sets the zoom

                .build();                   // Creates a CameraPosition from the builder

                MarkerOptions iMarkerOptions = new MarkerOptions();
                MarkerOptions markerOptions = new MarkerOptions();

                iMarkerOptions.position(iLatLng);
                iMarkerOptions.title(iLatLng.latitude + " : " + iLatLng.longitude);

                markerOptions.position(iLatLng);

                markerOptions.title(iLatLng.latitude + " : " + iLatLng.longitude);

                // Clears the previously touched position
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(iLatLng));
                googleMap.addMarker(iMarkerOptions);
                // Animating to the touched position
               // googleMap.animateCamera(CameraUpdateFactory.newLatLng(iLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Placing a marker on the touched position

                googleMap.addMarker(markerOptions);

    }


}


