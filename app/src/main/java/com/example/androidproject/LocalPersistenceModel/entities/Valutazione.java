package com.example.androidproject.LocalPersistenceModel.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"id_studente","id_insegnamento","quadrimestre"},unique = true))
public class Valutazione
{
    //Dall'id dell'insegnamento risalgo alla valutazione unica
    @ColumnInfo(name = "id_studente")
    private long idStudente;
    @ColumnInfo(name = "id_insegnamento")
    private long idInsegnamento;
    private int quadrimestre;

    @PrimaryKey(autoGenerate = true)
    private long id;

    public Valutazione() {
    }

    public long getIdStudente() {
        return idStudente;
    }

    public void setIdStudente(long idStudente) {
        this.idStudente = idStudente;
    }

    public long getIdInsegnamento() {
        return idInsegnamento;
    }

    public void setIdInsegnamento(long idInsegnamento) {
        this.idInsegnamento = idInsegnamento;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Valutazione{" +
                "idStudente=" + idStudente +
                ", idInsegnamento=" + idInsegnamento +
                ", quadrimestre=" + quadrimestre +
                '}';
    }
}
