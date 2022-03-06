package com.example.androidproject.LocalPersistenceModel.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class Indicatore implements Serializable {

    private long id;

    private String nome;


    public Indicatore() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if(!(obj instanceof Indicatore)) return false;
        if(((Indicatore)obj).id == this.id) return true;
        return false;
    }


}
