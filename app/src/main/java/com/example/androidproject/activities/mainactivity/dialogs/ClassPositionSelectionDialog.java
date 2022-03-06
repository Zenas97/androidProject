package com.example.androidproject.activities.mainactivity.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.androidproject.activities.mainactivity.MainActivity;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class ClassPositionSelectionDialog extends DialogFragment {

    MainActivity context;
    Classe classe;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;

        classe = (Classe) getArguments().getSerializable("classe");
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setTitle("Cambia posizione classe " + classe.getNomeClasse())
                .setMessage("Confermando verrà registrata se possible la posizione attuale come nuova posizione della classe")
                .setPositiveButton("Conferma",(dialog,which)->{

                    Task<LocationSettingsResponse> task = context.classLocalizationRequest();
                    //Se il task è null è perchè mancano i permessi
                    if(task == null) return;

                    task.addOnSuccessListener(context,new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            context.changeClassPositionByLocation(classe);
                        }
                    });

                    task.addOnFailureListener(context,new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            e.printStackTrace();
                            Log.i("myApp","Errore localizzazione "  + e.getCause() + " " + e.getMessage() );
                            Toast.makeText(context,"Errore localizzazione classe : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .setNegativeButton("Cancella",(dialog,which)->{

                }).create();
    }
}
