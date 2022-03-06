package com.example.androidproject.activities.valutazionistudente.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.LocalPersistenceModel.entities.ValoreIndicatore;
import com.example.androidproject.activities.valutazionistudente.ValutazioneStudente;
import com.example.androidproject.R;

import org.jetbrains.annotations.NotNull;

/*
 * Questa classe decide come fare il display della singola valutazione e dei suoi indicatori,
 * questa particolare istanza fa in modo di deferire il display dei singoli indicatori ad un altra
 * recycler view.
 *
 * La modifica di questa classe implica come viene mfatto il display della singola valutazione, si potrebbero
 * ad esempio cambiare le direzioni
 */
public class ViewHolderValutazioneStudente extends RecyclerView.ViewHolder implements IndicatoreModificatoListener {
    TextView nomeMateria;
    ValutazioneStudente valutazione;
    Context context;
    boolean indicatoreModificabile;


    ValutazioniIndicatoriAdapter indicatoriAdapter;
    RecyclerView recyclerIndicatori;

    public ViewHolderValutazioneStudente(@NonNull @NotNull View itemView,boolean indicatoreModificabile) {
        super(itemView);
        this.indicatoreModificabile = indicatoreModificabile;
        nomeMateria = (TextView) itemView.findViewById(R.id.NomeMateriaTextView);
        //nomeDocente = (TextView) itemView.findViewById(R.id.NomeDocenteTextView);
        recyclerIndicatori = itemView.findViewById(R.id.IndicatoriRecycler);

    }

    public void setValutazione(ValutazioneStudente valutazione, Context context) {
        this.valutazione = valutazione;
        this.context = context;
        String materia = valutazione.getNomeInsegnamento();

        //String docente = valutazione.valutazioneDati.nomeDocente + " " + valutazione.valutazioneDati.cognomeDocente;

        nomeMateria.setText("Materia: " + materia);
       // nomeDocente.setText("Docente: " + docente);

        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.VERTICAL);

        indicatoriAdapter = new ValutazioniIndicatoriAdapter(context);

        recyclerIndicatori.setLayoutManager(lm);
        recyclerIndicatori.setAdapter(indicatoriAdapter);
        Log.i("myApp","Dati indicatori e valutazione settati");
    }

    @Override
    public void indicatoreCambiato(ValutazioneStudente.IndicatoriValori valoreIndicatore) {
        indicatoriAdapter.aggiornaMediaValutazione(_calcolaMediaValutazione());
    }


    private double _calcolaMediaValutazione(){
        double sum = 0;
        int n = 0;
        for(ValutazioneStudente.IndicatoriValori x : valutazione.indicatori){
            if(x.getValore() != null){
                sum+= x.getValore();
                n+=1;
            }
        }
        return sum/n;
    }


    private class ValutazioniIndicatoriAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ValutazioneMediaViewHolder viewHolderMediaValutazione;
        public ValutazioniIndicatoriAdapter(Context context){
        }

        @NonNull
        @Override
        public @NotNull RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent,false);
            if(viewType == R.layout.activity_valutazionestudenti_singoloindicatore){
                return new IndicatoreValoreViewHolder(view,context,ViewHolderValutazioneStudente.this);
            }else if(viewType == R.layout.activity_valutazionestudenti_mediavalutazione){
                this.viewHolderMediaValutazione = new ValutazioneMediaViewHolder(view);
                return viewHolderMediaValutazione;
            }else{
                return new IndicatoreValoreReadOnlyViewHolder(view);
            }
        }

        /*
         * Qui devo fare in modo che il view holder dell'indicatore prenda i dati giusti
         */
        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            Log.i("myApp","Binding indicatori happening");
            if(holder instanceof IndicatoreValoreViewHolder){
                ((IndicatoreValoreViewHolder)holder).setIndicatoreValore(valutazione.getIndicatori().get(position));
            }else if(holder instanceof ValutazioneMediaViewHolder){
                ((ValutazioneMediaViewHolder)holder).setTextMediaValutazione(_calcolaMediaValutazione());
            }else{
                ((IndicatoreValoreReadOnlyViewHolder)holder).setTextIndicatore(valutazione.getIndicatori().get(position));
            }
        }

        @Override
        public int getItemCount() {
            return valutazione.indicatori.size() + 1;
        }


        public void aggiornaMediaValutazione(double calcolaMediaValutazione) {
            viewHolderMediaValutazione.setTextMediaValutazione(_calcolaMediaValutazione());
        }

        @Override
        public int getItemViewType(int position) {
            if(position == valutazione.indicatori.size()){
                return R.layout.activity_valutazionestudenti_mediavalutazione;
            }else{
                if(indicatoreModificabile){
                    return R.layout.activity_valutazionestudenti_singoloindicatore;
                }else{
                    return R.layout.activity_valutazionestudenti_singoloindicatore_nonmodificabile;
                }
            }
        }
    }
}
