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

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;

public class act_c1 extends AppCompatActivity {

    StdDBHelper dbHelper;
    private SQLiteDatabase db;
    String str_table_b2="table_b2";

    EditText e1,e2;
    Button b;
    TextView t_totalAmount;

    com.github.mikephil.charting.charts.LineChart myLineChart;
    ArrayList<Integer> arrayList1,arrayList2,end_arrayList1,end_arrayList2;
    ArrayList arrayList;
    Integer total_amount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_c1);

        e1=(EditText) findViewById(R.id.e1_act_c1);
        e2=(EditText) findViewById(R.id.e2_act_c1);
        b=(Button) findViewById(R.id.searchButton_act_c1);
        t_totalAmount=(TextView) findViewById(R.id.myTotalCost_act_c1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList=new ArrayList();
                arrayList1=new ArrayList<Integer>();
                arrayList2=new ArrayList<Integer>();
                myLineChart=(com.github.mikephil.charting.charts.LineChart) findViewById(R.id.lineChart_act_c1);

                initY();
                initChartFormat();

                //end_arrayList1=new ArrayList();
                //end_arrayList2=new ArrayList();
                String myYear=e1.getText().toString();
                String myMonth=e2.getText().toString();
                String myDate=myYear+myMonth;
                Integer DayLong=how_many_day(Integer.parseInt(myYear),Integer.parseInt(myMonth));
                initX(DayLong);
                reset_xy(DayLong);

                dbHelper=new StdDBHelper(act_c1.this,1);
                db=dbHelper.getWritableDatabase();
                Cursor c=db.rawQuery("SELECT * FROM "+str_table_b2,null);
                if(c.moveToFirst()){
                    if(c.getString(6).equals(myDate)){
                        Integer ori=arrayList2.get(c.getInt(4)-1);
                        arrayList2.set(c.getInt(4)-1,ori+c.getInt(5));
                    }
                    while(c.moveToNext()){
                        if(c.getString(6).equals(myDate)){
                            Integer ori=arrayList2.get(c.getInt(4)-1);
                            arrayList2.set(c.getInt(4)-1,ori+c.getInt(5));
                        }
                    }
                }
                set_totalAmount(DayLong);
                t_totalAmount.setText("本月總支出："+String.valueOf(total_amount)+"元");
                makeArrayList(DayLong);
                LineDataSet set1;
                set1=new LineDataSet(arrayList,"單日總支出");
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setColor(Color.rgb(255,0,0));//線的顏色
                set1.setCircleRadius(2);//圓點大小
                set1.setDrawCircleHole(false);//實心圓
                set1.setCircleColor(Color.rgb(255,0,0));//圓點顏色
                set1.setLineWidth(1.5f);//線的粗細
                set1.setValueTextSize(10f);//圓點字大小
                set1.setValueFormatter(new DefaultValueFormatter(0));//小數位數
                set1.setHighlightLineWidth(2f);//十字線寬度

                LineData lineData=new LineData(set1);
                myLineChart.setData(lineData);
                myLineChart.invalidate();


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
    public void reset_xy(Integer daylong){
        for(int i=0;i<daylong;i++){
            arrayList1.add(i+1);
            arrayList2.add(0);
        }
    }
    public void makeArrayList(Integer daylong){
        for(int i=0;i<daylong;i++){
            arrayList.add(new Entry(arrayList1.get(i),arrayList2.get(i)));
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
        description.setText("單月總支出折線圖");

        myLineChart.setNoDataText("no Data");
    }
    public void set_totalAmount(Integer daylong){
        total_amount=0;
        for(int i=0;i<daylong;i++){
            total_amount+=arrayList2.get(i);
        }
    }
}