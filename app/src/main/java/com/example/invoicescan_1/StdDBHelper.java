package com.example.invoicescan_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StdDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Class13";
    private static final int DATABASE_VERSION = 1;
    String T;
    Integer which;
    Integer b2=1;
    public StdDBHelper(Context context,Integer which) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.which=which;
        if(which==1){
            T="table_b2";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(which==b2){
            db.execSQL("CREATE TABLE IF NOT EXISTS "+T+"(_id TEXT primary key,invoiceNumber TEXT,invoiceDate TEXT" +
                    ",fiveCodeDate TEXT,dayOfMonth integer,amount integer,fiveCodeDate2 TEXT" +
                    ",food integer,clothes integer,livecost integer,gowalk integer,edu integer,havefun integer,otheract integer)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(which==b2){
            db.execSQL("DROP TABLE IF EXISTS "+T);
            onCreate(db);
        }
    }
}
