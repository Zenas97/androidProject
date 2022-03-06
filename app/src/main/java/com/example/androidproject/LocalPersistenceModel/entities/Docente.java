package com.example.androidproject.LocalPersistenceModel.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Docente
{
    @PrimaryKey
    private long matricola;

    private String nome;
    private String cognome;

    public Docente() {
    }

    public long getMatricola() {
        return matricola;
    }

    public void setMatricola(long matricola) {
        this.matricola = matricola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "matricola=" + matricola +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
