package com.example.androidproject.activities.mainactivity.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.androidproject.activities.mainactivity.MainActivity;
import com.example.androidproject.LocalPersistenceModel.entities.Classe;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LocalizationConflictResolutionDialog extends DialogFragment {

    private List<Classe> classi;
    private CharSequence[] itemsArray;
    private int selected = 0;
    private MainActivity activity;



    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;

        classi = (List<Classe>) getArguments().getSerializable("classi");
        itemsArray = new CharSequence[classi.size()];
        int i = 0;
        for(Classe x : classi){
            itemsArray[i++] = x.getNomeClasse();
        }
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleziona classe")
                .setSingleChoiceItems(itemsArray, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalizationConflictResolutionDialog.this.selected = which;
                    }
                })
                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.classSelect(classi.get(selected),0);
                    }
                })
                .setNegativeButton("Cancella",null);
        return builder.create();
    }
}
