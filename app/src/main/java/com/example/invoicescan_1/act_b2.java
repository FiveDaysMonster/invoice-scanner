package com.example.invoicescan_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class act_b2 extends AppCompatActivity {

    /*
    SQLiteDatabase sqLiteDatabase;
    String dataBaseName="myDatabase";
    //table name
    String b2_sqlite_tableName="b2_sqlite";
    String command="";

     */
    StdDBHelper dbHelper;
    private SQLiteDatabase db;
    String str_table_b2="table_b2";

    String myYear="",myMonth="",myDate="";
    EditText e_year,e_month;
    //API
    String str_appID;
    HashMap<String,String> myDict;
    Button b_search;
    TextView t_act_b2,t2_act_b2;
    Integer searchApiFali,returnNoValue,searchSuccess,searchFail;
    //千萬特獎
    String superPrizeNo="";
    //特獎
    String[] spcPrizeNo={"","",""};
    //頭獎
    String[] firstPrizeNo={"","","","","","","","","",""};
    //六獎
    String[] sixthPrizeNo={"","","","","",""};
    //中獎狀態
    Integer get_superPrizeNo,get_spcPrizeNo,get_firstPrizeNo_1,get_firstPrizeNo_2
            ,get_firstPrizeNo_3,get_firstPrizeNo_4,get_firstPrizeNo_5, get_firstPrizeNo_6
            ,get_firstPrizeNo_none;
    ArrayList arrayList1,arrayList2,arrayList3;
    ListView listView;
    TextView t1,t2,t3;
    Boolean hasPrize=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_b2);

        //sqLiteDatabase=openOrCreateDatabase(dataBaseName, Context.MODE_PRIVATE,null);

        init();
        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList1=new ArrayList();
                arrayList2=new ArrayList();
                arrayList3=new ArrayList();
                hasPrize=false;
                t1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                t1.setText("發票日期");
                t2.setText("發票號碼");
                t3.setText("中獎金額");
                myYear=e_year.getText().toString();
                myMonth=e_month.getText().toString();
                myDate=myYear+myMonth;
                myDict=new HashMap<String, String>();
                searchAPI();
            }
        });
    }

    public void init(){
        e_year=(EditText) findViewById(R.id.e1_act_b2);
        e_month=(EditText) findViewById(R.id.e2_act_b2);
        t1=(TextView) findViewById(R.id.textView2_act_b2);
        t2=(TextView) findViewById(R.id.textView3_act_b2);
        t3=(TextView) findViewById(R.id.textView4_act_b2);

        str_appID="EINV6202011224154";
        b_search=(Button) findViewById(R.id.searchButton_act_b2);
        t_act_b2=(TextView) findViewById(R.id.textView_act_b2);
        t2_act_b2=(TextView) findViewById(R.id.textView2_act_b2);
        listView=(ListView) findViewById(R.id.listB2);

        //handle value
        searchApiFali=123;
        returnNoValue=234;
        searchSuccess=345;
        searchFail=456;

        //中1000萬
        get_superPrizeNo=10000000;
        //200萬
        get_spcPrizeNo=2000000;
        //20萬
        get_firstPrizeNo_1=200000;
        //4萬
        get_firstPrizeNo_2=40000;
        //1萬
        get_firstPrizeNo_3=10000;
        //4000
        get_firstPrizeNo_4=4000;
        //1000
        get_firstPrizeNo_5=1000;
        //200
        get_firstPrizeNo_6=200;
        //none
        get_firstPrizeNo_none=0;


    }

    public void searchAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //建立連線
                    String myUrl="https://api.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp";
                    URL url=new URL(myUrl);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    //傳入參數
                    DataOutputStream dataOutputStream=new DataOutputStream(httpURLConnection.getOutputStream());
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("version=").append(URLEncoder.encode("0.2","UTF-8")).append("&");
                    stringBuilder.append("action=").append(URLEncoder.encode("QryWinningList","UTF-8")).append("&");
                    stringBuilder.append("invTerm=").append(URLEncoder.encode(myDate,"UTF-8")).append("&");
                    stringBuilder.append("appID=").append(URLEncoder.encode(str_appID,"UTF-8")).append("&");
                    dataOutputStream.writeBytes(stringBuilder.toString());

                    dataOutputStream.flush();
                    dataOutputStream.close();

                    //get return
                    InputStream inputStream=httpURLConnection.getInputStream();

                    if(inputStream!=null){
                        InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
                        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                        String line="";
                        Integer count=0;
                        String jsonContent="";
                        while((line=bufferedReader.readLine())!=null){
                            if(count==0){
                                count+=1;
                                jsonContent=line;
                            }
                        }

                        jsonContent=jsonContent.replace("{","");
                        jsonContent=jsonContent.replace("}","");
                        String[] a1=jsonContent.split(",");
                        String[] a2;
                        String ss1,ss2;
                        //HashMap<String,String> myDict=new HashMap<String, String>();
                        for(int i=0;i<a1.length;i++){
                            a2=a1[i].split(":");
                            ss1=a2[0].replace("\"","");
                            ss2=a2[1].replace("\"","");
                            myDict.put(ss1,ss2);
                        }
                        if(myDict.get("code").equals("200")){

                            handler.sendEmptyMessage(searchSuccess);
                            set_prizeNumber();
                            test_prizeNumber();

                        }
                        else{
                            handler.sendEmptyMessage(searchFail);
                        }

                    }
                    else{
                        handler.sendEmptyMessage(returnNoValue);
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(searchApiFali);
                    Log.d("狀況",e.toString());
                }
            }
        }).start();
    }

    public void set_prizeNumber(){
        //千萬特獎號碼
        superPrizeNo=myDict.get("superPrizeNo");
        //特獎號碼
        for(int i=0;i<3;i++){
            if(i==0){
                spcPrizeNo[i]=myDict.get("spcPrizeNo");
            }
            else{
                spcPrizeNo[i]=myDict.get("spcPrizeNo"+String.valueOf(i+1));
            }
        }
        //頭獎號碼
        for(int i=0;i<10;i++){
            firstPrizeNo[i]=myDict.get("firstPrizeNo"+String.valueOf(i+1));
        }
        //六獎號碼
        for(int i=0;i<6;i++){
            sixthPrizeNo[i]=myDict.get("sixthPrizeNo"+String.valueOf(i+1));
        }
    }

    public void test_prizeNumber(){
        Log.d("千萬特獎號碼",superPrizeNo);
        for(int i=0;i<3;i++){
            Log.d("特獎號碼",spcPrizeNo[i]);
        }
        for(int i=0;i<10;i++){
            Log.d("頭獎號碼",firstPrizeNo[i]);
        }
        for(int i=0;i<6;i++){
            Log.d("六獎號碼",sixthPrizeNo[i]);
        }
    }

    //回傳中獎金額
    public Integer if_prize(String str){
        if(win_superPrizeNo(str)){
            return get_superPrizeNo;
        }
        else if(win_spcPrizeNo(str)){
            return get_spcPrizeNo;
        }
        else if(win_firstPrizeNo1(str)){
            return get_firstPrizeNo_1;
        }
        else if(win_firstPrizeNo2(str)){
            return get_firstPrizeNo_2;
        }
        else if(win_firstPrizeNo3(str)){
            return get_firstPrizeNo_3;
        }
        else if(win_firstPrizeNo4(str)){
            return get_firstPrizeNo_4;
        }
        else if(win_firstPrizeNo5(str)){
            return get_firstPrizeNo_5;
        }
        else if(win_firstPrizeNo6(str)){
            return get_firstPrizeNo_6;
        }
        else if(win_sixPrizeNo(str)){
            return get_firstPrizeNo_6;
        }
        else{
            return get_firstPrizeNo_none;
        }

    }

    //1000 wan
    public boolean win_superPrizeNo(String s){
        if(s.equals(superPrizeNo)){
            return true;
        }
        else{
            return false;
        }
    }
    //200 wan
    public boolean win_spcPrizeNo(String s){
        Boolean state=false;
        for(int i=0;i<3;i++){
            if(s.equals(spcPrizeNo[i])){
                state=true;
                break;
            }
        }
        return state;
    }
    //20 wan
    public boolean win_firstPrizeNo1(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(s.equals(firstPrizeNo[i])){
                return true;
            }
        }
        return state;
    }
    //4 wan
    public boolean win_firstPrizeNo2(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(!firstPrizeNo[i].equals("")){
                String ss="",sss="";
                for(int k=1;k<=7;k++){
                    ss+=String.valueOf(firstPrizeNo[i].charAt(k));
                    sss+=String.valueOf(s.charAt(k));
                }
                if(sss.equals(ss)){
                    state=true;
                }
            }
        }
        return state;
    }
    //1 wan
    public boolean win_firstPrizeNo3(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(!firstPrizeNo[i].equals("")){
                String ss="",sss="";
                for(int k=2;k<=7;k++){
                    ss+=String.valueOf(firstPrizeNo[i].charAt(k));
                    sss+=String.valueOf(s.charAt(k));
                }
                if(sss.equals(ss)){
                    state=true;
                }
            }
        }
        return state;
    }
    //4000
    public boolean win_firstPrizeNo4(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(!firstPrizeNo[i].equals("")){
                String ss="",sss="";
                for(int k=3;k<=7;k++){
                    ss+=String.valueOf(firstPrizeNo[i].charAt(k));
                    sss+=String.valueOf(s.charAt(k));
                }
                if(sss.equals(ss)){
                    state=true;
                }
            }
        }
        return state;
    }
    //1000
    public boolean win_firstPrizeNo5(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(!firstPrizeNo[i].equals("")){
                String ss="",sss="";
                for(int k=4;k<=7;k++){
                    ss+=String.valueOf(firstPrizeNo[i].charAt(k));
                    sss+=String.valueOf(s.charAt(k));
                }
                if(sss.equals(ss)){
                    state=true;
                }
            }
        }
        return state;
    }
    //200
    public boolean win_firstPrizeNo6(String s){
        Boolean state=false;
        for(int i=0;i<10;i++){
            if(!firstPrizeNo[i].equals("")){
                String ss="",sss="";
                for(int k=5;k<=7;k++){
                    ss+=String.valueOf(firstPrizeNo[i].charAt(k));
                    sss+=String.valueOf(s.charAt(k));
                }
                if(sss.equals(ss)){
                    state=true;
                }
            }
        }
        return state;
    }
    //prize6 200
    public boolean win_sixPrizeNo(String s){
        Boolean state=false;
        String threeCodeS="";
        for(int i=5;i<=7;i++){
            threeCodeS+=String.valueOf(s.charAt(i));
        }
        for(int i=0;i<6;i++){
            if(!sixthPrizeNo[i].equals("")){
                if(threeCodeS.equals(sixthPrizeNo[i])){
                    state=true;
                }
            }
        }
        return state;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==searchApiFali){
                t_act_b2.setText("發生錯誤");
            }
            else if(msg.what==returnNoValue){
                t_act_b2.setText("回傳空值");
            }
            else if(msg.what==searchSuccess){
                t_act_b2.setText("查詢成功");
                //----------------------------------
                dbHelper=new StdDBHelper(act_b2.this,1);
                db=dbHelper.getWritableDatabase();
                Cursor c=db.rawQuery("SELECT * FROM "+str_table_b2,null);
                if(c.moveToFirst()){
                    //期別相同
                    if(c.getString(3).equals(myDate)){
                        Integer myPrize=0;
                        //中獎
                        if(if_prize(c.getString(1))!=0){
                            hasPrize=true;
                            arrayList1.add(c.getString(2));
                            arrayList2.add(c.getString(1));
                            arrayList3.add(String.valueOf(if_prize(c.getString(1))));
                            Log.d("發票號碼",c.getString(1));
                        }
                    }
                    while(c.moveToNext()){
                        if(c.getString(3).equals(myDate)){
                            Integer myPrize=0;
                            //中獎
                            if(if_prize(c.getString(1))!=0){
                                hasPrize=true;
                                arrayList1.add(c.getString(2));
                                arrayList2.add(c.getString(1));
                                arrayList3.add(String.valueOf(if_prize(c.getString(1))));
                                Log.d("發票號碼",c.getString(1));
                            }
                        }
                    }
                    if(hasPrize){
                        listView.setAdapter(new myAdapter());
                        setListViewHeightBasedOnChildren(listView);
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        t3.setVisibility(View.VISIBLE);
                    }
                    else{
                        listView.setAdapter(new myAdapter());
                        setListViewHeightBasedOnChildren(listView);
                        t1.setText("無中獎");
                        t1.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    t2_act_b2.setText("此期別無發票");
                }
            }
            else if(msg.what==searchFail){
                t_act_b2.setText(myDict.get("msg"));
            }
        }
    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 100;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList1.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=getLayoutInflater().inflate(R.layout.b2_list,null);
            TextView t1,t2,t3;
            t1=(TextView) v.findViewById(R.id.t1_b2ListView);
            t2=(TextView) v.findViewById(R.id.t2_b2ListView);
            t3=(TextView) v.findViewById(R.id.t3_b2ListView);
            t1.setText(String.valueOf(arrayList1.get(position)));
            t2.setText(String.valueOf(arrayList2.get(position)));
            t3.setText(String.valueOf(arrayList3.get(position)));
            return v;
        }
    }

}