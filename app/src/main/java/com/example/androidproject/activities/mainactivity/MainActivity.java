package com.example.androidproject.activities.mainactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.LocalPersistenceModel.entities.Indicatore;
import com.example.androidproject.LocalPersistenceModel.entities.Insegnamento;
import com.example.androidproject.LocalPersistenceModel.entities.Studente;
import com.example.androidproject.LocalPersistenceModel.entities.ValoreIndicatore;
import com.example.androidproject.LocalPersistenceModel.entities.Valutazione;
import com.example.androidproject.activities.classe.ClasseActivity;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.example.androidproject.R;
import com.example.androidproject.activities.mainactivity.dialogs.ClassPositionSelectionDialog;
import com.example.androidproject.activities.mainactivity.dialogs.DialogImportDataConfirmation;
import com.example.androidproject.activities.mainactivity.dialogs.LocalizationConflictResolutionDialog;
import com.example.androidproject.activities.mainactivity.viewholders.ClassiViewHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ClassSelected {

    RecyclerView recyclerView;
    Button bottoneLocalizzazione;
    ProgressBar progressBar;
    FusedLocationProviderClient fusedLocationClient;
    ListeningExecutorService dbTask = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    long matricolaDocente;
    boolean filePickingAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);


        fusedLocationClient = new FusedLocationProviderClient(this);
        matricolaDocente = getIntent().getLongExtra("matricolaDocente",-1);
        progressBar = findViewById(R.id.progressBarSelezioneClassi);

        bottoneLocalizzazione = (Button) findViewById(R.id.LocationRequestButton);
        bottoneLocalizzazione.setOnClickListener((view) -> selectClassByLocation());

        if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                && getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }


        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);

        startProgressBarAnimation();
        ListenableFuture<List<Classe>> taskPrelievoDati = getClassiDocente();

        Futures.addCallback(taskPrelievoDati, new FutureCallback<List<Classe>>() {
            @Override
            public void onSuccess(@Nullable List<Classe> result) {
                Adapter adapter = new Adapter(result);
                recyclerView.setAdapter(adapter);
                hideProgressBarAnimation();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                hideProgressBarAnimation();
                Log.i("myApp", "Errore prelievo classi docente " + t.getMessage());
            }
        }, getMainExecutor());
    }

    private ListenableFuture<List<Classe>> getClassiDocente() {
        ListenableFuture<List<Classe>> sas = dbTask.submit(() -> {
            Webb webb = Webb.create();

            Response<InputStream> response = webb.post("http://192.168.1.63:8080/androidAppServer/classiDocente")
                    .param("idDocente",matricolaDocente)
                    .ensureSuccess()
                    .asStream();

            InputStream result = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Classe> classi  = objectMapper.readValue(result, new TypeReference<List<Classe>>() {});
            return classi;
        });
        return sas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_option_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.importMenuChoice){
            new DialogImportDataConfirmation().show(getSupportFragmentManager(),"Confirm import");
            return true;
        }

        if(item.getItemId() == R.id.exportMenuChoice){
            filePickerAction("Export data option selected", Intent.ACTION_CREATE_DOCUMENT, 1);
            return true;
        }
        return false;
    }

    public void filePickerAction(String s, String actionOpenDocument, int i) {
        Log.i("myApp", s);
        Intent intent = new Intent(actionOpenDocument);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        startActivityForResult(intent, i);
    }



    public void openChangeClassLocationDialog(Classe classe) {
        Log.i("myApp", "Open Dialog change location");
        DialogFragment classPositionSelectionDialog = new ClassPositionSelectionDialog();
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putSerializable("classe", (Serializable) classe);
        classPositionSelectionDialog.setArguments(argumentsBundle);
        classPositionSelectionDialog.show(getSupportFragmentManager(), "classPositionSelectionRequest");

    }

    public void selectClassByLocation() {
        try {
            startProgressBarAnimation();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Task<LocationSettingsResponse> task = classLocalizationRequest();

            //Se il task è null è perchè c'è stata una richiesta di permessi
            if(task == null){
                throw new Exception("Permessi non garantiti");
            }

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i("myApp", "Posso fare la richiesta");
                    proceedLocatingClass(matricolaDocente);
                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    hideProgressBarAnimation();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.i("myApp", "Non e stato possibile fare la richiesta per qualche ragione " + e.getMessage());
                    Toast.makeText(MainActivity.this,"Errore localizzazione classe : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            hideProgressBarAnimation();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            e.printStackTrace();
        }
    }

    //Potrei legare i request code ai metodi, oppure no
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.i("myApp", "Permission for location granted");
        } else {
            Log.i("myApp", "Permission for location not granted for some reason");
        }
    }

    public void classSelect(Classe classe, int quadrimestre) {
        Log.i("myApp", "Class selected " + classe.getId() + " semester " + quadrimestre);
        Intent activityStarter = new Intent(this, ClasseActivity.class);
        activityStarter.putExtra("classeSelezionata", classe);
        activityStarter.putExtra("quadrimestreSelezionato", quadrimestre);
        activityStarter.putExtra("matricolaDocente",matricolaDocente);
        startActivity(activityStarter);
    }

    /*
     *  Gestisce la risposta del servizio di localizzazione
     */
    public void localizationResponse(List<Classe> classi) {
        hideProgressBarAnimation();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Log.i("myApp", "Flags cleared");

        if (classi != null) {
            Log.i("myApp", "Richiesta posizione terminata " + classi.size());
            //In verità dovrebbe selezionarmi il semestre attuale
            if (classi.size() == 1) {
                classSelect(classi.get(0), 0);
            }
            //Apri Dialog per la risoluzione del conflitto
            else if (classi.size() > 1) {
                DialogFragment conflictResolutionDialog = new LocalizationConflictResolutionDialog();
                Bundle argumentsBundle = new Bundle();
                argumentsBundle.putSerializable("classi", (Serializable) classi);
                conflictResolutionDialog.setArguments(argumentsBundle);
                conflictResolutionDialog.show(getSupportFragmentManager(), "conflictResolutionRequest");
            } else {
                Toast.makeText(getApplicationContext(), "Nessuna classe localizzata", Toast.LENGTH_SHORT).show();
                Log.i("myApp", "Errore : non è stato possibile fare la localizzazione");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Errore servizio di localizzazione", Toast.LENGTH_SHORT).show();
        }
    }


    public void startProgressBarAnimation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
    }

    public void hideProgressBarAnimation() {
        progressBar.setVisibility(View.GONE);
    }


    /*
     * Viene creata la richiesta di location e validata, serve a capire se si può fare
     */
    public Task<LocationSettingsResponse> classLocalizationRequest() {
        Log.i("myApp", "Request position start");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return null;
        }

        LocationRequest req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setExpirationTime(3000)
                .setExpirationDuration(3000)
                .setInterval(500);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(req);


        Task<LocationSettingsResponse> taskRequest = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        return taskRequest;
    }


    public void changeClassPositionByLocation(Classe classe) {
        /*
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    public void onSuccess(Location location) {
                        Log.i("myApp", "new Position computed for class " + location.getLatitude() + " " + location.getLongitude());
                        classe.setLatitude(location.getLatitude());
                        classe.setLongitude(location.getLongitude());
                        try {
                            dbTask.submit(() -> db.classeDao().updateClasse(classe)).get(60L, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            Log.i("myApp", "Errore nel salvare classe docente");
                            e.printStackTrace();
                        }
                        hideProgressBarAnimation();
                        Toast.makeText(MainActivity.this,"Posizione classe " + classe.getNomeClasse() + " modificata ",Toast.LENGTH_SHORT).show();
                    }
                });


        locationTask.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("myApp","Some error getting position");
                e.printStackTrace();
            }
        });
        */
    }

    /*
     *  Se una richiesta è stata validata allora viene fatta la richiesta di localizzazione effettiva
     *  quando finisce fa callback sul metodo della risposta dando la classe o classi che fanno parte della risposta
     */
    private void proceedLocatingClass(long matricolaDocente) {
        /*
             @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null)
                .addOnSuccessListener(this,new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        ExecutorService dbTask = Executors.newSingleThreadExecutor();
                            List<Classe> classiDocente = null;
                            if(location != null){
                                try {
                                    classiDocente = dbTask.submit(()->db.classeDao().getClassiDocente(matricolaDocente)).get(60L,TimeUnit.SECONDS);
                                } catch (Exception e) {
                                    Log.i("myApp","Errore nel prelevare le classi del docente");
                                    e.printStackTrace();
                                }
                                float distanceResults[] = new float[3];
                                for(int i = 0; i < classiDocente.size(); i+=1){ ;
                                    Location.distanceBetween(classiDocente.get(i).getLatitude(),classiDocente.get(i).getLongitude(),location.getLatitude(),location.getLongitude(),distanceResults);

                                    //Troppo distante
                                    if(distanceResults[0] > 50){
                                        classiDocente.remove(i);
                                        i-=1;
                                    }

                                }
                            }
                            //Manda la risposta all'activity, non importa se sia null o meno
                            localizationResponse(classiDocente);
                    }

                }).addOnFailureListener(this,new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("myApp","Some failure in position request " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                */
    }

    private class Adapter extends RecyclerView.Adapter<ClassiViewHolder> {
        List<Classe> classi = new ArrayList<Classe>();

        public Adapter(List<Classe> classi){
            this.classi = classi;
        }

        @NonNull
        @NotNull
        @Override
        public ClassiViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mainactivity_singolaclasse,parent,false);
            return new ClassiViewHolder(view,MainActivity.this);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ClassiViewHolder holder, int position) {
            holder.setClasse(classi.get(position));
        }

        @Override
        public int getItemCount() {
            return classi.size();
        }

    }
}
