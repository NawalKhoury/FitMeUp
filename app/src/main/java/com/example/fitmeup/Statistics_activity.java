package com.example.fitmeup;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Statistics_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize the LineChart
        LineChart lineChart = findViewById(R.id.lineChart);

        // Create a list of entries for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 1f));
        entries.add(new Entry(1f, 2f));
        entries.add(new Entry(2f, 3f));
        entries.add(new Entry(3f, 4f));
        entries.add(new Entry(4f, 3f));
        entries.add(new Entry(5f, 2f));
        entries.add(new Entry(6f, 1f));

        // Create a LineDataSet with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Statistics Data");
        dataSet.setColor(getResources().getColor(R.color.purple_500)); // Set line color
        dataSet.setValueTextColor(getResources().getColor(R.color.black)); // Set value text color

        // Create LineData with the LineDataSet
        LineData lineData = new LineData(dataSet);

        // Set the data for the chart
        lineChart.setData(lineData);

        // Refresh the chart to display the data
        lineChart.invalidate();
    }
}
