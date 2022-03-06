package com.example.androidproject.activities.dashboards;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidproject.LocalPersistenceModel.entities.Studente;
import com.example.androidproject.activities.dashboards.viewholders.BarChartViewHolder;
import com.example.androidproject.activities.dashboards.viewholders.RadarChartMediaMaterieViewHolder;
import com.example.androidproject.activities.dashboards.viewholders.AndamentoComplessivoBarChartViewHolder;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.example.androidproject.R;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    ProgressBar progressBar;
    Classe classeAttuale;
    long matricolaDocente;
    int quadrimestreAttuale;


    ListeningExecutorService dbTask = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        classeAttuale = (Classe) getIntent().getSerializableExtra("classeSelezionata");
        quadrimestreAttuale = getIntent().getIntExtra("quadrimestreSelezionato",0);
        matricolaDocente = getIntent().getLongExtra("matricolaDocente",0);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDashboard);

        viewPager = (ViewPager2)findViewById(R.id.ViewPager);

        Log.i("myApp","Activity Dashboard creata");

        startProgressBarAnimation();
        ListenableFuture<Object[]> dashboardDataTask = getDashboardDataModel();
        Futures.addCallback(dashboardDataTask, new FutureCallback<Object[]>() {
            @Override
            public void onSuccess(@Nullable Object[] result) {
                Log.i("myApp","Adapter per il viewpager carico");
                /*
                ViewPagerAdapter adapter = new ViewPagerAdapter(result);
                viewPager.setAdapter(adapter);
                hideProgressBarAnimation();
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        if(position == 0){
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        }
                        else{
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                    }
                });
                */


            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                hideProgressBarAnimation();
                Log.i("myApp","Error loading dashboard data " + t.getMessage());
            }
        },getMainExecutor());
    }

    public void startProgressBarAnimation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
    }

    public void hideProgressBarAnimation() {
        progressBar.setVisibility(View.GONE);
    }

    private ListenableFuture<Object[]> getDashboardDataModel() {
            /*
            List<BarChartBinAndamentoClasse> istogrammaStudenti = db.valutazioneDAO().getIstogrammaMediaTotaleStudentiClasse(classeAttuale.getId(),matricolaDocente);
            List<MediaComplessivaPerMateria> radarMaterie = db.valutazioneDAO().getMediaComplessivaPerMateria(classeAttuale.getId(),matricolaDocente);
            return new Object[]{istogrammaStudenti,radarMaterie};
            */
            ListenableFuture<Object[]> sas = dbTask.submit(() -> {
                Webb webb = Webb.create();

                Response<InputStream> response = webb.post("http://192.168.1.63:8080/androidAppServer/mediaComplessivaStudentiClasse")
                        .param("idClasse",classeAttuale.getId())
                        .ensureSuccess()
                        .asStream();

                InputStream result = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String,Object>> medieComplessivePerStudente  = objectMapper.readValue(result, new TypeReference<List<Map<String,Object>>>() {});

                int[] count = new int[10];
                for(Map<String,Object> entry : medieComplessivePerStudente) {
                    Integer valore = ((Double) entry.get("mediaComplessiva")).intValue();

                    if(valore == 10) count[10] += 1;
                    else count[valore]+= 1;
                }

                List<BarChartBinAndamentoClasse> bins = new ArrayList<>();
                for(int i = 0; i < 10; i++){
                    Log.i("myApp", i + " " + String.valueOf(count[i]));
                    BarChartBinAndamentoClasse bin = new BarChartBinAndamentoClasse();
                    bin.setGruppoVoti(i);
                    bin.setConteggio(count[i]);
                    bin.setNormalizzato((float)count[i]/(float)medieComplessivePerStudente.size());
                    bins.add(bin);
                }
                Log.i("myApp",bins.toString());
                return new Object[]{bins};
            });
            return sas;
    }

    private class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Object[] dashboardsDataModel;

        public ViewPagerAdapter(Object[] dashboardsDataModel) {
            this.dashboardsDataModel = dashboardsDataModel;
            Log.i("myApp","Adapter created");
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View dashboardView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            if (viewType == R.layout.activity_dashboard_andamentoclasse) {
                return new AndamentoComplessivoBarChartViewHolder(dashboardView, parent.getContext());
            }else{
                return new RadarChartMediaMaterieViewHolder(dashboardView, parent.getContext());
            }
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                ((BarChartViewHolder) holder).setPosition(dashboardsDataModel[position]);
            } else {
                ((RadarChartMediaMaterieViewHolder)holder).setPosition(dashboardsDataModel[position]);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.activity_dashboard_andamentoclasse;
            } else{
                return R.layout.activity_dashboard_radarmaterie;
            }
        }
    }
}
