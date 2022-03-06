package com.example.androidproject.activities.valutazionistudente.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.activities.valutazionistudente.SelectionGesture.ObservableAlternativeSelectionRegistrar;
import com.example.androidproject.activities.valutazionistudente.SelectionGesture.AlternativeSelectionObserver;
import com.example.androidproject.R;
import com.example.androidproject.activities.valutazionistudente.ValutazioneStudente;

import org.jetbrains.annotations.NotNull;

public class IndicatoreValoreViewHolder extends RecyclerView.ViewHolder implements AlternativeSelectionObserver {
    private static int NULL_SELECTION = -1;
    Context context;
    TextView indicatoreNome;
    ValutazioneStudente.IndicatoriValori indicatoreValore;
    ViewGroup indicatoreContainer;
    TextView indicatoreValoreContainer;
    Button increaseIndicatore;
    Button decreaseIndicatore;
    IndicatoreModificatoListener cambiamentiListener1;
    IndicatoreModificatoListener cambiamentiListener2;
    View itemView;

    public IndicatoreValoreViewHolder(@NonNull @NotNull View itemView, Context context, IndicatoreModificatoListener listener) {
        super(itemView);
        //Listeners, si dovrebbe modificare un attimo questa cosa
        this.cambiamentiListener1 = listener;
        this.cambiamentiListener2 = (IndicatoreModificatoListener) context;
        //Cose
        this.context = context;
        //Aggiungo il drawable del border alla view
        this.itemView = itemView;

        indicatoreContainer = (ViewGroup) itemView.findViewById(R.id.IndicatoreContainerReadOnly);
        indicatoreContainer.setClickable(true);
        indicatoreContainer.setOnClickListener((View)->{
            ((ObservableAlternativeSelectionRegistrar) context).registerListener(this);
        });


        indicatoreNome = (TextView) itemView.findViewById(R.id.IndicatoreNome);
        indicatoreNome.setClickable(true);
        indicatoreNome.setOnClickListener((View)->{
            ((ObservableAlternativeSelectionRegistrar) context).registerListener(this);
        });
        indicatoreNome.setPaintFlags(indicatoreNome.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


        indicatoreValoreContainer = (TextView) itemView.findViewById(R.id.valoreIndicatoreReadOnly);
        increaseIndicatore = (Button) itemView.findViewById(R.id.increaseButton);
        decreaseIndicatore = (Button) itemView.findViewById(R.id.decreaseButton);

        increaseIndicatore.setOnClickListener((View)->{
            increaseValue();
        });

        decreaseIndicatore.setOnClickListener((View)->{
            decreaseValue();
        });
    }

    public void setIndicatoreValore(ValutazioneStudente.IndicatoriValori indicatoreValore){
        this.indicatoreValore = indicatoreValore;
        this.indicatoreNome.setText(indicatoreValore.getNomeIndicatore());
        if(indicatoreValore.getValore() == null){
            this.indicatoreValoreContainer.setText("NaN");
        }else{
            this.indicatoreValoreContainer.setText(String.valueOf(indicatoreValore.getValore()));
        }
    }

    @Override
    public void leftEvent() {
        decreaseValue();
    }

    @Override
    public void rightEvent() {
        increaseValue();
    }


    //Questi due metodi possono diventare un interfaccia che poi l'activity utilizza
    public void selectIndicatore() {
        Drawable border = ResourcesCompat.getDrawable(context.getResources(),R.drawable.view_border_red,null);
        itemView.findViewById(R.id.linearLayoutIndicatore).setBackground(border);
    }

    public void deselectIndicatore(){
        itemView.findViewById(R.id.linearLayoutIndicatore).setBackgroundColor(Color.argb(0,0,0,0));
    }

    public void refreshView(){
        itemView.invalidate();
    }


    //Questi due metodi magari poi li rivedo
    private void increaseValue(){
        if(indicatoreValore.getValore() < 10){
            indicatoreValore.setValore(indicatoreValore.getValore() + 1);
        }
        indicatoreValoreContainer.setText(String.valueOf(indicatoreValore.getValore()));
        cambiamentiListener1.indicatoreCambiato(indicatoreValore);
        cambiamentiListener2.indicatoreCambiato(indicatoreValore);
    }

    private void decreaseValue(){
        if(indicatoreValore.getValore() > 0) {
            indicatoreValore.setValore(indicatoreValore.getValore() - 1);
            indicatoreValoreContainer.setText(String.valueOf(indicatoreValore.getValore()));
            cambiamentiListener1.indicatoreCambiato(indicatoreValore);
            cambiamentiListener2.indicatoreCambiato(indicatoreValore);
        }
    }

}
