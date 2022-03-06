package com.example.androidproject.activities.dashboards.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import org.jetbrains.annotations.NotNull;

public abstract class BarChartViewHolder extends RecyclerView.ViewHolder{
    BarChart barChart;
    BarData barChartData;
    Context context;
    View itemView;
    //Istanzio il chart e lo personalizzo, la personalizzazione in effetti potrebbe essere custom per classi
    public BarChartViewHolder(@NonNull @NotNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        barChart = (BarChart)itemView.findViewById(R.id.BarChart);
        YAxis yAxis = barChart.getAxisLeft();
        XAxis xAxis = barChart.getXAxis();

        //Modifico le impostazioni globali del chart
        personalizzaChart();

        //Modifico un po il rendering del chart nell'asse X
        personalizzaAsseX(xAxis);

        //Modifico un po il rendering del chart nell'asse Y sinistro
        personalizzaAsseY(yAxis);

        //Disable Right Axis
        disableRightAxis(true);

        //Disable Left Axis
        disableLeftAxis(false);
    }

    protected void disableLeftAxis(boolean doDisable) {
        barChart.getAxisLeft().setEnabled(doDisable);
    }

    protected void disableRightAxis(boolean doDisable) {
        barChart.getAxisRight().setEnabled(doDisable);
    }

    protected void personalizzaChart() {
        barChart.setDrawGridBackground(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(true);
        barChart.setFitBars(true);
        barChart.setDefaultFocusHighlightEnabled(false);
        barChart.setHighlightFullBarEnabled(false);
    }

    protected void personalizzaAsseY(YAxis yAxis) {
        yAxis.setGranularity(0.5f);
        yAxis.setDrawGridLines(false);
        yAxis.setTextSize(12f);
        yAxis.setAxisLineWidth(3f);
        yAxis.setAxisMinimum(0f);
    }

    protected void personalizzaAsseX(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setAxisLineWidth(3f);
        xAxis.setAvoidFirstLastClipping(true);
    }


    //Lego i dati al chart
    public void setPosition(Object dashboardsDataModel){
        //Carica il dataset, questa dipende dalla classe specifica
        BarDataSet dataSet = loadDataSet(dashboardsDataModel);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        afterDatasetLoaded();

        //SetBarColors
        dataSet.setColor(Color.BLUE);


        barChartData.addDataSet(dataSet);
        barChartData.setBarWidth(0.9f);


        barChart.setData(barChartData);
        personalizzaDescrizione();

        barChart.invalidate();
    }

    protected abstract void afterDatasetLoaded();
    protected abstract BarDataSet loadDataSet(Object dashboardDataModel);
    protected abstract void setBarColors(BarDataSet dataSet);
    protected abstract void personalizzaDescrizione();

}