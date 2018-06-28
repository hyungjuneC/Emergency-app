package com.project9.databasetest;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "TestDataBase";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArr;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();

        //데이터베이스 생성(파라메터 Context) 및 오픈
        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //DataBase에 값을 입력
        mDbOpenHelper.insertColumn("서울 성심병원", "02-966-1616", "서울 특별시 동대문구 왕산로 259, 서울 성심병원(청량리동)");
        mDbOpenHelper.insertColumn("이화여자대학교의과대학부속목동병원", "02-2650-5114", "서울특별시 양천구 안양천로 1071 (목동)");
        mDbOpenHelper.insertColumn("강동경희대학교의대병원", "1577-5800", "서울특별시 강동구 동남로 892 (상일동)");
        mDbOpenHelper.insertColumn("재단법인아산사회복지재단 서울아산병원", "1688-7575", "서울특별시 송파구 올림픽로43길 88 (풍납동, 서울아산병원)");
        mDbOpenHelper.insertColumn("학교법인가톨릭학원가톨릭대학교서울성모병원", "1588-1511", "서울특별시 서초구 반포대로 222 (반포동)");
        mDbOpenHelper.insertColumn("한국원자력의학원원자력병원", "02-970-2114", "서울특별시 노원구 노원로 75, 한국원자력의학원 (공릉동)");


        //ArrayList 초기화
        mInfoArr = new ArrayList<InfoClass>();

        doWhileCursorToArray();

        //값이 제대로 입력됬는지 확인하기 위해 로그를 찍어본다
        for (InfoClass i : mInfoArr) {
            Log.i(TAG, "ID = " + i._id);
            Log.i(TAG, "NAME = " + i.name);
            Log.i(TAG, "CONTACT = " + i.contact);
            Log.i(TAG, "EMAIL = " + i.email);
        }

        //리스트뷰에 사용할 어댑터 초기화(파라메터 Context, ArrayList<InfoClass>)
        mAdapter = new CustomAdapter(this, mInfoArr);
        mListView.setAdapter(mAdapter);
        //리스트뷰의 아이템을 길게 눌렀을 경우 삭제하기 위해 롱클릭 리스너 따로 설정
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                Log.i(TAG, "position = " + position);
                //리스트뷰의 position은 0부터 시작하므로 1을 더함
                boolean result = mDbOpenHelper.deleteColumn(position + 1);
                Log.i(TAG, "result = " + result);

                if (result) {
                    //정상적인 position을 가져왔을 경우 ArrayList의 position과 일치하는 index 정보를 remove
                    mInfoArr.remove(position);
                    //어댑터에 ArrayList를 다시 세팅 후 값이 변경됬다고 어댑터에 알림
                    mAdapter.setArrayList(mInfoArr);
                    mAdapter.notifyDataSetChanged();
                } else {
                    //잘못된 position을 가져왔을 경우 다시 확인 요청
                    Toast.makeText(MainActivity.this, "INDEX를 확인해 주세요",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    //doWhile문을 이용하여 Cursor에 내용을 다 InfoClass에 입력 후 InfoClass를 ArrayList에 Add
    private void doWhileCursorToArray() {

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
            mInfoArr.add(mInfoClass);
        }
        //Cursor 닫기
        mCursor.close();
    }

    /**
     * 추가 버튼 클릭 메소드.
     *
     * @param v
     */
    public void btnAdd(View v) {
        //추가를 누를 경우 EditText에 있는 String 값을 다 가져옴
        mDbOpenHelper.insertColumn(
                mEditTexts[Constants.NAME].getText().toString().trim(),
                mEditTexts[Constants.CONTACT].getText().toString().trim(),
                mEditTexts[Constants.EMAIL].getText().toString().trim()
        );
        //ArrayList 내용 삭제
        mInfoArr.clear();

        doWhileCursorToArray();

        mAdapter.setArrayList(mInfoArr);
        mAdapter.notifyDataSetChanged();
        //Cursor 닫기
        mCursor.close();
    }

    /**
     * 레이아웃 세팅하는 메소드
     */
    private EditText[] mEditTexts;
    private ListView mListView;

    private void setLayout() {
        mEditTexts = new EditText[]{
                (EditText) findViewById(R.id.etName),
                (EditText) findViewById(R.id.etContact),
                (EditText) findViewById(R.id.etEmail)
        };

        mListView = (ListView) findViewById(R.id.list);
    }

    //액티비티가 종료 될 때 디비를 닫아준다
    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }
}
