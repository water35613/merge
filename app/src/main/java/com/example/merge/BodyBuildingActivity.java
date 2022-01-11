package com.example.merge;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class BodyBuildingActivity extends AppCompatActivity {
    private EditText ed_book,ed_price;
    private Button btn_query,btn_insert,btn_finish,btn_delete;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private ArrayList<String>items = new ArrayList<>();
    private SQLiteDatabase dbrw;



    class Data{
        int photo;
        String name;
    }
    public class MyAdapter extends BaseAdapter{
        private BodyBuildingActivity.Data[] data;
        private int view;

        public MyAdapter(BodyBuildingActivity.Data[] data,int view){
            this.data=data;
            this.view=view;
        }

        @Override
        public int getCount() {return data.length;}

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            convertView = getLayoutInflater().inflate(view, parent, false);
            TextView name = convertView.findViewById(R.id.name);
            name.setText(data[position].name);
            return convertView;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_building);
        ed_book=findViewById(R.id.ed_book);
        ed_price=findViewById(R.id.ed_price);
        btn_query=findViewById(R.id.btn_query);
        btn_insert=findViewById(R.id.btn_insert);
        btn_delete=findViewById(R.id.btn_delete);
        btn_finish=findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(BodyBuildingActivity.this,
                        FinishProject.class), 1);
            }
        });

        listView=findViewById(R.id.listView);
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();




        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c;
                if(ed_book.length()<1)
                    c=dbrw.rawQuery("SELECT * FROM myTable",null);
                else
                    c=dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE'"
                            +ed_book.getText().toString()+"'",null);
                c.moveToFirst();
                items.clear();
                Toast.makeText(BodyBuildingActivity.this,"共有"+c.getCount()+
                        "筆資料",Toast.LENGTH_SHORT).show();
                for (int i =0;i<c.getCount();i++){
                    items.add("品項:"+c.getString(0)+"\t\t\t\t"+c.getString(1)+"大卡");
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            }
        });
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_book.length()<1 ||ed_price.length()<1)
                    Toast.makeText(BodyBuildingActivity.this,"欄位請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try{
                        dbrw.execSQL("INSERT INTO myTable(book,price)VALUES(?,?)",
                                new Object[]{ed_book.getText().toString(),
                                        ed_price.getText().toString()});
                        Toast.makeText(BodyBuildingActivity.this,
                                "新增品項"+ed_book.getText().toString()+ed_price.getText().toString()+"\t\t\t\t大卡",
                                Toast.LENGTH_SHORT).show();
                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(BodyBuildingActivity.this,"新增失敗:"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_book.length()<1)
                    Toast.makeText(BodyBuildingActivity.this,"欄位請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '"+
                                ed_book.getText().toString()+"'");
                        Toast.makeText(BodyBuildingActivity.this,"刪除品項"+
                                ed_book.getText().toString(),Toast.LENGTH_SHORT).show();
                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(BodyBuildingActivity.this,"刪除失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // @Override
        //protected void onCreate(Bundle savedInstanceState) {
        //    super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_main);

        String[] transNameArray = new String[]{"第一天", "第二天", "第三天", "結算"};


        Data[] transData = new Data[transNameArray.length];
        for(int i=0; i<transData.length; i++) {
            transData[i] = new Data();
            transData[i].name = transNameArray[i];

        }

        MyAdapter transAdapter = new MyAdapter(transData, R.layout.trans_list);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(transAdapter);


    }
}


















