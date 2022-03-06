package com.example.androidproject.activities.classe.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.activities.classe.StudentSelected;
import com.example.androidproject.LocalPersistenceModel.entities.Studente;
import com.example.androidproject.R;

import org.jetbrains.annotations.NotNull;

public class StudenteViewHolder extends RecyclerView.ViewHolder {

    TextView nomeStudente;
    TextView cognomeStudente;
    TextView matricolaStudenteText;

    View itemView;
    Button tutteValutazioniButton;
    Button docenteValutazioniButton;
    Studente studente;

    public StudenteViewHolder(@NonNull @NotNull View itemView, StudentSelected listener) {
        super(itemView);
        this.itemView = itemView;
        nomeStudente = itemView.findViewById(R.id.studentName);
        cognomeStudente = itemView.findViewById(R.id.studentSurname);
        matricolaStudenteText = itemView.findViewById(R.id.matricolaStudente);
        tutteValutazioniButton = itemView.findViewById(R.id.TutteValutazioniButton);
        docenteValutazioniButton = itemView.findViewById(R.id.docenteValutazioniButton);


        tutteValutazioniButton.setOnClickListener((view)-> listener.studenteSelezionato(studente,false));
        docenteValutazioniButton.setOnClickListener((view)-> listener.studenteSelezionato(studente,true));

    }

    public void setStudente(Studente studente){
        this.studente = studente;
        matricolaStudenteText.setText("Matricola : " + studente.getMatricola());
        cognomeStudente.setText(studente.getCognome());
        nomeStudente.setText(studente.getNome());
    }
}
