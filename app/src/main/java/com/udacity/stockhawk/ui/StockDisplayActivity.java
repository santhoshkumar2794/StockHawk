package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by santhosh-3366 on 14/04/17.
 */

public class StockDisplayActivity extends AppCompatActivity {

    private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_display_activity);

        Intent intent = getIntent();
        String stockTitle = intent.getStringExtra("symbol");
        getSupportActionBar().setTitle(stockTitle);
        Cursor cursor = getContentResolver().query(Contract.Quote.URI,null, Contract.Quote.COLUMN_SYMBOL+ "=?",new String[]{stockTitle},null);
        cursor.moveToFirst();

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(cursor.getString(Contract.Quote.POSITION_SYMBOL));

        TextView lastPriceView = (TextView) findViewById(R.id.current_price);
        String lastPrice = "$"+cursor.getFloat(Contract.Quote.POSITION_PRICE);
        lastPriceView.setText(lastPrice);

        String strings = cursor.getString(Contract.Quote.POSITION_HISTORY);

        if (strings!=null && !strings.equals("")) {
            List<String> list = Arrays.asList(strings.split("\n"));
            ArrayList<Entry> values = new ArrayList<>();
            ArrayList<String> xValues = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                String value = list.get(i);
                String[] points = value.split(",");
                values.add(new Entry(i, Float.parseFloat(points[1])));
                String date = DateFormat.getInstance().format(Double.parseDouble(points[0]));
                xValues.add(date);
            }


            LineChart lineChart = (LineChart) findViewById(R.id.linechart);
            LineDataSet lineData = new LineDataSet(values, "DataSet1");
            lineData.setLineWidth(2);
            lineData.setColor(Color.RED);
            lineData.setDrawCircles(false);

            LineData data = new LineData(lineData);
            lineChart.setData(data);
            lineChart.animateX(2000);
            lineData.setDrawFilled(true);
            lineData.setFillColor(Color.WHITE);
            data.setValueTextColor(Color.WHITE);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setAxisLineColor(Color.WHITE);
            xAxis.setGridColor(Color.WHITE);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
        }
    }
}
