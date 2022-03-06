package com.example.androidproject.activities.valutazionistudente;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.LocalPersistenceModel.entities.ValoreIndicatore;
import com.example.androidproject.activities.mainactivity.ExportModel;
import com.example.androidproject.activities.valutazionistudente.SelectionGesture.MotionGestureAlternativeSelection;
import com.example.androidproject.activities.valutazionistudente.SelectionGesture.AlternativeSelectionObserver;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.example.androidproject.LocalPersistenceModel.entities.Studente;
import com.example.androidproject.R;
import com.example.androidproject.activities.valutazionistudente.SelectionGesture.ObservableAlternativeSelectionRegistrar;
import com.example.androidproject.activities.valutazionistudente.viewholders.IndicatoreModificatoListener;
import com.example.androidproject.activities.valutazionistudente.viewholders.IndicatoreValoreViewHolder;
import com.example.androidproject.activities.valutazionistudente.viewholders.ViewHolderValutazioneStudente;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goebl.david.Request;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ValutazioniStudenteActivity extends AppCompatActivity implements ObservableAlternativeSelectionRegistrar, IndicatoreModificatoListener {

    Studente studente;
    int quadrimestreAttuale;
    long matricolaDocente;
    boolean valutazioniDocente;
    Classe classeAttuale;


    RecyclerView myRecycler;
    TextView studenteInfo;
    TextView mediaCalcolataText;
    MenuItem uploadActionItem;
    MenuItem refreshActionItem;
    MotionGestureAlternativeSelection motionGestureAlternativeSelectionService;
    ListeningExecutorService dbTask = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    ProgressBar progressBar;
    JSONObject indicatoriModificati = new JSONObject();
    List<ValutazioneStudente> valutazioniStudente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inizializza Layout e Dati iniziali
        setContentView(R.layout.activity_valutazionestudenti);

        studente = (Studente) getIntent().getSerializableExtra("selectedStudent");

        classeAttuale = (Classe) getIntent().getSerializableExtra("classeSelezionata");

        quadrimestreAttuale = getIntent().getIntExtra("quadrimestreSelezionato", 0);

        matricolaDocente = getIntent().getLongExtra("matricolaDocente", 0);

        valutazioniDocente = getIntent().getBooleanExtra("valutazioniDocente",false);



        Toolbar toolbar = findViewById(R.id.toolbarValutazioni);
        setSupportActionBar(toolbar);



        try {
            motionGestureAlternativeSelectionService = new MotionGestureAlternativeSelection(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBarValutazioniStudente);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        myRecycler = findViewById(R.id.recyclerValutazioni);
        studenteInfo = (TextView) findViewById(R.id.studenteInfoValutazioni);
        mediaCalcolataText = (TextView) findViewById(R.id.MediaTotaleCalcolataText);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        myRecycler.setLayoutManager(lm);

        studenteInfo.setText(classeAttuale.getNomeClasse() + " " + studente.getNome() + " " + studente.getCognome() + " " + (quadrimestreAttuale + 1) + "°Q");

        caricaValutazioniStudenti((error)->{
            Toast.makeText(this,"Errore caricamento dati",Toast.LENGTH_LONG).show();
        },null);

    }

    private void caricaValutazioniStudenti(Consumer<Throwable> failureEvent,Consumer<Void> afterSuccessEvent) {
        startProgressBarAnimation();
        ListenableFuture<List<ValutazioneStudente>> taskPrelievoDati = getValutazioniStudente();
        Futures.addCallback(taskPrelievoDati,
                new FutureCallback<List<ValutazioneStudente>>() {
                    @Override
                    public void onSuccess(@Nullable List<ValutazioneStudente> result) {
                        valutazioniStudente = result;
                        Log.i("myApp", "Success " + result.size());
                        ValutazioniStudenteAdapter valutazioniAdapter = new ValutazioniStudenteAdapter(result, ValutazioniStudenteActivity.this);
                        myRecycler.setAdapter(valutazioniAdapter);
                        valutazioniAdapter.notifyDataSetChanged();
                        setMediaCalcolataText();
                        hideProgressBarAnimation();
                        enableActionsMenu(true);
                        if(afterSuccessEvent != null) afterSuccessEvent.accept(null);

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("myApp", "Some error " + t.getMessage());
                        hideProgressBarAnimation();
                        t.printStackTrace();
                        enableActionsMenu(true);
                        if(failureEvent != null) failureEvent.accept(t);
                    }
                }, getMainExecutor());
    }

    protected void enableActionsMenu(boolean b) {
        refreshActionItem.setEnabled(b);
        uploadActionItem.setEnabled(b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.valutazioni_menu,menu);
        refreshActionItem = menu.findItem(R.id.refreshAction);
        uploadActionItem = menu.findItem(R.id.uploadAction);
        if(valutazioniDocente == false){
            uploadActionItem.setVisible(false);
            uploadActionItem.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uploadAction:
                enableActionsMenu(false);
                ListenableFuture<Boolean> uploadTask  = uploadValutazioni();
                Futures.addCallback(uploadTask, new FutureCallback<Boolean>() {
                    @Override
                    public void onSuccess(@Nullable Boolean result) {
                        hideProgressBarAnimation();
                        Toast.makeText(ValutazioniStudenteActivity.this,"Upload valutazioni successo",Toast.LENGTH_SHORT).show();
                        enableActionsMenu(true);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        hideProgressBarAnimation();
                        Toast.makeText(ValutazioniStudenteActivity.this,"Errore upload valutazioni " + t.getMessage(),Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        enableActionsMenu(false);

                    }
                },getMainExecutor());
                return true;
            case R.id.refreshAction:
                enableActionsMenu(false);
                caricaValutazioniStudenti((error)->{
                    Toast.makeText(this,"Error refresh dati " + error.getMessage(),Toast.LENGTH_LONG).show();
                },(success)->{
                    Toast.makeText(this,"Successo refresh",Toast.LENGTH_LONG).show();
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ListenableFuture<Boolean> uploadValutazioni() {
        startProgressBarAnimation();
        ListenableFuture<Boolean> sas = dbTask.submit(() -> {
            Webb webb = Webb.create();

            Response<InputStream> response = webb.post("http://192.168.1.63:8080/androidAppServer/updateValutazioni")
                    .body(indicatoriModificati)
                    .ensureSuccess()
                    .asStream();

            InputStream result = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            Boolean r = objectMapper.readValue(result,Boolean.class);
            return r;
        });
        return sas;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(motionGestureAlternativeSelectionService != null){
            motionGestureAlternativeSelectionService.closeSensor();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(motionGestureAlternativeSelectionService != null){
            motionGestureAlternativeSelectionService.activateSensor();
        }
    }

    /*
     * Questa deve essere calcolata con i dati locali
     */
    private void setMediaCalcolataText(){
        double sum = 0;
        for(ValutazioneStudente valutazioneStudente : valutazioniStudente){
            double temp_sum = 0;
            List<ValutazioneStudente.IndicatoriValori> indicatori = valutazioneStudente.indicatori;
            for(ValutazioneStudente.IndicatoriValori indicatoriValutazione : indicatori){
                temp_sum+= indicatoriValutazione.valore;
            }
            temp_sum/= indicatori.size();
            sum+= temp_sum;
        }
        Double mediaTotale = sum/valutazioniStudente.size();
        mediaCalcolataText.setText("Media Complessiva : "+ mediaTotale.toString());
    }

    @Override
    public void registerListener(AlternativeSelectionObserver listener) {
        Log.i("myApp","Registering a new listener for shake protocol");
        AlternativeSelectionObserver previousListener = motionGestureAlternativeSelectionService.getListener();
        if(previousListener == listener){
            ((IndicatoreValoreViewHolder)previousListener).deselectIndicatore();
            motionGestureAlternativeSelectionService.unregisterListener();
        }
        else{
            if(previousListener != null){
                ((IndicatoreValoreViewHolder)previousListener).deselectIndicatore();
            }
            ((IndicatoreValoreViewHolder)listener).selectIndicatore();
            motionGestureAlternativeSelectionService.registerListener(listener);
        }
    }

    /*
     *  Richiede in maniera asincrona le valutazioni dello studente
     */
    private ListenableFuture<List<ValutazioneStudente>> getValutazioniStudente() {
        ListenableFuture<List<ValutazioneStudente>> sas = dbTask.submit(() -> {
                Log.i("myApp","Prelievo dati dal web " + "matricola : " + studente.getMatricola() + " quad : " + quadrimestreAttuale);
                Webb webb = Webb.create();

                Request request = webb.post("http://192.168.1.63:8080/androidAppServer/valutazioniStudente")
                        .param("idStudente",studente.getMatricola())
                        .param("quadrimestre",quadrimestreAttuale)
                        .ensureSuccess();

                if(valutazioniDocente){
                    request.param("idDocente",matricolaDocente);
                }

                Response<InputStream> response = request.asStream();

                InputStream result = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                List<ValutazioneStudente> valutazioni  = objectMapper.readValue(result, new TypeReference<List<ValutazioneStudente>>() {});
                Log.i("myApp",valutazioni.toString());
                return valutazioni;
        });
        return sas;
    }

    public void startProgressBarAnimation() {
        progressBar.setVisibility(View.VISIBLE);

        progressBar.bringToFront();
    }

    public void hideProgressBarAnimation() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void indicatoreCambiato(ValutazioneStudente.IndicatoriValori indicatoriValore) {
        try{
            Log.i("myApp","Saving indicatore");
            indicatoriModificati.put(indicatoriValore.getIdIndicatoreValore().toString(),indicatoriValore.getValore());
            Log.i("myApp",indicatoriModificati.toString());
            setMediaCalcolataText();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Error : " + e.getMessage(),Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }


    private class ValutazioniStudenteAdapter extends RecyclerView.Adapter<ViewHolderValutazioneStudente> {
        Context context;
        List<ValutazioneStudente> valutazioni = new ArrayList<>();
        double mediaTotaleValutazioni;

        public ValutazioniStudenteAdapter(List<ValutazioneStudente> valutazioni, Context context){
            this.valutazioni = valutazioni;
            this.context = context;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolderValutazioneStudente onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_valutazionestudenti_singolavalutazione,parent,false);
            return new ViewHolderValutazioneStudente(view,valutazioniDocente);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolderValutazioneStudente holder, int position) {
            holder.setValutazione(valutazioni.get(position),context);
        }

        @Override
        public int getItemCount() {
            return valutazioni.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }
}
