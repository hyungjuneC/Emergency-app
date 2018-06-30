package com.example.whwkd.tmapexample;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.whwkd.tmapexample.database.DbOpenHelper;
import com.example.whwkd.tmapexample.database.InfoClass;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class math {
    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;

    private static int mMarkerID;

    private ArrayList<InfoClass> mInfoArr;
    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;


    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();




    public void getMapPoint(ArrayList<MapPoint> arr1, Activity act){
        mDbOpenHelper = new DbOpenHelper(act);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mCursor = null;
        //DB에 있는 모든 컬럼을 가져옴
        mCursor = mDbOpenHelper.getAllColumns();
        //컬럼의 갯수 확인
        Log.i(TAG, "Count = " + mCursor.getCount());
        mInfoArr = new ArrayList<InfoClass>();

        while (mCursor.moveToNext()) {
            //InfoClass에 입력된 값을 압력
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("contact")),
                    mCursor.getString(mCursor.getColumnIndex("email"))
            );
            //입력된 값을 가지고 있는 InfoClass를 InfoArray에 add
            mInfoArr.add(mInfoClass);


        }
        for (InfoClass i : mInfoArr) {
            String parsing = i.email;
            parsing.trim();
            String[] parsearr = parsing.split(",");
            MapPoint a = new MapPoint(i.name,Double.valueOf(parsearr[0]),Double.valueOf(parsearr[1]));
            arr1.add(a);
        }



        //값이 제대로 입력됬는지 확인하기 위해 로그를 찍어본다
        for (MapPoint i : arr1) {
            Log.i(TAG, "name = " + i.getName());
            Log.i(TAG, "Latitude = " + i.getLatitude());
            Log.i(TAG, "Logintude = " + i.getLongitude());

        }
        //Cursor 닫기
        mCursor.close();


    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public void getClosestArr(ArrayList<MapPoint> arr1 , MapPoint user) {
        for(MapPoint i : arr1){
            i.setDistance(getDistance(i.getLatitude(),i.getLongitude(),user.getLatitude(),user.getLongitude(),"kilometer"));
        }

        Collections.sort(arr1  , new Comparator<MapPoint>() {
            @Override
            public int compare(MapPoint o1, MapPoint o2){

                return (o1.getDistance() < o2.getDistance()) ? -1: (o1.getDistance()> o2.getDistance()) ? 1:0 ;


            }
        });

        for (MapPoint i : arr1) {
            Log.i(TAG, "name = " + i.getName());
            Log.i(TAG, "Latitude = " + i.getLatitude());
            Log.i(TAG, "Logintude = " + i.getLongitude());

        }

    }





}
