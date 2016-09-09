package com.praveen.restaurant_find;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FavActivity extends AppCompatActivity {

    HashMap<Integer,ResturauntData> resturauntDataHashMap;
    Bitmap image;
    ResturauntList adapter;
    int ResturauntCount= 0;//(int) ResultActivity.count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);



        DBHelper db = new DBHelper(FavActivity.this);
        ResturauntCount=db.numberOfRows();

        resturauntDataHashMap= db.getAllFav();
        try {
            createList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void createList() throws ExecutionException, InterruptedException {

        adapter = new ResturauntList(FavActivity.this,ResturauntCount-1,resturauntDataHashMap);

       ListView listV = (ListView) findViewById(R.id.fListView);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.data = (ResturauntData)resturauntDataHashMap.get(position);
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(i);
                //  Log.e("name", data.getResturauntName());
            }
        });
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
        Log.d("done", "3");
        listV.setAdapter(adapter);
        Log.d("done", "4");
        adapter.notifyDataSetChanged();
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
    public class GetBitMapTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... h) {
            Log.d("image",h[0]);
            image = getBitmapFromURL(h[0]);
            return null;
        }


    }
}



