package com.example.androidproject.activities.dashboards.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidproject.activities.dashboards.BarChartBinAndamentoClasse;
import com.example.androidproject.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class AndamentoComplessivoBarChartViewHolder extends BarChartViewHolder{
    double mediaComplessiva;
    double varianzaComplessiva;

    public AndamentoComplessivoBarChartViewHolder(@NonNull @NotNull View itemView, Context context) {
        super(itemView, context);
    }

    @Override
    protected BarDataSet loadDataSet(Object dashboardsDataModel) {
        barChartData = new BarData();
        List<BarChartBinAndamentoClasse> istogrammaStudenti = (List<BarChartBinAndamentoClasse>) dashboardsDataModel;

        barChart.getXAxis().setLabelCount(istogrammaStudenti.size(),
                false);


        List<BarEntry> dataSetEntry = new ArrayList<>();

        this.mediaComplessiva = 0;
        for(BarChartBinAndamentoClasse x : istogrammaStudenti){
            this.mediaComplessiva += x.GruppoVoti * (x.Normalizzato);
        }
        Log.i("myApp","Media calcolata : " + this.mediaComplessiva);

        //Calcola varianza;
        this.varianzaComplessiva = 0;
        for(BarChartBinAndamentoClasse x : istogrammaStudenti){
            this.varianzaComplessiva += Math.pow(x.GruppoVoti - this.mediaComplessiva,2) * x.Normalizzato;
        }
        this.varianzaComplessiva = Math.sqrt(this.varianzaComplessiva);
        Log.i("myApp","STDEV calcolata : " + this.varianzaComplessiva);

        float counter = 0;
        String[] labels = new String[istogrammaStudenti.size()];
        for(BarChartBinAndamentoClasse binIstogramma: istogrammaStudenti){
            float actual = counter++;
            dataSetEntry.add(new BarEntry(actual,binIstogramma.Normalizzato));
            labels[(int) actual] = "("+(int)actual + "-"+((int)actual+1) +")";
        }
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setTextSize(8f);

        BarDataSet dataset = new BarDataSet(dataSetEntry,"Percentuale range voto");
        dataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                NumberFormat formatter= NumberFormat.getPercentInstance();
                formatter.setMinimumFractionDigits(1);
                return formatter.format(barEntry.getY());
            }
        });

        return dataset;
    }

    @Override
    protected void setBarColors(BarDataSet dataSet) {
        dataSet.setColor(Color.BLUE);
    }

    @Override
    protected void afterDatasetLoaded() {
        Formatter formatter = new Formatter();
        String media = formatter.format("%.2f",this.mediaComplessiva).toString();
        formatter.close();
        formatter = new Formatter();
        String stdDev = formatter.format("%.2f",this.varianzaComplessiva).toString();
        ((TextView)itemView.findViewById(R.id.barChartTextView)).setText("Andamento voti " + "media "
        + media + " stdev: " + stdDev);
    }

    @Override
    protected void personalizzaDescrizione() {
        barChart.getDescription().setEnabled(false);
    }
}
