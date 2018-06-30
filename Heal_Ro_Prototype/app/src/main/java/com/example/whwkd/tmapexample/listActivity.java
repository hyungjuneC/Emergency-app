package com.example.whwkd.tmapexample;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whwkd.tmapexample.database.DbOpenHelper;
import com.example.whwkd.tmapexample.database.InfoClass;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class listActivity extends AppCompatActivity {

    private ArrayList<InfoClass> mInfoArr;
    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ListView mListView;
    private double[] X;
    private double[] Y;
    private String[] names;
    private double[] dist;
    private TMapData tMapData;
    TextView txt ;
    MyAdapter mMyAdapter;
    ImageView image ;

    private math myMath = new math();


    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_route);
        tMapData = new TMapData();
        /*image = (ImageView)findViewById(R.id.);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(listActivity.this,MainActivity.class);
                listActivity.this.startActivity(mainIntent);
                listActivity.this.finish();
            }
        });*/


        mListView = (ListView)findViewById(R.id.listView);

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
            Intent listintent = getIntent();
            X = listintent.getDoubleArrayExtra("latitude");
            Y = listintent.getDoubleArrayExtra("longitude");
            names = listintent.getStringArrayExtra("names");
            Log.i(String.valueOf(X.length),String.valueOf(Y.length));
            dist = listintent.getDoubleArrayExtra("distance");

            dataSetting();
        }
    }



    private void dataSetting(){


        mMyAdapter = new MyAdapter();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1; i<5; i++) {
                    try {
                        mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.location), names[i],tMapData.convertGpsToAddress(X[i],Y[i]) ,
                                ContextCompat.getDrawable(getApplicationContext(),R.drawable.location),String.valueOf(dist[i]).substring(0,5)+"km");
                        txt= findViewById(R.id.Hosname);
                        txt.setText(names[0]);
                        txt = findViewById(R.id.address);
                        txt.setText(tMapData.convertGpsToAddress(X[0],Y[0]));
                        txt = findViewById(R.id.kilometer);
                        txt.setText(String.valueOf(dist[0]).substring(0,5)+"km");




                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.start();

        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        mListView.setAdapter(mMyAdapter);
    }

/*
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {
            // parent는 AdapterView의 속성의 모두 사용 할 수 있다.
            String tv = (String)parent.getAdapter().getItem(position);
            Toast.makeText(getApplicationContext(), tv, Toast.LENGTH_SHORT).show();

            // view는 클릭한 Row의 view를 Object로 반환해 준다.
            TextView tv_view = (TextView)view.findViewById(R.id.);
            tv_view.setText("바꿈");

            // Position 은 클릭한 Row의 position 을 반환해 준다.
            Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            // l_Position 은 클릭한 Row의 long type의 position 을 반환해 준다.
            Toast.makeText(getApplicationContext(), "l = " + l_position, Toast.LENGTH_SHORT).show();
        }
    };

*/
}
