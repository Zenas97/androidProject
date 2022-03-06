package com.example.androidproject.activities.valutazionistudente.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.Formatter;

public class ValutazioneMediaViewHolder extends RecyclerView.ViewHolder {

    TextView mediaValutazioneTextView;
    public ValutazioneMediaViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mediaValutazioneTextView = (TextView) itemView.findViewById(R.id.MediaIndicatoriText);
    }

    public void setTextMediaValutazione(double mediaValutazione){
        Formatter formatter = new Formatter();
        formatter.format("%.2f",mediaValutazione);
        mediaValutazioneTextView.setText(formatter.toString());
    }
}
