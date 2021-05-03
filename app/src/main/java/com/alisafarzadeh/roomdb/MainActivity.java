package com.alisafarzadeh.roomdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btninsert,removelayoutebtn;
    EditText edit_english,edit_persian;
    FloatingActionButton floatingActionButton;

    List<MainData> dataList = new ArrayList<>();
    RecAdapter recAdapter;
    RecyclerView rec;
    RoomHolder dbholder;

    List<DataTemp> temp = new ArrayList<>();
    Intent intent;

    Toolbar toolbar;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        intent = new Intent(MainActivity.this, MyServiceVocab.class);;
        edit_persian = findViewById(R.id.EditPersian);
        edit_english =findViewById(R.id.EditEnglish);
        rec = findViewById(R.id.Recycler);
        btninsert =findViewById(R.id.BtnInsert);
        removelayoutebtn=findViewById(R.id.removeaddlayoutBTN);
        floatingActionButton = findViewById(R.id.actionbutton);
        cardView = findViewById(R.id.cardView);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);

            }
        });
        removelayoutebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        recAdapter = new RecAdapter(MainActivity.this,dataList);

        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        dbholder = RoomHolder.getInstance(MainActivity.this);

        for (int i = 0; i < dbholder.mainDao().getAllData().size() ; i++) {
            MainData t = new MainData();
            t.setEnglish(dbholder.mainDao().getAllData().get(i).getEnglish());
            t.setPersian(dbholder.mainDao().getAllData().get(i).getPersian());
            dataList.add(t);
        }



        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rec.addItemDecoration(decoration);
        rec.setAdapter(recAdapter);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData m = new MainData();
                if (!edit_persian.getText().toString().equals("") && !edit_english.getText().toString().equals("")) {
                    m.setEnglish(edit_english.getText().toString());
                    m.setPersian(edit_persian.getText().toString());
                    dbholder.mainDao().insert(m);

                    dataList.clear();
                    dataList.addAll(dbholder.mainDao().getAllData());
                    recAdapter.notifyDataSetChanged();

                    edit_english.setText("");
                    edit_persian.setText("");
                    rec.smoothScrollToPosition(dbholder.mainDao().getAllData().size());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuitem:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog()
    {
        Dialog alertDialog = new Dialog(MainActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.customdialog);
        Spinner spinnerNumber =  alertDialog.findViewById(R.id.numbertime);
        Button btn = alertDialog.findViewById(R.id.btnwork);
        CheckBox checkBoxRandom = alertDialog.findViewById(R.id.checkBox2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "heloo", Toast.LENGTH_SHORT).show();
                if (!settoArray())
                {
                    Log.d("array","کلمه ای یافت نشد . کلمات مورد نظر را کلیک کنید.");
                }
                else{
                    for (int i = 0; i < arreng.length ; i++) {
                        Log.d("array",arreng[i]+" - \n");
                    }
                    Log.d("spi",spinnerNumber.getSelectedItem()+"");
                    intent.putExtra("eng",arreng);
                    intent.putExtra("per",arrper);
                    intent.putExtra("ran",checkBoxRandom.isChecked());
                    intent.putExtra("time",Integer.parseInt(spinnerNumber.getSelectedItem().toString()));
                    intent.setAction("play");
                    startService(intent);
                }
            }
        });
        alertDialog.show();
    }


    String[] arreng;
    String[] arrper;
    public boolean settoArray()
    {
        arreng=new String[recAdapter.getEnglish_select().size()];
        arrper=new String[recAdapter.getPersian_select().size()];
        if (arreng.length<=0)
        {
            return false;
        }else
        {
            for (int i = 0; i < recAdapter.getEnglish_select().size(); i++) {
                arreng[i]=recAdapter.getEnglish_select().get(i);
                arrper[i]=recAdapter.getPersian_select().get(i);
            }
            return true;
        }
    }
}