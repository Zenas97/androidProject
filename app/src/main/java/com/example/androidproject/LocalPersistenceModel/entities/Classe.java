package com.example.androidproject.LocalPersistenceModel.entities;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Classe implements Serializable {
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "nome_classe")
    public String nomeClasse;

    //Coordinate
    public double longitudine;
    public double latitudine;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public double getLongitude() {
        return longitudine;
    }

    public void setLongitude(double longitude) {
        this.longitudine = longitude;
    }

    public double getLatitude() {
        return latitudine;
    }

    public void setLatitude(double latitude) {
        this.latitudine = latitude;
    }
}
