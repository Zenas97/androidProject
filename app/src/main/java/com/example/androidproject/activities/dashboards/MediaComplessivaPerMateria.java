package com.example.androidproject.activities.dashboards;

public class MediaComplessivaPerMateria {
    public String nomeInsegnamento;
    public double media;

    public MediaComplessivaPerMateria() {
    }

    public MediaComplessivaPerMateria(String nomeInsegnamento, double media) {
        this.nomeInsegnamento = nomeInsegnamento;
        this.media = media;
    }

    public String getNomeInsegnamento() {
        return nomeInsegnamento;
    }

    public void setNomeInsegnamento(String nomeInsegnamento) {
        this.nomeInsegnamento = nomeInsegnamento;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }
}
