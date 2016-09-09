package com.praveen.restaurant_find;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private TextView displayText;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int PLACE_PICKER_REQUEST = 1;
    public static LatLng pickerLoc = new LatLng(37.398160, -122.180831);
    public static String searchResult=null;
    public ListView listV = null;
    public ArrayList<String> list =null;
    String id = null;
    public static ResturauntData data;

    YelpTest yelp = null;
    YelpTask yelpTask = null;
    HashMap<Integer,ResturauntData> resturauntDataHashMap;
    Bitmap image;
    ResturauntList adapter;
    String[] ItemName;
    int ResturauntCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText = (TextView)findViewById(R.id.searchViewResult);
        list = new ArrayList<String>();
        listV = (ListView) findViewById(R.id.listView);
        //  adapter = new
        //        listV.setAdapter(adapter);
        id = new String();

        yelp = new YelpTest();
        resturauntDataHashMap = new HashMap<>();

        initSearchView();
        //String response = yelp.search("cake", pickerLoc.latitude, pickerLoc.longitude, "20", "16000");
        setLocation();
    }

    private void setLocation() {

        ImageButton bn1 = (ImageButton) findViewById(R.id.location);
        ImageView fav = (ImageView) findViewById(R.id.fav);
        DBHelper db = new DBHelper(MainActivity.this);
        final int Count=db.numberOfRows();

            Log.d("else",String.valueOf(Count));
            fav.setClickable(true);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(getApplicationContext(), FavActivity.class);
                    startActivity(i);
                }
            });

        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlacePicker.IntentBuilder intentBuilder =
//                        new PlacePicker.IntentBuilder();
//                intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
//                Intent intent = null;
                try {
                    //intent = intentBuilder.build(MainActivity.this);
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                //startActivityForResult(intent, PLACE_PICKER_REQUEST);
                // Log.d("result",intent.toString());


            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                pickerLoc=place.getLatLng();
                String toastMsg = String.format("Place: %s", pickerLoc);
                Log.d("result",toastMsg.toString());
//                /Log.d("result1",String.valueOf(place.));
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        Log.d("ser", String.valueOf(searchView.getQuery()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                Log.d("result",query);
                searchResult=query;
                try {
                    getResurauntDetails(query);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };



        searchView.setOnQueryTextListener(queryTextListener);


    }

    void getResurauntDetails(String s) throws ExecutionException, InterruptedException {
        new YelpTask().execute(s).get();

        // Log.d("count",getString(ResturauntCount));
        for(int i =0; i<ResturauntCount;i++)
        {
            if(resturauntDataHashMap.get(i) != null ) {
                Void aVoid = new GetBitMapTask().execute(resturauntDataHashMap.get(i).getUrl()).get();
                resturauntDataHashMap.get(i).setIcon(image);
            }
            else
            {
                Log.d("null","null");
            }

        }
        Log.d("Count",String.valueOf(ResturauntCount));
        adapter = new ResturauntList(MainActivity.this,ResturauntCount,resturauntDataHashMap);

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (ResturauntData)resturauntDataHashMap.get(position);
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(i);
                Log.e("name",data.getResturauntName());
            }
        });
        Log.d("done", "3");
        listV.setAdapter(adapter);
        Log.d("done", "4");
        adapter.notifyDataSetChanged();
    }

    public class YelpTask extends AsyncTask<String, Void, JSONObject> {

        JSONObject yelpData ;

        @Override
        protected JSONObject doInBackground(String... h) {
            double lat = 0;
            double longitude = 0;

            String response = yelp.search(h[0], pickerLoc.latitude, pickerLoc.longitude, "20", "16000");
            Log.v("API", response);

            try {
                yelpData = new JSONObject(response);
            } catch (Exception e) {

            }
            ResturauntData resturauntData;
            try {
                JSONArray buisness = yelpData.getJSONArray("businesses");
                //    Log.d("result",buisness.toString(0));


                ResturauntCount = 0;
                for (int i = 0; i < buisness.length(); i++) {

                    resturauntData = new ResturauntData();
                    JSONObject name = buisness.getJSONObject(i);
                    //Log.d("buisness",name.toString(0));

                    resturauntData.setResturauntName(name.getString("name"));

                    resturauntData.setReviewcountcount(name.getInt("review_count"));

                    resturauntData.setphoneNo(name.getString("phone"));

                    JSONObject location = name.getJSONObject("location");

                    if (location != null)
                    {
                        JSONObject cood = location.getJSONObject("coordinate");
                         lat = cood.getDouble("latitude");
                         longitude = cood.getDouble("longitude");
                        JSONArray add = location.getJSONArray("display_address");

                        if(add != null) {

                            StringBuilder addStr= new StringBuilder();
                            for(int j=0;j<add.length();j++)
                            {
                                addStr.append(add.getString(j));
                                addStr.append("\n");
                            }

                            resturauntData.setAddress(addStr.toString());
                        }
                        else
                        {
                            Log.e("null", "address");
                        }
                    }

                    resturauntData.setRating((name.getDouble("rating")));

                    resturauntData.setUrl(name.getString("image_url"));
                    resturauntData.setLoc(lat,longitude);
                    // Void aVoid = new GetBitMapTask().execute(name.getString("image_url")).get();
                    resturauntDataHashMap.put(i,resturauntData);
                    ResturauntCount++;
                }

                return yelpData;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return yelpData;
        }

        protected void onPostExecute(JSONObject result) {
            ResturauntData resturauntData;
            super.onPostExecute(result);


        }
    }
    public class GetBitMapTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... h) {
            Log.d("image",h[0]);
            image = getBitmapFromURL(h[0]);
            return null;
        }


    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            Log.d("image",src);
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            try{
                connection.connect();
            }
            catch (Exception e)
            {

            }
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
