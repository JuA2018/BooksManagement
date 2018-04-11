package org.techtown.booksmanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //멤버 필드
    DBHelper helper;
    SQLiteDatabase DB;
    NoteDBAdapterActivity Adapter;


    EditText edit_id, edit_writer, edit_book;
    Button btn_add, btn_edit, btn_delete, btn_search;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);
        try{
            DB = helper.getWritableDatabase();
        }catch(SQLiteException e){
            DB = helper.getReadableDatabase();
        }

        edit_id = (EditText)findViewById(R.id.edtID);
        edit_writer = (EditText)findViewById(R.id.edt_writer);
        edit_book = (EditText)findViewById(R.id.edt_bookname);

        btn_add = (Button)findViewById(R.id.btn_add);
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_search = (Button)findViewById(R.id.btn_search);

        list = (ListView)findViewById(R.id.list);

        Adapter = new NoteDBAdapterActivity(this);

        Cursor cursor = DB.rawQuery("select _id, writer, book from notes;",null);

        while(cursor.moveToNext()){//실행시킬때 출력하는거
            Adapter.addData
                    (cursor.getString(0),cursor.getString(1),cursor.getString(2));
        }
        //어댑터 연결
        list.setAdapter(Adapter); //어댑터 객체의 listview와 연결

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NoteDBAdapterActivity.NoteVO vo =(NoteDBAdapterActivity.NoteVO)Adapter.getItem(position);
                edit_id.setText(vo.getId());
                edit_writer.setText(vo.getWriter());
                edit_book.setText(vo.getBook());
            }
        });
        btn_add.setBackgroundColor(Color.LTGRAY);
        btn_edit.setBackgroundColor(Color.LTGRAY);
        btn_search.setBackgroundColor(Color.LTGRAY);
        btn_delete.setBackgroundColor(Color.LTGRAY);

        //버튼에 대한 이벤트 연결
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String id = edit_id.getText().toString();
        String writer = edit_writer.getText().toString();
        String book = edit_book.getText().toString();

        Cursor cursor;

        switch (v.getId()){
            case R.id.btn_add://주소가 메모리에 정수로 설정이 되있음
                DB.execSQL("insert into notes values (null,'" + writer + "','" + book +"');");
                //string을 입력받을 때는 '로 묶어줘야됨
                Toast.makeText
                        (MainActivity.this,"데이터가 추가됨",Toast.LENGTH_SHORT).show();

                Adapter = new NoteDBAdapterActivity(this);
                list.setAdapter(Adapter);

                cursor = DB.rawQuery("select _id, writer, book from notes;",null);

                while(cursor.moveToNext()){//실행시킬때 출력하는거
                    Adapter.addData
                            (cursor.getString(0),cursor.getString(1),cursor.getString(2));
                }
                Adapter.notifyDataSetChanged();

                edit_id.setText(null);
                edit_writer.setText(null);
                edit_book.setText(null);

                break;
            case R.id.btn_edit:
                DB.execSQL("update notes set writer ='" + writer +
                        "', book= '" + book + "' where _id= '" + id +"';");
                //string을 입력받을 때는 '로 묶어줘야됨
                Toast.makeText(MainActivity.this,"데이터가 수정되었음",Toast.LENGTH_SHORT).show();

                Adapter = new NoteDBAdapterActivity(this);
                list.setAdapter(Adapter);

                cursor = DB.rawQuery("select _id, writer, book from notes;",null);

                while(cursor.moveToNext()){//실행시킬때 출력하는거
                    Adapter.addData(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                }
                Adapter.notifyDataSetChanged();
                break;
            case R.id.btn_delete:
                DB.execSQL("delete from notes where _id='" + id +"';");
                //string을 입력받을 때는 '로 묶어줘야됨
                Toast.makeText(MainActivity.this,"데이터가 삭제됨",Toast.LENGTH_SHORT).show();

                Adapter.removeData(id);

                list.setAdapter(Adapter);

                Adapter.notifyDataSetChanged();

                edit_id.setText(null);
                edit_writer.setText(null);
                edit_book.setText(null);
                break;
            case R.id.btn_search:
                Toast.makeText(this, "데이터 검색", Toast.LENGTH_SHORT).show();
                Adapter = new NoteDBAdapterActivity(this);
                list.setAdapter(Adapter);

                if(writer.equals("")){
                    cursor = DB.rawQuery
                            ("select _id, writer, book from notes;",null);
                }
                else{
                    cursor = DB.rawQuery
                            ("select _id, writer, book from notes where writer='"+ writer +"';",null);
                }
                while(cursor.moveToNext()){//실행시킬때 출력하는거
                    Adapter.addData
                            (cursor.getString(0),cursor.getString(1),cursor.getString(2));
                }
                Adapter.notifyDataSetChanged();
                edit_writer.setText(null);
                break;

        }


    }

    //내부클래스 ->세분화
    class DBHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "mycontacts.db";
        private static final int DATABASE_VERSION = 2;

        public DBHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        };

        @Override
        public void onCreate(SQLiteDatabase DB) {
            //테이블 생성
            DB.execSQL("create table notes(_id integer primary key autoincrement , writer text , book text);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
            DB.execSQL("drop table if exists notes ");

        }
    }
}