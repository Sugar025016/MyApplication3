package com.example.doll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.doll.util.LineChartDataUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class LineChartData extends AppCompatActivity {
    LineChartDataUtil lineChartDataUtil;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_data);


        lineChart = findViewById(R.id.lineChart);
        lineChartDataUtil = new LineChartDataUtil(lineChart,this);

        for(int i = 0;i<10;i++){
            xData.add("第" + i + "筆");
            yData.add(new Entry(i-1, i));
        }
        lineChartDataUtil.initX(xData);
        lineChartDataUtil.initY(0F,10F);
        lineChartDataUtil.initDataSet(yData);
    }
}