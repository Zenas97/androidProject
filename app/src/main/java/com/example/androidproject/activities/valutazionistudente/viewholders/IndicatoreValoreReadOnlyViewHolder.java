package com.example.androidproject.activities.valutazionistudente.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.activities.valutazionistudente.ValutazioneStudente;

import org.jetbrains.annotations.NotNull;


public class IndicatoreValoreReadOnlyViewHolder extends RecyclerView.ViewHolder {

    TextView indicatoreValore;
    TextView indicatoreNome;
    public IndicatoreValoreReadOnlyViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        indicatoreValore = itemView.findViewById(R.id.valoreIndicatoreReadOnly);
        indicatoreNome = itemView.findViewById(R.id.indicatoreReadOnly);
    }


    public void setTextIndicatore(ValutazioneStudente.IndicatoriValori indicatore){
        indicatoreValore.setText(indicatore.getValore().toString());
        indicatoreNome.setText(indicatore.getNomeIndicatore());
    }
}
