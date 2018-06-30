package com.example.whwkd.tmapexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.whwkd.tmapexample.database.DbOpenHelper;
import com.example.whwkd.tmapexample.database.InfoClass;
import com.skt.Tmap.TMapPoint;

import java.sql.SQLException;
import java.util.ArrayList;

public class Greeter extends AppCompatActivity  {
    private ArrayList<InfoClass> mInfoArr;
    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;

    private math myMath = new math();


    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    protected void getHosLocations() {
        myMath.getMapPoint(m_mapPoint, this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeter);
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if (pref.getInt("intkey", 0) == 0) {


            //DataBase에 값을 입력
            mDbOpenHelper.insertColumn("서울 성심병원", "02-966-1616", "37.584443,127.049898");
            mDbOpenHelper.insertColumn("이화여자대학교의과대학부속목동병원", "02-2650-5114", "37.322113,127.123123");
            mDbOpenHelper.insertColumn("강동경희대학교의대병원", "1577-5800", "37.48113,127.573123");
            mDbOpenHelper.insertColumn("재단법인아산사회복지재단 서울아산병원", "1688-7575", "37.112113,127.123123");
            mDbOpenHelper.insertColumn("학교법인가톨릭학원가톨릭대학교서울성모병원", "1588-1511", "37.132113,127.123123");
            mDbOpenHelper.insertColumn("한국원자력의학원원자력병원", "02-970-2114", "37.472113,127.563123");


            edit.putInt("intkey", 1);
            edit.commit();
        } else {
            /*

            mDbOpenHelper = new DbOpenHelper(this);
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
                m_mapPoint.add(a);
            }



            //값이 제대로 입력됬는지 확인하기 위해 로그를 찍어본다
            for (MapPoint i : m_mapPoint) {
                Log.i(TAG, "name = " + i.getName());
                Log.i(TAG, "Latitude = " + i.getLatitude());
                Log.i(TAG, "Logintude = " + i.getLongitude());

            }


            //Cursor 닫기
            mCursor.close();

            */


            getHosLocations();



        }
        ImageView btn = (ImageView)findViewById(R.id.button_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Greeter.this,MainActivity.class);
                Greeter.this.startActivity(mainIntent);
                Greeter.this.finish();
            }
        });
    }



    public void doWhileCursorToArray() {
        mDbOpenHelper = new DbOpenHelper(this);
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

    }
}
