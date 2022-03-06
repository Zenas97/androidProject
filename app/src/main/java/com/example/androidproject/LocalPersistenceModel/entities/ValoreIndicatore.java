package com.example.androidproject.LocalPersistenceModel.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

public class ValoreIndicatore {

    private Long idValoreIndicatore;

    private Integer valore;

    public ValoreIndicatore() {
    }

    public Long getIdValoreIndicatore() {
        return idValoreIndicatore;
    }

    public void setIdValoreIndicatore(Long idValoreIndicatore) {
        this.idValoreIndicatore = idValoreIndicatore;
    }

    public Integer getValore() {
        return valore;
    }

    public void setValore(Integer valore) {
        this.valore = valore;
    }
}
