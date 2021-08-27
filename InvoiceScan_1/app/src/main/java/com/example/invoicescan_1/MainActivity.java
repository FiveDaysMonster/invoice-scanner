package com.example.invoicescan_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    //action bar 按鈕
    ImageButton action_bar_button,action_bar_button2;
    //button B
    ImageButton a1,a2,b1,b2,c1,c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(MainActivity.this);

        init();
        //button event
        button_a_event();
        button_b_event();
        button_c_event();

    }
    public void init(){
        //button A
        a1=(ImageButton) findViewById(R.id.button_a1);
        a2=(ImageButton) findViewById(R.id.button_a2);
        //button B
        b1=(ImageButton) findViewById(R.id.button_b1);
        b2=(ImageButton) findViewById(R.id.button_b2);
        //button C
        c1=(ImageButton) findViewById(R.id.button_c1);
        c2=(ImageButton) findViewById(R.id.button_c2);
    }
    //button a event
    public void button_a_event(){
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,myscanner.class);
                startActivity(intent);
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,handtouchInsert.class);
                startActivity(intent);
            }
        });
    }
    //button b event
    public void button_b_event(){
        //b1
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,act_b1.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,act_b2.class);
                startActivity(intent);
            }
        });
    }

    public void button_c_event(){
        //c1
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,act_c1.class);
                startActivity(intent);
            }
        });
        //c2
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,act_c2.class);
                startActivity(intent);
            }
        });

    }
}