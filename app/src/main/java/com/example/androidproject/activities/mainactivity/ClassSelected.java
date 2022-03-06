package com.example.androidproject.activities.mainactivity;

import com.example.androidproject.LocalPersistenceModel.entities.Classe;

public interface ClassSelected {
    void classSelect(Classe classe, int quadrimestre);
    void openChangeClassLocationDialog(Classe classe);
}
