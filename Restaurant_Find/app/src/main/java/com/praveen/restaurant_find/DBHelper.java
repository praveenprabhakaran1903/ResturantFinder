package com.praveen.restaurant_find;

/**
 * Created by Divya on 3/26/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String FAV_TABLE_NAME = "favorite";
    public static final String FAV_COLUMN_BNAME = "name";
    public static final String FAV_COLUMN_IMG = "image";
    public static final String FAV_COLUMN_RATING = "rating";
    public static final String FAV_COLUMN_ADDR = "addr";
    public static final String FAV_COLUMN_PHNO = "phno";
    public static final String FAV_COLUMN_REVIEWCOUNT = "rc";
    public static final String FAV_COLUMN_LAT = "lat";
    public static final String FAV_COLUMN_LONG = "longi";

    //public static final String FAV_COLUMN_ADDR2 = "addr2";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table favorite " +
                        "(name text,image text,rating text, addr text, phno text, rc text, lat text, longi text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS favorite");
        onCreate(db);
    }
    public long insertFav(String name, String image, String rating, String addr, String phno, String rc, String lat, String longi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", image);
        contentValues.put("rating", rating);
        contentValues.put("addr", addr);
        contentValues.put("phno", phno);
        contentValues.put("rc", rc);
        contentValues.put("lat", lat);
        contentValues.put("longi", longi);
       long x= db.insert("favorite", null, contentValues);
        Log.d("Insert",String.valueOf(x));
        return x;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FAV_TABLE_NAME);
        //Cursor c=db.rawQuery("select count(*) from favorites", null);
        //int numRows=c.getInt(0);
        Log.d("number",String.valueOf(numRows));
        return numRows;
    }

    public HashMap<Integer,ResturauntData> getAllFav()
    {
        Log.d("Hashmap","Getallfav");
        HashMap<Integer,ResturauntData> RstDtHM = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favorite", null);
        int i=0;
        Log.d("Qres",String.valueOf(res.moveToFirst()));
        while (res.moveToNext()){
            ResturauntData rd = new ResturauntData();
            rd.setResturauntName(res.getString(0));
            rd.setUrl(res.getString(1));
            rd.setRating(res.getDouble(2));
            rd.setAddress(res.getString(3));
            rd.setphoneNo(res.getString(4));
            rd.setReviewcountcount(Integer.valueOf(res.getString(5)));
            rd.setLoc(Double.valueOf(res.getString(6)),Double.valueOf(res.getString(7)));

            RstDtHM.put(i,rd);
            i++;
        }
        /*res.moveToFirst();

        /*while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(FAV_COLUMN_BNAME)));
            res.moveToNext();
        }
        return array_list;*/
        return RstDtHM;
    }
}
