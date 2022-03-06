package com.example.androidproject.activities.classe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidproject.R;
import com.example.androidproject.activities.dashboards.DashboardActivity;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.example.androidproject.LocalPersistenceModel.entities.Studente;
import com.example.androidproject.activities.classe.viewholders.StudenteViewHolder;
import com.example.androidproject.activities.valutazionistudente.ValutazioniStudenteActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;

/*
 * Questo è un componente assolutamente riusabile, si individua infatti una DataSource, una Recycler_view_element, ed un Adapter ed un EventHandler :D
 * Forse anche l'adapter è riusabile :D, una parte comunque è riutilizzabile, non tutto.
 *
 */
public class ClasseActivity extends AppCompatActivity implements StudentSelected {

    RecyclerView recyclerView;
    TextView nomeClasse;
    Button semesterButton;
    Button dashboardButton;
    Button schemaIndicatoriButton;
    int quadrimestreAttuale;
    long matricolaDocente;
    Classe classeAttuale;
    ListeningExecutorService dbTask = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    ProgressBar progressBar;

    int[] stupidArray = new int[]{1,2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classe);
        quadrimestreAttuale = getIntent().getIntExtra("quadrimestreSelezionato",0);
        classeAttuale = (Classe) getIntent().getSerializableExtra("classeSelezionata");
        matricolaDocente = getIntent().getLongExtra("matricolaDocente",0);
        nomeClasse = (TextView) findViewById(R.id.classText);
        semesterButton = (Button) findViewById(R.id.semesterButton);
        dashboardButton = (Button) findViewById(R.id.dashboardButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarClasse);

        setInfoHeader();

        semesterButton.setOnClickListener((view) -> {
            quadrimestreAttuale = ((quadrimestreAttuale + 1) % 2);
            setInfoHeader();
        });

        dashboardButton.setOnClickListener((view)-> startDashboardActivity());


        recyclerView = findViewById(R.id.studentRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);

        startProgressBarAnimation();
        ListenableFuture<List<Studente> > taskPrelievoDati = getStudentiClasse();

        Futures.addCallback(taskPrelievoDati, new FutureCallback<List<Studente>>() {
            @Override
            public void onSuccess(@Nullable List<Studente> result) {
                Adapter adapter = new Adapter(result,ClasseActivity.this);
                recyclerView.setAdapter(adapter);
                hideProgressBarAnimation();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                hideProgressBarAnimation();
                Log.i("myApp","Error loading data " + t.getMessage());
            }
        },getMainExecutor());
    }

    private ListenableFuture<List<Studente>> getStudentiClasse() {
        ListenableFuture<List<Studente>> sas = dbTask.submit(() -> {
            Webb webb = Webb.create();

            Response<InputStream> response = webb.post("http://192.168.1.63:8080/androidAppServer/studentiClasse")
                    .param("idClasse",classeAttuale.id)
                    .ensureSuccess()
                    .asStream();

            InputStream result = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Studente> studenti  = objectMapper.readValue(result, new TypeReference<List<Studente>>() {});
            return studenti;
        });
        return sas;
    }


    private void startDashboardActivity(){
        Intent activityStarter = new Intent(this, DashboardActivity.class);
        activityStarter.putExtra("classeSelezionata",classeAttuale);
        activityStarter.putExtra("quadrimestreSelezionato", quadrimestreAttuale);
        activityStarter.putExtra("matricolaDocente",matricolaDocente);
        startActivity(activityStarter);
    }

    public void studenteSelezionato(Studente studente,boolean valutazioniDocente) {
        Intent activityStarter = new Intent(this, ValutazioniStudenteActivity.class);
        activityStarter.putExtra("selectedStudent",studente);
        activityStarter.putExtra("classeSelezionata",classeAttuale);
        activityStarter.putExtra("quadrimestreSelezionato", quadrimestreAttuale);
        activityStarter.putExtra("matricolaDocente",matricolaDocente);
        activityStarter.putExtra("valutazioniDocente",valutazioniDocente);
        startActivity(activityStarter);
    }

    private void setInfoHeader() {
        nomeClasse.setText(classeAttuale.getNomeClasse() + " " + (quadrimestreAttuale + 1) + "°Q");
        semesterButton.setText(stupidArray[(quadrimestreAttuale + 1) % 2]+ "°Q");
    }

    private void startProgressBarAnimation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
    }

    private void hideProgressBarAnimation() {
        progressBar.setVisibility(View.GONE);
    }

    private class Adapter extends RecyclerView.Adapter<StudenteViewHolder> {
        List<Studente> studenti;
        public Adapter(List<Studente> studenti, Context context){
            this.studenti = studenti;
        }

        @NonNull
        @NotNull
        @Override
        public StudenteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_classe_singolostudente,parent,false);
            return new StudenteViewHolder(view,ClasseActivity.this);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull StudenteViewHolder holder, int position) {
            holder.setStudente(studenti.get(position));
        }

        @Override
        public int getItemCount() {
            return studenti.size();
        }

    }
}
