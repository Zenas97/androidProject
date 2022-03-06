package com.example.androidproject.activities.mainactivity.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.androidproject.activities.mainactivity.MainActivity;

import org.jetbrains.annotations.NotNull;

public class DialogImportDataConfirmation extends DialogFragment {

    MainActivity activity;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        return dialogBuilder.setTitle("Import schema")
                .setMessage("Importare uno schema causerà la cancellazione dei dati memorizzati, assicurarsi di salvare i dati esportandoli con l'" +
                        "apposita funzione")
                .setPositiveButton("Conferma",(dialog,which)->{
                    activity.filePickerAction("Import selected", Intent.ACTION_OPEN_DOCUMENT,0);
                }).create();
    }
}
