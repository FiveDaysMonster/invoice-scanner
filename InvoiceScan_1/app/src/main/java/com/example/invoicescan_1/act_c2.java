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
                        //???
                        ori=af_y.get(c.getInt(4)-1);
                        af_y.set(c.getInt(4)-1,ori+c.getInt(7));
                        //???
                        ori=ac_y.get(c.getInt(4)-1);
                        ac_y.set(c.getInt(4)-1,ori+c.getInt(8));
                        //???
                        ori=al_y.get(c.getInt(4)-1);
                        al_y.set(c.getInt(4)-1,ori+c.getInt(9));
                        //???
                        ori=ag_y.get(c.getInt(4)-1);
                        ag_y.set(c.getInt(4)-1,ori+c.getInt(10));
                        //???
                        ori=ae_y.get(c.getInt(4)-1);
                        ae_y.set(c.getInt(4)-1,ori+c.getInt(11));
                        //???
                        ori=afun_y.get(c.getInt(4)-1);
                        afun_y.set(c.getInt(4)-1,ori+c.getInt(12));
                        //??????
                        ori=ao_y.get(c.getInt(4)-1);
                        ao_y.set(c.getInt(4)-1,ori+c.getInt(13));
                    }
                    while(c.moveToNext()){
                        if(c.getString(6).equals(myDate)){
                            //total
                            total_amount+=c.getInt(5);
                            //???
                            ori=af_y.get(c.getInt(4)-1);
                            af_y.set(c.getInt(4)-1,ori+c.getInt(7));
                            //???
                            ori=ac_y.get(c.getInt(4)-1);
                            ac_y.set(c.getInt(4)-1,ori+c.getInt(8));
                            //???
                            ori=al_y.get(c.getInt(4)-1);
                            al_y.set(c.getInt(4)-1,ori+c.getInt(9));
                            //???
                            ori=ag_y.get(c.getInt(4)-1);
                            ag_y.set(c.getInt(4)-1,ori+c.getInt(10));
                            //???
                            ori=ae_y.get(c.getInt(4)-1);
                            ae_y.set(c.getInt(4)-1,ori+c.getInt(11));
                            //???
                            ori=afun_y.get(c.getInt(4)-1);
                            afun_y.set(c.getInt(4)-1,ori+c.getInt(12));
                            //??????
                            ori=ao_y.get(c.getInt(4)-1);
                            ao_y.set(c.getInt(4)-1,ori+c.getInt(13));
                        }
                    }
                }
                set_textView(DayLong);
                makeArrayList(DayLong);

                LineDataSet s1,s2,s3,s4,s5,s6,s7;
                s1=new LineDataSet(a_food,"???");
                s1.setColor(Color.rgb(255,0,0));//????????????
                s1.setCircleColor(Color.rgb(255,0,0));//????????????
                s1.setMode(LineDataSet.Mode.LINEAR);
                s1.setCircleRadius(2);//????????????
                s1.setDrawCircleHole(false);//?????????
                s1.setLineWidth(1.5f);//????????????
                s1.setValueTextSize(10f);//???????????????
                s1.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s1.setHighlightLineWidth(2f);//???????????????

                s2=new LineDataSet(a_clothes,"???");
                s2.setColor(Color.rgb(102,221,0));//????????????
                s2.setCircleColor(Color.rgb(102,221,0));//????????????
                s2.setMode(LineDataSet.Mode.LINEAR);
                s2.setCircleRadius(2);//????????????
                s2.setDrawCircleHole(false);//?????????
                s2.setLineWidth(1.5f);//????????????
                s2.setValueTextSize(10f);//???????????????
                s2.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s2.setHighlightLineWidth(2f);//???????????????

                s3=new LineDataSet(a_live,"???");
                s3.setColor(Color.rgb(0,187,255));//????????????
                s3.setCircleColor(Color.rgb(0,187,255));//????????????
                s3.setMode(LineDataSet.Mode.LINEAR);
                s3.setCircleRadius(2);//????????????
                s3.setDrawCircleHole(false);//?????????
                s3.setLineWidth(1.5f);//????????????
                s3.setValueTextSize(10f);//???????????????
                s3.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s3.setHighlightLineWidth(2f);//???????????????

                s4=new LineDataSet(a_go,"???");
                s4.setColor(Color.rgb(0,0,204));//????????????
                s4.setCircleColor(Color.rgb(0,0,204));//????????????
                s4.setMode(LineDataSet.Mode.LINEAR);
                s4.setCircleRadius(2);//????????????
                s4.setDrawCircleHole(false);//?????????
                s4.setLineWidth(1.5f);//????????????
                s4.setValueTextSize(10f);//???????????????
                s4.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s4.setHighlightLineWidth(2f);//???????????????

                s5=new LineDataSet(a_edu,"???");
                s5.setColor(Color.rgb(238,119,0));//????????????
                s5.setCircleColor(Color.rgb(238,119,0));//????????????
                s5.setMode(LineDataSet.Mode.LINEAR);
                s5.setCircleRadius(2);//????????????
                s5.setDrawCircleHole(false);//?????????
                s5.setLineWidth(1.5f);//????????????
                s5.setValueTextSize(10f);//???????????????
                s5.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s5.setHighlightLineWidth(2f);//???????????????

                s6=new LineDataSet(a_fun,"???");
                s6.setColor(Color.rgb(136,102,0));//????????????
                s6.setCircleColor(Color.rgb(136,102,0));//????????????
                s6.setMode(LineDataSet.Mode.LINEAR);
                s6.setCircleRadius(2);//????????????
                s6.setDrawCircleHole(false);//?????????
                s6.setLineWidth(1.5f);//????????????
                s6.setValueTextSize(10f);//???????????????
                s6.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s6.setHighlightLineWidth(2f);//???????????????

                s7=new LineDataSet(a_other,"??????");
                s7.setColor(Color.rgb(204,0,255));//????????????
                s7.setCircleColor(Color.rgb(204,0,255));//????????????
                s7.setMode(LineDataSet.Mode.LINEAR);
                s7.setCircleRadius(2);//????????????
                s7.setDrawCircleHole(false);//?????????
                s7.setLineWidth(1.5f);//????????????
                s7.setValueTextSize(10f);//???????????????
                s7.setValueFormatter(new DefaultValueFormatter(0));//????????????
                s7.setHighlightLineWidth(2f);//???????????????

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
                ss1=new BarDataSet(be1,"???");
                ss2=new BarDataSet(be2,"???");
                ss3=new BarDataSet(be3,"???");
                ss4=new BarDataSet(be4,"???");
                ss5=new BarDataSet(be5,"???");
                ss6=new BarDataSet(be6,"???");
                ss7=new BarDataSet(be7,"??????");
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

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x??????????????????
        xAxis.setTextColor(Color.BLUE);//x???????????????
        xAxis.setTextSize(12);//x???????????????
        xAxis.setGranularity(1);//x?????????
        xAxis.setAxisLineWidth(2);//x?????????
        xAxis.setAxisMaximum(m);//x???????????????
        xAxis.setAxisMinimum(1);//x???????????????

    }
    public void initY(){
        YAxis rightAxis=myLineChart.getAxisRight();
        YAxis leftAxis=myLineChart.getAxisLeft();

        rightAxis.setTextColor(Color.BLUE);//y???????????????
        rightAxis.setTextSize(12);//y???????????????
        rightAxis.setGranularity(1);//y?????????
        rightAxis.setAxisLineWidth(1);//y?????????
        rightAxis.setAxisMinimum(0);

        leftAxis.setTextColor(Color.BLUE);
        leftAxis.setTextSize(12);
        leftAxis.setGranularity(1);
        leftAxis.setAxisLineWidth(1);
        leftAxis.setAxisMinimum(0);
    }
    public void initChartFormat(){
        Description description=myLineChart.getDescription();
        description.setText("??????????????????????????????");

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
        t_total.setText("??????????????????"+String.valueOf(total_amount)+"???");
        t_food.setText("??????"+String.valueOf(total_food)+"???");
        t_clothes.setText("??????"+String.valueOf(total_clothes)+"???");
        t_live.setText("??????"+String.valueOf(total_live)+"???");
        t_go.setText("??????"+String.valueOf(total_go)+"???");
        t_edu.setText("??????"+String.valueOf(total_edu)+"???");
        t_fun.setText("??????"+String.valueOf(total_fun)+"???");
        t_other.setText("?????????"+String.valueOf(total_other)+"???");
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