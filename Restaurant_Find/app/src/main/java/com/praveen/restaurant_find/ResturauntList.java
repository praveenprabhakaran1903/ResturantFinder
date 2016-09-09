package com.praveen.restaurant_find;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class ResturauntList extends ArrayAdapter<Object>{
    private final Activity context;
    HashMap<Integer,ResturauntData> resturauntDataHashMap;
    Integer Count=0;


    public ResturauntList(Activity context,Integer Count,HashMap<Integer,ResturauntData> resturauntDataHashMap ) {
        super(context, R.layout.resturaunt_list);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.resturauntDataHashMap = resturauntDataHashMap;
        this.Count = Count;
    }
    public int getCount(){
        Log.e("count",String.valueOf(Count));
        return Count;

    }


    public Object getItem(int position) {

        return (Object) resturauntDataHashMap.get(position);
    }



    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.resturaunt_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txtAddress = (TextView) rowView.findViewById(R.id.address);
        TextView txtRating     = (TextView) rowView.findViewById(R.id.rating);

        Log.e("getView", "1");
        if(resturauntDataHashMap.get(position)!= null) {
            txtTitle.setText(resturauntDataHashMap.get(position).getResturauntName());
            imageView.setImageBitmap(resturauntDataHashMap.get(position).getImage());
            txtAddress.setText(resturauntDataHashMap.get(position).getAddress1());
            txtRating.setText(resturauntDataHashMap.get(position).getRating());
            //    extratxt.setText("Description "+itemname[position]);
        }
        return rowView;

    };
}
