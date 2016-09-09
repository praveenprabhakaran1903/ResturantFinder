package com.praveen.restaurant_find;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class ResturauntData {

    int Reviewcount;
    String ResturauntName;
    String Address;
    String Url;
    String Rating;
    Bitmap icon;
    String phoneNo;
    LatLng loc=null;

    public void ResturauntData(){

    }

    public void setReviewcountcount(int c)
    {
        Reviewcount = c;
    }

    public void setResturauntName(String A)
    {
        ResturauntName = A;
    }

    public void setAddress(String A)
    {
        Address =A;
    }



    public void setIcon(Bitmap B)
    {
        icon = B;
    }

    public void setRating(double B)
    {
        Rating = String.valueOf(B);
    }

    public void setUrl(String url)
    {
        this.Url = url;
    }
    public void setphoneNo(String phoneNo)
    {
        this.phoneNo = phoneNo;
    }
    public void setLoc(Double lat,Double longi)
    {
        this.loc = new LatLng(lat,longi);
    }

    public String getUrl(){return Url;}
    public String getphoneNo(){return phoneNo;}
    public int getReviewcount ()
    {
        return Reviewcount;
    }

    public String getRating(){return Rating;}
    public String getAddress1(){return Address;}
    public LatLng getLoc(){return loc;}

    public String getResturauntName(){
        return ResturauntName;
    }

    public Bitmap getImage()
    {
        return icon;
    }

}
