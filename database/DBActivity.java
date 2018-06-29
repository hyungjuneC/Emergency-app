package com.project9.databasetest.database;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DBActivity extends Activity  {
    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArr;
    private CustomAdapter mAdapter;

    public void setDatabase() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pref.getInt("intkey", 0) == 0) {


            //DataBase에 값을 입력
            mDbOpenHelper.insertColumn("서울 성심병원", "02-966-1616", "서울 특별시 동대문구 왕산로 259, 서울 성심병원(청량리동)");
            mDbOpenHelper.insertColumn("이화여자대학교의과대학부속목동병원", "02-2650-5114", "서울특별시 양천구 안양천로 1071 (목동)");
            mDbOpenHelper.insertColumn("강동경희대학교의대병원", "1577-5800", "서울특별시 강동구 동남로 892 (상일동)");
            mDbOpenHelper.insertColumn("재단법인아산사회복지재단 서울아산병원", "1688-7575", "서울특별시 송파구 올림픽로43길 88 (풍납동, 서울아산병원)");
            mDbOpenHelper.insertColumn("학교법인가톨릭학원가톨릭대학교서울성모병원", "1588-1511", "서울특별시 서초구 반포대로 222 (반포동)");
            mDbOpenHelper.insertColumn("한국원자력의학원원자력병원", "02-970-2114", "서울특별시 노원구 노원로 75, 한국원자력의학원 (공릉동)");


            edit.putInt("intkey", 1);
            edit.commit();
            mDbOpenHelper.close();
        }
        else{
            mDbOpenHelper.close();

        }
    }

    public void doWhileCursorToArray(ArrayList<InfoClass> arr) {
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

        while (mCursor.moveToNext()) {
            //InfoClass에 입력된 값을 압력
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("contact")),
                    mCursor.getString(mCursor.getColumnIndex("email"))
            );
            //입력된 값을 가지고 있는 InfoClass를 InfoArray에 add
            arr.add(mInfoClass);
        }
        //Cursor 닫기
        mCursor.close();
        mDbOpenHelper.close();
    }
}
