package com.example.invoicescan_1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class act_c2 extends AppCompatActivity {

    StdDBHelper dbHelper;
    private SQLiteDatabase db;
    String str_table_b2="table_b2";

    EditText e1,e2;
    Button b;
    TextView t_total,t_food,t_clothes,t_live,t_go,t_edu,t_fun,t_other;
    com.github.mikephil.charting.charts.LineChart myLineChart;
    com.github.mikephil.charting.charts.BarChart myBarChart;
    
    ArrayList a_food,a_clothes,a_live,a_go,a_edu,a_fun,a_other;
    ArrayList<Integer> a_x,af_y,ac_y,al_y,ag_y,ae_y,afun_y,ao_y,a_total;
    
    Integer total_amount=0,total_food=0,total_clothes=0,total_live=0,total_go=0,total_edu=0,total_fun=0,total_other=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_c2);

        e1=(EditText) findViewById(R.id.e1_act_c2);
        e2=(EditText) findViewById(R.id.e2_act_c2);
        b=(Button) findViewById(R.id.searchButton_act_c2);
        t_total=(TextView) findViewById(R.id.myTotalCost_act_c2);
        t_food=(TextView) findViewById(R.id.t1_act_c2);
        t_clothes=(TextView) findViewById(R.id.t2_act_c2);
        t_live=(TextView) findViewById(R.id.t3_act_c2);
        t_go=(TextView) findViewById(R.id.t4_act_c2);
        t_edu=(TextView) findViewById(R.id.t5_act_c2);
        t_fun=(TextView) findViewById(R.id.t6_act_c2);
        t_other=(TextView) findViewById(R.id.t7_act_c2);
        
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ArrayList a_food,a_clothes,a_live,a_go,a_edu,a_fun,a_other;
                ArrayList<Integer> a_x,af_y,ac_y,al_y,ag_y,ae_y,afun_y,ao_y;
                */
                myLineChart=(com.github.mikephil.charting.charts.LineChart) findViewById(R.id.lineChart_act_c2);
                String myYear=e1.getText().toString();
                String myMonth=e2.getText().toString();
                String myDate=myYear+myMonth;
                Integer DayLong=how_many_day(Integer.parseInt(myYear),Integer.parseInt(myMonth));
                initX(DayLong);
                initY();
                initChartFormat();
                a_food=new ArrayList();
                a_clothes=new ArrayList();
                a_live=new ArrayList();
                a_go=new ArrayList();
                a_edu=new ArrayList();
                a_fun=new ArrayList();
                a_other=new ArrayList();
                a_x=new ArrayList<Integer>();
                af_y=new ArrayList<Integer>();
                ac_y=new ArrayList<Integer>();
                al_y=new ArrayList<Integer>();
                ag_y=new ArrayList<Integer>();
                ae_y=new ArrayList<Integer>();
                afun_y=new ArrayList<Integer>();
                ao_y=new ArrayList<Integer>();
                a_total=new ArrayList<Integer>();
                reset_array(DayLong);
                dbHelper=new StdDBHelper(act_c2.this,1);
                db=dbHelper.getWritableDatabase();
                Cursor c=db.rawQuery("SELECT * FROM "+str_table_b2,null);
                Integer ori=0;
                total_amount=0;
                total_food=0;
                total_clothes=0;
                total_live=0;
                total_go=0;
                total_edu=0;
                total_fun=0;
                total_other=0;
                if(c.moveToFirst()){
                    if(c.getString(6).equals(myDate)){
                        total_amount+=c.getInt(5);
                        //食
                        ori=af_y.get(c.getInt(4)-1);
                        af_y.set(c.getInt(4)-1,ori+c.getInt(7));
                        //衣
                        ori=ac_y.get(c.getInt(4)-1);
                        ac_y.set(c.getInt(4)-1,ori+c.getInt(8));
                        //住
                        ori=al_y.get(c.getInt(4)-1);
                        al_y.set(c.getInt(4)-1,ori+c.getInt(9));
                        //行
                        ori=ag_y.get(c.getInt(4)-1);
                        ag_y.set(c.getInt(4)-1,ori+c.getInt(10));
                        //育
                        ori=ae_y.get(c.getInt(4)-1);
                        ae_y.set(c.getInt(4)-1,ori+c.getInt(11));
                        //樂
                        ori=afun_y.get(c.getInt(4)-1);
                        afun_y.set(c.getInt(4)-1,ori+c.getInt(12));
                        //其他
                        ori=ao_y.get(c.getInt(4)-1);
                        ao_y.set(c.getInt(4)-1,ori+c.getInt(13));
                    }
                    while(c.moveToNext()){
                        if(c.getString(6).equals(myDate)){
                            //total
                            total_amount+=c.getInt(5);
                            //食
                            ori=af_y.get(c.getInt(4)-1);
                            af_y.set(c.getInt(4)-1,ori+c.getInt(7));
                            //衣
                            ori=ac_y.get(c.getInt(4)-1);
                            ac_y.set(c.getInt(4)-1,ori+c.getInt(8));
                            //住
                            ori=al_y.get(c.getInt(4)-1);
                            al_y.set(c.getInt(4)-1,ori+c.getInt(9));
                            //行
                            ori=ag_y.get(c.getInt(4)-1);
                            ag_y.set(c.getInt(4)-1,ori+c.getInt(10));
                            //育
                            ori=ae_y.get(c.getInt(4)-1);
                            ae_y.set(c.getInt(4)-1,ori+c.getInt(11));
                            //樂
                            ori=afun_y.get(c.getInt(4)-1);
                            afun_y.set(c.getInt(4)-1,ori+c.getInt(12));
                            //其他
                            ori=ao_y.get(c.getInt(4)-1);
                            ao_y.set(c.getInt(4)-1,ori+c.getInt(13));
                        }
                    }
                }
                set_textView(DayLong);
                makeArrayList(DayLong);

                LineDataSet s1,s2,s3,s4,s5,s6,s7;
                s1=new LineDataSet(a_food,"食");
                s1.setColor(Color.rgb(255,0,0));//線的顏色
                s1.setCircleColor(Color.rgb(255,0,0));//圓點顏色
                s1.setMode(LineDataSet.Mode.LINEAR);
                s1.setCircleRadius(2);//圓點大小
                s1.setDrawCircleHole(false);//實心圓
                s1.setLineWidth(1.5f);//線的粗細
                s1.setValueTextSize(10f);//圓點字大小
                s1.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s1.setHighlightLineWidth(2f);//十字線寬度

                s2=new LineDataSet(a_clothes,"衣");
                s2.setColor(Color.rgb(102,221,0));//線的顏色
                s2.setCircleColor(Color.rgb(102,221,0));//圓點顏色
                s2.setMode(LineDataSet.Mode.LINEAR);
                s2.setCircleRadius(2);//圓點大小
                s2.setDrawCircleHole(false);//實心圓
                s2.setLineWidth(1.5f);//線的粗細
                s2.setValueTextSize(10f);//圓點字大小
                s2.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s2.setHighlightLineWidth(2f);//十字線寬度

                s3=new LineDataSet(a_live,"住");
                s3.setColor(Color.rgb(0,187,255));//線的顏色
                s3.setCircleColor(Color.rgb(0,187,255));//圓點顏色
                s3.setMode(LineDataSet.Mode.LINEAR);
                s3.setCircleRadius(2);//圓點大小
                s3.setDrawCircleHole(false);//實心圓
                s3.setLineWidth(1.5f);//線的粗細
                s3.setValueTextSize(10f);//圓點字大小
                s3.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s3.setHighlightLineWidth(2f);//十字線寬度

                s4=new LineDataSet(a_go,"行");
                s4.setColor(Color.rgb(0,0,204));//線的顏色
                s4.setCircleColor(Color.rgb(0,0,204));//圓點顏色
                s4.setMode(LineDataSet.Mode.LINEAR);
                s4.setCircleRadius(2);//圓點大小
                s4.setDrawCircleHole(false);//實心圓
                s4.setLineWidth(1.5f);//線的粗細
                s4.setValueTextSize(10f);//圓點字大小
                s4.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s4.setHighlightLineWidth(2f);//十字線寬度

                s5=new LineDataSet(a_edu,"育");
                s5.setColor(Color.rgb(238,119,0));//線的顏色
                s5.setCircleColor(Color.rgb(238,119,0));//圓點顏色
                s5.setMode(LineDataSet.Mode.LINEAR);
                s5.setCircleRadius(2);//圓點大小
                s5.setDrawCircleHole(false);//實心圓
                s5.setLineWidth(1.5f);//線的粗細
                s5.setValueTextSize(10f);//圓點字大小
                s5.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s5.setHighlightLineWidth(2f);//十字線寬度

                s6=new LineDataSet(a_fun,"樂");
                s6.setColor(Color.rgb(136,102,0));//線的顏色
                s6.setCircleColor(Color.rgb(136,102,0));//圓點顏色
                s6.setMode(LineDataSet.Mode.LINEAR);
                s6.setCircleRadius(2);//圓點大小
                s6.setDrawCircleHole(false);//實心圓
                s6.setLineWidth(1.5f);//線的粗細
                s6.setValueTextSize(10f);//圓點字大小
                s6.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s6.setHighlightLineWidth(2f);//十字線寬度

                s7=new LineDataSet(a_other,"其他");
                s7.setColor(Color.rgb(204,0,255));//線的顏色
                s7.setCircleColor(Color.rgb(204,0,255));//圓點顏色
                s7.setMode(LineDataSet.Mode.LINEAR);
                s7.setCircleRadius(2);//圓點大小
                s7.setDrawCircleHole(false);//實心圓
                s7.setLineWidth(1.5f);//線的粗細
                s7.setValueTextSize(10f);//圓點字大小
                s7.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                s7.setHighlightLineWidth(2f);//十字線寬度

                LineData lineData=new LineData(s1,s2,s3,s4,s5,s6,s7);
                myLineChart.setData(lineData);
                myLineChart.invalidate();

                //Barchart
                myBarChart=(com.github.mikephil.charting.charts.BarChart) findViewById(R.id.barChart_act_c2);
                myBarChart.animateX(1000, Easing.Linear);
                myBarChart.animateY(1000,Easing.Linear);

                XAxis xAxis2=myBarChart.getXAxis();
                xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

                ArrayList be1,be2,be3,be4,be5,be6,be7;
                be1=new ArrayList();
                be2=new ArrayList();
                be3=new ArrayList();
                be4=new ArrayList();
                be5=new ArrayList();
                be6=new ArrayList();
                be7=new ArrayList();
                be1.add(new BarEntry(0,total_food));
                be2.add(new BarEntry(1,total_clothes));
                be3.add(new BarEntry(2,total_live));
                be4.add(new BarEntry(3,total_go));
                be5.add(new BarEntry(4,total_edu));
                be6.add(new BarEntry(5,total_fun));
                be7.add(new BarEntry(6,total_other));
                BarDataSet ss1,ss2,ss3,ss4,ss5,ss6,ss7;
                ss1=new BarDataSet(be1,"食");
                ss2=new BarDataSet(be2,"衣");
                ss3=new BarDataSet(be3,"住");
                ss4=new BarDataSet(be4,"行");
                ss5=new BarDataSet(be5,"育");
                ss6=new BarDataSet(be6,"樂");
                ss7=new BarDataSet(be7,"其他");
                ss1.setColor(Color.rgb(255,0,0));
                ss2.setColor(Color.rgb(102,221,0));
                ss3.setColor(Color.rgb(0,187,255));
                ss4.setColor(Color.rgb(0,0,204));
                ss5.setColor(Color.rgb(238,119,0));
                ss6.setColor(Color.rgb(136,102,0));
                ss7.setColor(Color.rgb(204,0,255));
                ss1.setValueTextSize(10);
                ss2.setValueTextSize(10);
                ss3.setValueTextSize(10);
                ss4.setValueTextSize(10);
                ss5.setValueTextSize(10);
                ss6.setValueTextSize(10);
                ss7.setValueTextSize(10);
                ss1.setValueFormatter(new DefaultValueFormatter(0));
                ss2.setValueFormatter(new DefaultValueFormatter(0));
                ss3.setValueFormatter(new DefaultValueFormatter(0));
                ss4.setValueFormatter(new DefaultValueFormatter(0));
                ss5.setValueFormatter(new DefaultValueFormatter(0));
                ss6.setValueFormatter(new DefaultValueFormatter(0));
                ss7.setValueFormatter(new DefaultValueFormatter(0));
                BarData barData=new BarData(ss1,ss2,ss3,ss4,ss5,ss6,ss7);
                myBarChart.setData(barData);

                
            }
        });
    }
    public Integer how_many_day(Integer year,Integer month){
        year+=1911;
        if((((((month==1 || month==3)||month==5)||month==7)||month==8)||month==10)||month==12){
            return 31;
        }
        else if(month==2){
            if ((year%4==0 && year%100!=0)||year%400==0){
                return 29;
            }
            else{
                return 28;
            }
        }
        else{
            return 30;
        }
    }
    public void initX(Integer m){
        XAxis xAxis=myLineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x軸顯示在下方
        xAxis.setTextColor(Color.BLUE);//x軸文字顏色
        xAxis.setTextSize(12);//x軸文字大小
        xAxis.setGranularity(1);//x軸間隔
        xAxis.setAxisLineWidth(2);//x軸寬度
        xAxis.setAxisMaximum(m);//x軸最大座標
        xAxis.setAxisMinimum(1);//x軸最小座標

    }
    public void initY(){
        YAxis rightAxis=myLineChart.getAxisRight();
        YAxis leftAxis=myLineChart.getAxisLeft();

        rightAxis.setTextColor(Color.BLUE);//y軸文字顏色
        rightAxis.setTextSize(12);//y軸文字大小
        rightAxis.setGranularity(1);//y軸間隔
        rightAxis.setAxisLineWidth(1);//y軸寬度
        rightAxis.setAxisMinimum(0);

        leftAxis.setTextColor(Color.BLUE);
        leftAxis.setTextSize(12);
        leftAxis.setGranularity(1);
        leftAxis.setAxisLineWidth(1);
        leftAxis.setAxisMinimum(0);
    }
    public void initChartFormat(){
        Description description=myLineChart.getDescription();
        description.setText("食衣住行育樂整合分析");

        myLineChart.setNoDataText("no Data");
    }
    public void reset_array(Integer daylong){
        for(int i=0;i<daylong;i++){
            a_x.add(i+1);
            af_y.add(0);
            ac_y.add(0);
            al_y.add(0);
            ag_y.add(0);
            ae_y.add(0);
            afun_y.add(0);
            ao_y.add(0);
            a_total.add(0);
        }
    }
    public void set_textView(Integer daylong){
        for(int i=0;i<daylong;i++){
            total_food+=af_y.get(i);
            total_clothes+=ac_y.get(i);
            total_live+=al_y.get(i);
            total_go+=ag_y.get(i);
            total_edu+=ae_y.get(i);
            total_fun+=afun_y.get(i);
            total_other+=ao_y.get(i);
        }
        t_total.setText("本月總支出："+String.valueOf(total_amount)+"元");
        t_food.setText("食："+String.valueOf(total_food)+"元");
        t_clothes.setText("衣："+String.valueOf(total_clothes)+"元");
        t_live.setText("住："+String.valueOf(total_live)+"元");
        t_go.setText("行："+String.valueOf(total_go)+"元");
        t_edu.setText("育："+String.valueOf(total_edu)+"元");
        t_fun.setText("樂："+String.valueOf(total_fun)+"元");
        t_other.setText("其他："+String.valueOf(total_other)+"元");
    }
    public void makeArrayList(Integer daylong){
        for(int i=0;i<daylong;i++){
            a_food.add(new Entry(a_x.get(i),af_y.get(i)));
            a_clothes.add(new Entry(a_x.get(i),ac_y.get(i)));
            a_live.add(new Entry(a_x.get(i),al_y.get(i)));
            a_go.add(new Entry(a_x.get(i),ag_y.get(i)));
            a_edu.add(new Entry(a_x.get(i),ae_y.get(i)));
            a_fun.add(new Entry(a_x.get(i),afun_y.get(i)));
            a_other.add(new Entry(a_x.get(i),ao_y.get(i)));
        }
    }
    
}