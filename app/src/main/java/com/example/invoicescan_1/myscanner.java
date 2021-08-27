package com.example.invoicescan_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class myscanner extends AppCompatActivity {

    Integer table_b2=1;
    String str_table_b2="table_b2";

    StdDBHelper dbHelper;
    private SQLiteDatabase db;

    String str_myDay;

    IntentIntegrator intentIntegrator;
    ImageButton action_bar_button1,action_bar_button2,action_bar_button3;
    ListView list_myItem;
    //小計
    Integer totalCost=0;
    //List 上面的空見
    String[] spinnerItem={"食","衣","住","行","育","樂","其他","折扣","捨棄"};
    //textview
    TextView t_myscanner,t_sellerName,t_fiveCodeDate,t_itemNum;
    String str_myscanner_content="";
    //scan state
    Boolean scan_finish=false,API_state=false;
    //驗證碼
    String str_UUID="",str_randomNumber="";
    //期別
    String str_fiveCodeDate;
    //年月日
    String str_invoiceTime;
    //發票表頭、發票body、發票號碼
    String str_invoiceHead,str_invoiceBody,str_invoiceNumber;
    Boolean date_pick_state=false;
    //品項數量
    Integer itemNum=0;
    //發票detail
    String str_sellerName="",str_itemNum=" ";
    //API回傳狀態
    Boolean APIstate=false;
    String APIstateDetail="";
    ArrayList arrayList1,arrayList2;
    ArrayAdapter arrayAdapter1,arrayAdapter2;
    Boolean listState1=false,listState2=false,listFinish=false;
    Boolean dictFinish=false;
    HashMap<String,String> myDict;
    Integer whichList=1;
    Button save_button;
    String str_myYear,str_myMonth;

    ArrayList<Integer> arrayList_7;
    ArrayList<Integer> which_spinner;
    ArrayList<Integer> single_cost;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==100){
                for(int i=0;i<single_cost.size();i++){
                    single_cost.set(i,0);
                }
                whichList=1;
                arrayList1=new ArrayList();
                arrayList1.add("1："+myDict.get("description1"));
                arrayList1.add("數量："+myDict.get("quantity1"));
                arrayList1.add("單價："+myDict.get("unitPrice1"));
                arrayList1.add("小計："+myDict.get("amount1"));
                single_cost.set(0,Integer.parseInt(myDict.get("amount1")));
                totalCost=Integer.valueOf(myDict.get("amount1"));
                t_myscanner.setText("總金額："+String.valueOf(totalCost));
                list_myItem.setAdapter(new myListAdapter());
                save_button.setVisibility(View.VISIBLE);
                Log.d("現成狀態","完成");

            }
            else if(msg.what==200){
                whichList=2;
                arrayList2=new ArrayList();
                totalCost=0;
                for(int i=0;i<single_cost.size();i++){
                    single_cost.set(i,0);
                }
                for(int ii=1;ii<=itemNum;ii++){
                    arrayList2.add(String.valueOf(ii)+"："+myDict.get("description"+String.valueOf(ii)));
                    arrayList2.add("數量："+myDict.get("quantity"+String.valueOf(ii)));
                    arrayList2.add("單價："+myDict.get("unitPrice"+String.valueOf(ii)));
                    arrayList2.add("小計："+myDict.get("amount"+String.valueOf(ii)));
                    single_cost.set(ii-1,Integer.parseInt(myDict.get("amount"+String.valueOf(ii))));
                    totalCost+=Integer.valueOf(myDict.get("amount"+String.valueOf(ii)));
                }
                t_myscanner.setText("總金額："+String.valueOf(totalCost));
                list_myItem.setAdapter(new myListAdapter());
                save_button.setVisibility(View.VISIBLE);
                Log.d("現成狀態","完成");
            }
            else if(msg.what==300){
                t_myscanner.setText("發生錯誤");
            }
            else if(msg.what==400){
                t_myscanner.setText("不是發票");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscanner);
        Stetho.initializeWithDefaults(this);

        save_button=(Button) findViewById(R.id.button_myscanner_save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<9;i++){
                    arrayList_7.set(i,0);
                }
                for(int i=0;i<which_spinner.size();i++){
                    Integer ori=arrayList_7.get(which_spinner.get(i));
                    arrayList_7.set(which_spinner.get(i),ori+single_cost.get(i));
                }
                putData_b2();
                Toast.makeText(myscanner.this,"成功儲存",Toast.LENGTH_LONG).show();
            }
        });


        list_myItem=(ListView) findViewById(R.id.list_myItem_myscanner);
        intentIntegrator=new IntentIntegrator(myscanner.this);
        t_myscanner=(TextView) findViewById(R.id.text_myscanner);
        arrayList_7=new ArrayList<Integer>();
        which_spinner=new ArrayList<Integer>();
        single_cost=new ArrayList<Integer>();
        reset_arrayList7();
        //make action bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.actionbar_myscanner,null);
        actionBar.setCustomView(view,new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.MATCH_PARENT));
        action_bar_button1=(ImageButton) view.findViewById(R.id.action_bar_button);
        action_bar_button2=(ImageButton) view.findViewById(R.id.action_bar_button2);
        //action bar button
        action_bar_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                intentIntegrator.setPrompt("將電子發票BarCode放置掃描區域內");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.setCaptureActivity(myscanner2.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

        action_bar_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(myscanner.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dictFinish=false;
        t_myscanner.setText("");
        myDict=new HashMap<String, String>();
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        get_alphaAndNumber(intentResult);

        //alertdialog
        if(scan_finish){
            Calendar calendar=Calendar.getInstance();
            DatePickerDialog datePickerDialog=new DatePickerDialog(myscanner.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String myMonth,myDay;
                    if(month+1<10){
                        myMonth="0"+String.valueOf(month+1);
                    }
                    else{
                        myMonth=String.valueOf(month+1);
                    }

                    if(dayOfMonth<10){
                        myDay="0"+String.valueOf(dayOfMonth);
                        str_myDay=myDay;
                    }
                    else{
                        myDay=String.valueOf(dayOfMonth);
                        str_myDay=myDay;
                    }
                    //設定參數日期
                    str_invoiceTime= valueOf(year)+"/"
                            +myMonth+"/"
                            +myDay;
                    str_myMonth=myMonth;
                    str_myYear=String.valueOf(year-1911);
                    searchAPI();
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }
    }
    //set title_alpha  and  target_number
    //is invoice?--->scan_finish
    public void get_alphaAndNumber(IntentResult intentResult){
        if(intentResult!=null){
            str_myscanner_content=intentResult.getContents();
            if(is19(str_myscanner_content)){
                //5碼日期
                str_fiveCodeDate=getInfo(intentResult.getContents(),0,4);
                //發票表頭
                str_invoiceHead=getInfo(intentResult.getContents(),5,6);
                //invoice body
                str_invoiceBody=getInfo(intentResult.getContents(),7,14);
                //invoice number
                str_invoiceNumber=str_invoiceHead+str_invoiceBody;
                //random code
                str_randomNumber=getInfo(intentResult.getContents(),15,18);
                //UUID
                str_UUID=android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                if(isInvoice(str_invoiceHead,str_invoiceBody)){
                    t_myscanner.setText("成功");
                    Toast.makeText(myscanner.this,"success",Toast.LENGTH_LONG).show();
                    scan_finish=true;
                }
                else{
                    t_myscanner.setText("失敗");
                    Toast.makeText(myscanner.this,"fail",Toast.LENGTH_LONG);
                }
            }
            else{
                handler.sendEmptyMessage(400);
                Log.d("is invoice ","not invoice");
                Toast.makeText(myscanner.this,"fail",Toast.LENGTH_LONG);
            }
        }
        else{
            Toast.makeText(myscanner.this,"null",Toast.LENGTH_LONG).show();
            scan_finish=false;
            t_myscanner.setText("掃描失敗");
        }




    }

    public boolean isInvoice(String titleAlpha,String invoiceNum){
        if((isAlpha(titleAlpha))&&(isNum(invoiceNum))){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isNum(String str){
        Pattern pattern=Pattern.compile("[0-9]*");
        Matcher myStr=pattern.matcher(str);
        if(!myStr.matches()){
            return false;
        }
        else{
            if(str.length()==8){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean isAlpha(String str){
        Pattern pattern=Pattern.compile("[A-Z]*");
        Matcher matcher=pattern.matcher(str);
        if(!matcher.matches()){
            return false;
        }
        else{
            if(str.length()==2){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean is19(String str){
        if(str.length()==19){
            return true;
        }
        else{
            return false;
        }
    }

    public String getInfo(String str,Integer n1,Integer n2){
        String s="";
        for(int i=n1;i<=n2;i++){
            s+= valueOf(str.charAt(i));
        }
        return s;
    }

    public void searchAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    API_state=false;
                    listState1=false;
                    listState2=false;
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
                    stringBuilder.append("version=").append(URLEncoder.encode("0.5","UTF-8")).append("&");
                    stringBuilder.append("type=").append(URLEncoder.encode("Barcode","UTF-8")).append("&");
                    stringBuilder.append("invNum=").append(URLEncoder.encode(str_invoiceNumber,"UTF-8")).append("&");
                    stringBuilder.append("action=").append(URLEncoder.encode("qryInvDetail","UTF-8")).append("&");
                    stringBuilder.append("generation=").append(URLEncoder.encode("V2","UTF-8")).append("&");
                    stringBuilder.append("invTerm=").append(URLEncoder.encode(str_fiveCodeDate,"UTF-8")).append("&");
                    stringBuilder.append("invDate=").append(URLEncoder.encode(str_invoiceTime,"UTF-8")).append("&");
                    stringBuilder.append("UUID=").append(URLEncoder.encode(str_UUID,"UTF-8")).append("&");
                    stringBuilder.append("randomNumber=").append(URLEncoder.encode(str_randomNumber,"UTF-8")).append("&");
                    stringBuilder.append("appID=").append(URLEncoder.encode("EINV6202011224154","UTF-8")).append("&");
                    dataOutputStream.writeBytes(stringBuilder.toString());

                    dataOutputStream.flush();
                    dataOutputStream.close();

                    //get return
                    InputStream inputStream=httpURLConnection.getInputStream();
                    Integer now_status=httpURLConnection.getResponseCode();
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
                        //json
                        JSONObject jsonObject=new JSONObject(jsonContent);
                        //API state
                        if(String.valueOf(jsonObject.get("code")).equals("200")){
                            API_state=true;
                        }
                        else{
                            API_state=false;
                            APIstateDetail=String.valueOf(jsonObject.get("msg"));
                        }
                        Log.d("發票號碼",String.valueOf(jsonObject.get("invNum")));
                        Log.d("發票開立日期",String.valueOf(jsonObject.get("invDate")));
                        Log.d("賣方名稱",String.valueOf(jsonObject.get("sellerName")));
                        Log.d("對獎發票期別",String.valueOf(jsonObject.get("invPeriod")));
                        Log.d("品項個數",String.valueOf(count_(String.valueOf(jsonObject.getJSONArray("details")))));
                        str_sellerName=String.valueOf(jsonObject.get("sellerName"));
                        str_itemNum=String.valueOf(count_(String.valueOf(jsonObject.getJSONArray("details"))));
                        itemNum=count_(String.valueOf(jsonObject.getJSONArray("details")));
                        for(int i=0;i<itemNum;i++){
                            which_spinner.add(0);
                            single_cost.add(0);
                        }
                        //details 個個數據及
                        String myData=String.valueOf(jsonObject.getJSONArray("details"));
                        System.out.println(myData);
                        myData=myData.replace("\"","");
                        System.out.println(myData);
                        myData=myData.replace("[","");
                        System.out.println(myData);
                        myData=myData.replace("]","");
                        System.out.println(myData);
                        //只有一個品項
                        //HashMap<String,String> myDict=new HashMap<String, String>();
                        if(count_(myData)==1){
                            String[] s1=myData.split(",");
                            for(int i=0;i<s1.length;i++){
                                String[] s2=s1[i].split(":");
                                s2[0]=s2[0].replace("{","");
                                s2[0]=s2[0].replace("}","");
                                s2[1]=s2[1].replace("{","");
                                s2[1]=s2[1].replace("}","");
                                myDict.put(s2[0]+"1",s2[1]);
                            }
                            handler.sendEmptyMessage(100);
                        }
                        else if(count_(myData)>1){
                            String[] s1=myData.split("\\},");
                            for(int i=0;i<s1.length;i++){
                                System.out.println("狀態一:"+s1[i]);
                                String[] s2=s1[i].split(",");
                                for(int k=0;k<s2.length;k++){
                                    String[] s3=s2[k].split(":");
                                    s3[0]=s3[0].replace("{","");
                                    s3[0]=s3[0].replace("}","");
                                    s3[1]=s3[1].replace("{","");
                                    s3[1]=s3[1].replace("}","");
                                    myDict.put(s3[0]+String.valueOf(i+1),s3[1]);
                                    Log.d("字典狀態",s3[0]+String.valueOf(i+1));
                                }
                            }
                            handler.sendEmptyMessage(200);
                        }
                    }
                }catch (Exception e){
                    handler.sendEmptyMessage(300);
                    t_myscanner.setText(e.toString());
                    Log.d("意外",e.toString());
                }
            }
        }).start();
    }

    public Integer count_(String str){
        Integer i=0;
        while(str.indexOf("{")!=-1){
            Integer a=str.indexOf("{");
            str=str.substring(a+1);
            i++;
        }
        return i;
    }

    public class myListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemNum*4;
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
            View v=getLayoutInflater().inflate(R.layout.list_myscanner,null);
            Spinner myspinner=(Spinner) v.findViewById(R.id.list_spinner_myscanner);
            //Log.d("現在位置",String.valueOf(position));
            if(position==0 ||position%4==0){
                myspinner.setVisibility(View.VISIBLE);
                myspinner.setAdapter(new ArrayAdapter<>(myscanner.this,android.R.layout.simple_list_item_1,spinnerItem));
            }
            else{
                myspinner.setVisibility(View.INVISIBLE);
            }
            final TextView textView=(TextView) v.findViewById(R.id.list_text_myscanner);
            if(whichList==1){

                textView.setText(String.valueOf(arrayList1.get(position)));
            }
            else if(whichList==2){

                textView.setText(String.valueOf(arrayList2.get(position)));
            }
            myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String s1=textView.getText().toString();
                    String s2=String.valueOf(s1.charAt(0));
                    Integer index=Integer.parseInt(s2)-1;
                    which_spinner.set(index,position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return v;
        }
    }
    public void putData_b2(){
        dbHelper=new StdDBHelper(myscanner.this,1);
        db=dbHelper.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+str_table_b2,null);
        Boolean state=false;
        String id=str_fiveCodeDate+str_invoiceBody;
        if(c.moveToFirst()){
            if(c.getString(0).equals(id)){
                state=true;
                ContentValues cv=new ContentValues();
                cv.put("invoiceNumber",str_invoiceBody);
                cv.put("invoiceDate",str_invoiceTime);
                cv.put("fiveCodeDate",str_fiveCodeDate);
                cv.put("dayOfMonth",Integer.parseInt(str_myDay));
                cv.put("amount",totalCost);
                cv.put("fiveCodeDate2",str_myYear+str_myMonth);
                cv.put("food",arrayList_7.get(0));
                cv.put("clothes",arrayList_7.get(1));
                cv.put("livecost",arrayList_7.get(2));
                cv.put("gowalk",arrayList_7.get(3));
                cv.put("edu",arrayList_7.get(4));
                cv.put("havefun",arrayList_7.get(5));
                cv.put("otheract",arrayList_7.get(6));
                db.update(str_table_b2,cv,"_id="+id,null);
            }
            while(c.moveToNext()){
                if(c.getString(0).equals(id)){
                    state=true;
                    ContentValues cv=new ContentValues();
                    cv.put("invoiceNumber",str_invoiceBody);
                    cv.put("invoiceDate",str_invoiceTime);
                    cv.put("fiveCodeDate",str_fiveCodeDate);
                    cv.put("dayOfMonth",Integer.parseInt(str_myDay));
                    cv.put("amount",totalCost);
                    cv.put("fiveCodeDate2",str_myYear+str_myMonth);
                    cv.put("food",arrayList_7.get(0));
                    cv.put("clothes",arrayList_7.get(1));
                    cv.put("livecost",arrayList_7.get(2));
                    cv.put("gowalk",arrayList_7.get(3));
                    cv.put("edu",arrayList_7.get(4));
                    cv.put("havefun",arrayList_7.get(5));
                    cv.put("otheract",arrayList_7.get(6));
                    db.update(str_table_b2,cv,"_id="+id,null);
                }
            }
            if(state==false){
                ContentValues cv=new ContentValues();
                cv.put("_id",str_fiveCodeDate+str_invoiceBody);
                cv.put("invoiceNumber",str_invoiceBody);
                cv.put("invoiceDate",str_invoiceTime);
                cv.put("fiveCodeDate",str_fiveCodeDate);
                cv.put("dayOfMonth",Integer.parseInt(str_myDay));
                cv.put("amount",totalCost);
                cv.put("fiveCodeDate2",str_myYear+str_myMonth);
                cv.put("food",arrayList_7.get(0));
                cv.put("clothes",arrayList_7.get(1));
                cv.put("livecost",arrayList_7.get(2));
                cv.put("gowalk",arrayList_7.get(3));
                cv.put("edu",arrayList_7.get(4));
                cv.put("havefun",arrayList_7.get(5));
                cv.put("otheract",arrayList_7.get(6));
                db.insert(str_table_b2,null,cv);
            }
        }
        else{
            ContentValues cv=new ContentValues();
            cv.put("_id",str_fiveCodeDate+str_invoiceBody);
            cv.put("invoiceNumber",str_invoiceBody);
            cv.put("invoiceDate",str_invoiceTime);
            cv.put("fiveCodeDate",str_fiveCodeDate);
            cv.put("dayOfMonth",Integer.parseInt(str_myDay));
            cv.put("amount",totalCost);
            cv.put("fiveCodeDate2",str_myYear+str_myMonth);
            cv.put("food",arrayList_7.get(0));
            cv.put("clothes",arrayList_7.get(1));
            cv.put("livecost",arrayList_7.get(2));
            cv.put("gowalk",arrayList_7.get(3));
            cv.put("edu",arrayList_7.get(4));
            cv.put("havefun",arrayList_7.get(5));
            cv.put("otheract",arrayList_7.get(6));
            db.insert(str_table_b2,null,cv);
        }
    }
    public void reset_arrayList7(){
        for(int i=0;i<9;i++){
            arrayList_7.add(0);
        }

    }
}