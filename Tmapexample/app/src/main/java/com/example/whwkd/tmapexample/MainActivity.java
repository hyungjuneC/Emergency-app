package com.example.whwkd.tmapexample;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.example.whwkd.tmapexample.database.DbOpenHelper;
import com.example.whwkd.tmapexample.database.InfoClass;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;



import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements TMapGpsManager.onLocationChangedCallback {

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
    private Button roadbtn;
    private Button listbtn;

    private math myMath = new math();


    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();


    @Override
    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Log.i(String.valueOf(lat),String.valueOf(lon));

    }

    protected void getHosLocations(){
        myMath.getMapPoint(m_mapPoint,this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        roadbtn = (Button)findViewById(R.id.btn_draw_State);
        roadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarButton();
            }
        });
        listbtn =(Button)findViewById(R.id.add_button);
        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this,listActivity.class);
                double [] arrX = new double[5];
                double [] arrY = new double[5];
                String[] names = new String[5];
                double[] dist = new double[5];
                for(int i = 0; i< 5; i++){
                    names[i]=m_mapPoint.get(i).getName();
                    arrX[i] = m_mapPoint.get(i).getLatitude();
                    arrY[i]= m_mapPoint.get(i).getLongitude();
                    dist[i] = m_mapPoint.get(i).getDistance();

                }
                mainIntent.putExtra("latitude",arrX);
                mainIntent.putExtra("longitude",arrY);
                mainIntent.putExtra("names",names);
                mainIntent.putExtra("distance",dist);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();

            }
        });

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
            mDbOpenHelper.insertColumn("서울 성심병원", "02-966-1616", "12.132113,13.123123");
            mDbOpenHelper.insertColumn("이화여자대학교의과대학부속목동병원", "02-2650-5114", "14.132113,16.123123");
            mDbOpenHelper.insertColumn("강동경희대학교의대병원", "1577-5800", "14.132113,17.123123");
            mDbOpenHelper.insertColumn("재단법인아산사회복지재단 서울아산병원", "1688-7575", "14.132113,17.123123");
            mDbOpenHelper.insertColumn("학교법인가톨릭학원가톨릭대학교서울성모병원", "1588-1511", "14.132113,17.123123");
            mDbOpenHelper.insertColumn("한국원자력의학원원자력병원", "02-970-2114", "14.132113,17.123123");


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

        mContext = this;

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mapview);
        tmapview = new

                TMapView(this);
        frameLayout.addView(tmapview);
        tmapview.setSKTMapApiKey("cc6bbfab-5288-4f06-9b44-9ddaca7e9b71");









        // 현재 보는 방향


        // 줌레벨
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        MapPoint user = new MapPoint("user", 37.123131,127.345345);
        tmapview.setLocationPoint(user.getLongitude(),user.getLatitude());
        myMath.getClosestArr(m_mapPoint,user);

        showMarkerPoint();



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }

    }


    //T MAP 경로 Activity로 연결해준다.
    /*
    public void CarButton(View v) {
        Intent mainIntent = new Intent(MainActivity.this,.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();

    }
*/



    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tmapview.setLocationPoint(longitude, latitude);
                tmapview.setCenterPoint(longitude, latitude);
                Log.i("TmapTest",""+longitude+","+latitude);
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

    private void showList(){
        Intent mainIntent = new Intent(MainActivity.this,listActivity.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();

    }




    public void doWhileCursorToArray(){
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



        //값이 제대로 입력됬는지 확인하기 위해 로그를 찍어본다
        for (InfoClass i : mInfoArr) {
            Log.i(TAG, "ID = " + i._id);
            Log.i(TAG, "NAME = " + i.name);
            Log.i(TAG, "CONTACT = " + i.contact);
            Log.i(TAG, "EMAIL = " + i.email);
        }
        //Cursor 닫기
        mCursor.close();


}






    public void addPoint() { //여기에 핀을 꼽을 포인트들을 배열에 add해주세요!
        // 강남 //
        m_mapPoint.add(new MapPoint("강남", 37.510350, 127.066847));
    }


    public void showMarkerPoint() {// 마커 찍는거 빨간색 포인트.

            TMapPoint point = new TMapPoint(m_mapPoint.get(0).getLatitude(),
                    m_mapPoint.get(0).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();

            tmapview.setCenterPoint(point.getLongitude(),point.getLatitude());
            tmapview.setZoomLevel(15);
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.big_picker);
            //poi_dot은 지도에 꼽을 빨간 핀 이미지입니다

            item1.setTMapPoint(point);
            item1.setName(m_mapPoint.get(0).getName());
            item1.setVisible(item1.VISIBLE);

            item1.setIcon(bitmap);

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.i_go);

            // 풍선뷰 안의 항목에 글을 지정합니다.
            item1.setCalloutTitle(m_mapPoint.get(0).getName());
            item1.setCalloutSubTitle("가장 가까운 응급실!");
            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);



            String strID = String.format("pmarker%d", mMarkerID++);

            tmapview.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);

    }
    public void CarButton(){

        TMapTapi tMapTapi = new TMapTapi(this);
        boolean isTmapApp = tMapTapi.isTmapApplicationInstalled();
        if(isTmapApp){
            tMapTapi.invokeRoute(m_mapPoint.get(0).getName(),(float)m_mapPoint.get(0).getLongitude(),(float)m_mapPoint.get(0).getLatitude());
        }
        else{
            Toast.makeText(MainActivity.this, "티맵을 다운 받으셔야 합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

}
