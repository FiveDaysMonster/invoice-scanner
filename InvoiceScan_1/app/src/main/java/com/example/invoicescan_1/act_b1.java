package com.example.invoicescan_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class act_b1 extends AppCompatActivity {

    EditText pick_year,pick_month;
    Button button_search;
    String s1,s2;
    String superPrizeNo="";
    String[] spcPrizeNo,firstPrizeNo,sixthPrizeNo;
    TextView t_superPrizeNo,t_spcPrizeNo,t_firstPrizeNo,t_sixthPrizeNo,t_sixthPrizeNo2,t_sixthPrizeNo3;
    String six1="",six2="",six3="";
    TextView t_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_b1);

        pick_year=(EditText) findViewById(R.id.spinner1_act_b1);
        pick_month=(EditText) findViewById(R.id.spinner2_act_b1);
        button_search=(Button) findViewById(R.id.button_search_act_b1);
        t_superPrizeNo=(TextView) findViewById(R.id.t_b1_superPrizeNo);
        spcPrizeNo=new String[3];
        t_spcPrizeNo=(TextView) findViewById(R.id.t_b1_spcPrizeNo);
        firstPrizeNo=new String[10];
        t_firstPrizeNo=(TextView) findViewById(R.id.t_b1_firstPrizeNo);
        sixthPrizeNo=new String[3];
        t_sixthPrizeNo=(TextView) findViewById(R.id.t_b1_sixthPrizeNo);
        t_sixthPrizeNo2=(TextView) findViewById(R.id.t_b1_sixthPrizeNo2);
        t_sixthPrizeNo3=(TextView) findViewById(R.id.t_b1_sixthPrizeNo3);
        t_date=(TextView) findViewById(R.id.t_date_act_b1);
        //api search

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_s1_s2();
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
                            stringBuilder.append("version=").append(URLEncoder.encode("0.2","UTF-8")).append("&");
                            stringBuilder.append("action=").append(URLEncoder.encode("QryWinningList","UTF-8")).append("&");
                            stringBuilder.append("invTerm=").append(URLEncoder.encode(s1+s2,"UTF-8")).append("&");
                            stringBuilder.append("appID=").append(URLEncoder.encode("EINV6202011224154","UTF-8")).append("&");
                            dataOutputStream.writeBytes(stringBuilder.toString());

                            dataOutputStream.flush();
                            dataOutputStream.close();

                            //get return
                            InputStream inputStream=httpURLConnection.getInputStream();
                            Integer now_status=httpURLConnection.getResponseCode();

                            //read data
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

                                jsonContent=jsonContent.replace("{","");
                                jsonContent=jsonContent.replace("}","");
                                String[] a1=jsonContent.split(",");
                                String[] a2;
                                String ss1,ss2;
                                HashMap<String,String> myDict=new HashMap<String, String>();
                                for(int i=0;i<a1.length;i++){
                                    a2=a1[i].split(":");
                                    ss1=a2[0].replace("\"","");
                                    ss2=a2[1].replace("\"","");
                                    myDict.put(ss1,ss2);
                                }

                                //Log.d("訊息回應馬",myDict.get("code"));
                                //Log.d("系土回應碼",myDict.get("msg"));
                                if(myDict.get("code").equals("200")){
                                    //date
                                    t_date.setText("期別："+myDict.get("invoYm"));
                                    //---特別獎
                                    superPrizeNo=myDict.get("superPrizeNo");

                                    try{
                                        t_superPrizeNo.setText(superPrizeNo);
                                    }catch (Exception e){
                                        t_superPrizeNo.setText("");
                                    }
                                    //---特獎
                                    spcPrizeNo[0]=myDict.get("spcPrizeNo");
                                    spcPrizeNo[1]=myDict.get("spcPrizeNo2");
                                    spcPrizeNo[2]=myDict.get("spcPrizeNo3");
                                    try{
                                        String s_spcPrizeNo="";
                                        for(int i=0;i<3;i++){
                                            if(!(spcPrizeNo[i].matches(""))){
                                                if(i==0){
                                                    s_spcPrizeNo+=spcPrizeNo[i];
                                                    //t_spcPrizeNo.setText(spcPrizeNo[i]);
                                                }
                                                else{
                                                    s_spcPrizeNo+="\n"+spcPrizeNo[i];
                                                    //t_spcPrizeNo.setText("\n"+spcPrizeNo[i]);
                                                }
                                            }
                                        }
                                        t_spcPrizeNo.setText(s_spcPrizeNo);
                                    }catch (Exception e){
                                        t_spcPrizeNo.setText("");
                                    }
                                    //---頭獎
                                    firstPrizeNo[0]=myDict.get("firstPrizeNo1");
                                    firstPrizeNo[1]=myDict.get("firstPrizeNo2");
                                    firstPrizeNo[2]=myDict.get("firstPrizeNo3");
                                    firstPrizeNo[3]=myDict.get("firstPrizeNo4");
                                    firstPrizeNo[4]=myDict.get("firstPrizeNo5");
                                    firstPrizeNo[5]=myDict.get("firstPrizeNo6");
                                    firstPrizeNo[6]=myDict.get("firstPrizeNo7");
                                    firstPrizeNo[7]=myDict.get("firstPrizeNo8");
                                    firstPrizeNo[8]=myDict.get("firstPrizeNo9");
                                    firstPrizeNo[9]=myDict.get("firstPrizeNo10");

                                    try{
                                        String s_firstPrizeNo="";
                                        for(int i=0;i<3;i++){
                                            if(!(firstPrizeNo[i].matches(""))){
                                                if(i==0){
                                                    s_firstPrizeNo+=firstPrizeNo[i];
                                                    //t_spcPrizeNo.setText(spcPrizeNo[i]);
                                                }
                                                else{
                                                    s_firstPrizeNo+="\n"+firstPrizeNo[i];
                                                    //t_spcPrizeNo.setText("\n"+spcPrizeNo[i]);
                                                }
                                            }
                                        }
                                        t_firstPrizeNo.setText(s_firstPrizeNo);
                                    }catch (Exception e){
                                        t_firstPrizeNo.setText("");
                                    }
                                    //---六獎
                                    six1=myDict.get("sixthPrizeNo1");
                                    six2=myDict.get("sixthPrizeNo2");
                                    six3=myDict.get("sixthPrizeNo3");
                                    try{
                                        t_sixthPrizeNo.setText(six1);
                                        t_sixthPrizeNo2.setText(six2);
                                        t_sixthPrizeNo3.setText(six3);
                                    }catch (Exception e){
                                        t_sixthPrizeNo.setText("");
                                        t_sixthPrizeNo2.setText("");
                                        t_sixthPrizeNo3.setText("");
                                    }
                                }
                                else if(myDict.get("code").equals("901")){
                                    t_date.setText("無此期別資料");
                                }
                            }
                            else{
                                Log.d("---------return-------","did not work");
                            }
                        }catch (Exception e){
                            Log.d("catch exception  ",e.toString());
                        }
                    }
                }).start();
            }

        });
    }

    public void set_s1_s2(){
        s1=pick_year.getText().toString();
        s2=pick_month.getText().toString();
    }
}