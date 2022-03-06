package com.example.androidproject.activities.dashboards.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.activities.dashboards.MediaComplessivaPerMateria;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RadarChartMediaMaterieViewHolder extends RecyclerView.ViewHolder {
    RadarChart radarChart;
    RadarData radarChartData;
    Context context;

    public RadarChartMediaMaterieViewHolder(@NonNull @NotNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        radarChart = itemView.findViewById(R.id.RadarChart);

        radarChart.setDrawWeb(true);
        radarChart.setWebColor(Color.BLUE);
        radarChart.getYAxis().setAxisMinimum(0f);
        radarChart.getYAxis().setAxisMaximum(10f);
        radarChart.getYAxis().setEnabled(false);
        radarChart.setScaleY(1.2f);
        radarChart.setScaleX(1.2f);
        radarChart.getDescription().setEnabled(false);
    }


    public void setPosition(Object dashboardDataModel){
        radarChartData= new RadarData();
        RadarDataSet dataSet = loadDataSet(dashboardDataModel);
        dataSet.setColor(Color.RED);
        dataSet.setFillColor(Color.RED);


        radarChartData.addDataSet(dataSet);
        radarChart.setData(radarChartData);
        radarChart.invalidate();
    }

    private RadarDataSet loadDataSet(Object dashboardDataModel){
        List<RadarEntry> dataSetEntry = new ArrayList<>();

        List<MediaComplessivaPerMateria> medieComplessiveMaterie = (List<MediaComplessivaPerMateria>) dashboardDataModel;
        String[] nameLabels = new String[medieComplessiveMaterie.size()];
        Log.i("myApp","size materie" + medieComplessiveMaterie.size());
        float counter = 0;
        for(MediaComplessivaPerMateria mediaMateria: medieComplessiveMaterie){
            float actual = counter++;
            dataSetEntry.add(new RadarEntry((float)mediaMateria.media,actual));
            nameLabels[(int)actual] = mediaMateria.nomeInsegnamento;
        }
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(nameLabels));
        return new RadarDataSet(dataSetEntry,"Media Materia");
    }
}
