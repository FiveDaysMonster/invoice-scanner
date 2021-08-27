package com.example.invoicescan_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class handtouchInsert extends AppCompatActivity {

    //tablename
    Integer table_b2=1;
    String str_table_b2="table_b2";

    StdDBHelper dbHelper;
    private SQLiteDatabase db;


    Button button,save_button;
    String invoiceNumber="";
    EditText e_alpha,e_number,e_random;
    DatePicker datePicker;
    String invoiceDate="";
    Integer myMonth=0;
    String str_myMonth="",str_myYear="";
    String str_myDay="";
    String invoiceRandomNumber="";

    Boolean API_state=false;
    //發票期別
    String invoiceFiveDate="";
    //API
    String invoiceHead="",invoiceBody="";
    String APIstateDetail="";
    //偵測資料是否完整
    Boolean dataFinishInsert=false;
    //UUID
    String str_UUID="";
    //DATA
    String str_sellerName="",str_itemNum="";
    Integer itemNum=0,whichList=1,totalCost=0;
    TextView t_myscanner;
    ArrayList arrayList1,arrayList2;
    HashMap<String,String> myDict;
    ListView list_myItem;
    WindowManager windowManager;
    WindowManager.LayoutParams wl;
    String[] spinnerItem={"食","衣","住","行","育","樂","其他","折扣","捨棄"};

    ArrayList<Integer> arrayList_7;
    ArrayList<Integer> which_spinner;
    ArrayList<Integer> single_cost;
    Integer now_super_pos=0;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==100){
                whichList=1;
                for(int i=0;i<single_cost.size();i++){
                    single_cost.set(i,0);
                }
                arrayList1=new ArrayList();
                //arrayList1.add("品項名稱："+myDict.get("description1"));
                arrayList1.add("1："+myDict.get("description1"));
                arrayList1.add("數量："+myDict.get("quantity1"));
                arrayList1.add("單價："+myDict.get("unitPrice1"));
                arrayList1.add("小計："+myDict.get("amount1"));
                single_cost.set(0,Integer.parseInt(myDict.get("amount1")));
                totalCost=Integer.valueOf(myDict.get("amount1"));
                t_myscanner.setText("總金額："+String.valueOf(totalCost));
                Log.d("清單長度1",String.valueOf(arrayList1.size()));
                list_myItem.setAdapter(new myListAdapter());
                setListViewHeightBasedOnChildren(list_myItem);
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
                Log.d("清單長度2",String.valueOf(arrayList2.size()));
                t_myscanner.setText("總金額："+String.valueOf(totalCost));
                list_myItem.setAdapter(new myListAdapter());
                setListViewHeightBasedOnChildren(list_myItem);
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
        setContentView(R.layout.activity_handtouch_insert);

        init();
        reset_arrayList7();
        button_event();

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
                Toast.makeText(handtouchInsert.this,"成功儲存",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void init(){
        windowManager=getWindowManager();
        // vv=getLayoutInflater().inflate(R.layout.handtouch2,null);
        e_alpha=(EditText) findViewById(R.id.e1_handtouch);
        e_number=(EditText) findViewById(R.id.e2_handtouch);
        e_random=(EditText) findViewById(R.id.random_handtouch);
        datePicker=(DatePicker) findViewById(R.id.datepicker_handtouch);
        button=(Button) findViewById(R.id.button_handtouch);
        str_UUID=android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        save_button=(Button) findViewById(R.id.saveButton_handtouch);
        list_myItem=(ListView) findViewById(R.id.listView_handtouch);
        t_myscanner=(TextView) findViewById(R.id.textview_handtouch);

        arrayList_7=new ArrayList<Integer>();
        which_spinner=new ArrayList<Integer>();
        single_cost=new ArrayList<Integer>();
    }

    //發票號碼(invoiceNumber)
    public void set_invoiceNumber(){
        invoiceHead=e_alpha.getText().toString();
        invoiceBody=e_number.getText().toString();
        invoiceNumber=e_alpha.getText().toString()+e_number.getText().toString();
    }
    //發票日期(invoiceDate)
    public void set_invoiceDate(){
        myMonth=datePicker.getMonth()+1;//獲得月份
        //獲得發票日期
        if(myMonth<10){
            str_myMonth="0"+String.valueOf(myMonth);
        }
        else{
            str_myMonth=String.valueOf(myMonth);
        }
        if(datePicker.getDayOfMonth()<10){
            str_myDay="0"+String.valueOf(datePicker.getDayOfMonth());
        }
        else{
            str_myDay=String.valueOf(datePicker.getDayOfMonth());
        }
        str_myYear=String.valueOf(datePicker.getYear()-1911);
        invoiceDate=String.valueOf(datePicker.getYear())+"/"+
                str_myMonth+"/"+
                str_myDay;
        //Toast.makeText(handtouchInsert.this,invoiceDate,Toast.LENGTH_LONG).show();
    }
    //發票期別(invoiceFiveDate)
    public void set_fiveDateCode(){
        Integer myMinYear=datePicker.getYear()-1911;
        Integer myMon=datePicker.getMonth()+1;
        if(myMon%2==1){
            myMon+=1;
        }
        String m=String.valueOf(myMon);
        if(myMon<10){
            m="0"+m;
        }
        invoiceFiveDate=String.valueOf(myMinYear)+m;
        //Toast.makeText(handtouchInsert.this,invoiceFiveDate,Toast.LENGTH_LONG).show();
    }
    //隨機碼(invoiceRandomNumber)
    public void set_invoiceRandomNumber(){
        invoiceRandomNumber=e_random.getText().toString();
    }

    public void if_data_finish(){
        if(((invoiceHead.length()==2)&&(invoiceBody.length()==8))&&(invoiceRandomNumber.length()==4)){
            dataFinishInsert=true;
        }
        else{
            dataFinishInsert=false;
        }
    }

    public void button_event(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_invoiceNumber();
                set_invoiceDate();
                set_fiveDateCode();
                set_invoiceRandomNumber();
                if_data_finish();
                if(dataFinishInsert){
                    myDict=new HashMap<String, String>();
                    searchAPI();
                    //Toast.makeText(handtouchInsert.this,"完成",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(handtouchInsert.this,"資料未輸入完整",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void searchAPI(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
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
                    stringBuilder.append("invNum=").append(URLEncoder.encode(invoiceNumber,"UTF-8")).append("&");
                    stringBuilder.append("action=").append(URLEncoder.encode("qryInvDetail","UTF-8")).append("&");
                    stringBuilder.append("generation=").append(URLEncoder.encode("V2","UTF-8")).append("&");
                    stringBuilder.append("invTerm=").append(URLEncoder.encode(invoiceFiveDate,"UTF-8")).append("&");
                    stringBuilder.append("invDate=").append(URLEncoder.encode(invoiceDate,"UTF-8")).append("&");
                    stringBuilder.append("UUID=").append(URLEncoder.encode(str_UUID,"UTF-8")).append("&");
                    stringBuilder.append("randomNumber=").append(URLEncoder.encode(invoiceRandomNumber,"UTF-8")).append("&");
                    stringBuilder.append("appID=").append(URLEncoder.encode("EINV6202011224154","UTF-8")).append("&");
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
            now_super_pos=position;
            View v=getLayoutInflater().inflate(R.layout.handtouch2style,null);
            Spinner myspinner=(Spinner) v.findViewById(R.id.list_spinner_handtouch);
            //Log.d("現在位置",String.valueOf(position));
            if(position==0 ||position%4==0){
                myspinner.setVisibility(View.VISIBLE);
                myspinner.setAdapter(new ArrayAdapter<>(handtouchInsert.this,android.R.layout.simple_list_item_1,spinnerItem));
            }
            else{
                myspinner.setVisibility(View.INVISIBLE);
            }
            final TextView textView=(TextView) v.findViewById(R.id.list_text_handtouch);
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

    public void putData_b2(){

        dbHelper=new StdDBHelper(handtouchInsert.this,1);
        db=dbHelper.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+str_table_b2,null);
        Boolean state=false;
        String id=invoiceFiveDate+invoiceBody;
        if(c.moveToFirst()){
            if(c.getString(0).equals(id)){
                state=true;
                ContentValues cv=new ContentValues();
                cv.put("invoiceNumber",invoiceBody);
                cv.put("invoiceDate",invoiceDate);
                cv.put("fiveCodeDate",invoiceFiveDate);
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
                    cv.put("invoiceNumber",invoiceBody);
                    cv.put("invoiceDate",invoiceDate);
                    cv.put("fiveCodeDate",invoiceFiveDate);
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
                cv.put("_id",invoiceFiveDate+invoiceBody);
                cv.put("invoiceNumber",invoiceBody);
                cv.put("invoiceDate",invoiceDate);
                cv.put("fiveCodeDate",invoiceFiveDate);
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
            cv.put("_id",invoiceFiveDate+invoiceBody);
            cv.put("invoiceNumber",invoiceBody);
            cv.put("invoiceDate",invoiceDate);
            cv.put("fiveCodeDate",invoiceFiveDate);
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