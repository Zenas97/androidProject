package com.example.androidproject.LocalPersistenceModel.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value={"id_docente","id_classe","nome_insegnamento"},unique = true))
public class Insegnamento
{
    //L'unicità deve essere qui, si fa tramite indices
    @ColumnInfo(name = "id_docente")
    private long idDocente;
    @ColumnInfo(name = "id_classe")
    private long idClasse;
    @ColumnInfo(name = "nome_insegnamento")
    private String nomeInsegnamento;

    @PrimaryKey(autoGenerate = true)
    private long id;

    public Insegnamento() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(long idDocente) {
        this.idDocente = idDocente;
    }

    public long getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(long idClasse) {
        this.idClasse = idClasse;
    }

    public String getNomeInsegnamento() {
        return nomeInsegnamento;
    }

    public void setNomeInsegnamento(String nomeInsegnamento) {
        this.nomeInsegnamento = nomeInsegnamento;
    }

    @Override
    public String toString() {
        return "Insegnamento{" +
                "idDocente=" + idDocente +
                ", idClasse=" + idClasse +
                ", nomeInsegnamento='" + nomeInsegnamento + '\'' +
                '}';
    }
}
