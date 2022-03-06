package com.example.androidproject.activities.dashboards;

public class BarChartBinAndamentoClasse {
    public long GruppoVoti;
    public long Conteggio;
    public float Normalizzato;

    public BarChartBinAndamentoClasse() {
    }

    public long getGruppoVoti() {
        return GruppoVoti;
    }

    public void setGruppoVoti(long gruppoVoti) {
        GruppoVoti = gruppoVoti;
    }

    public long getConteggio() {
        return Conteggio;
    }

    public void setConteggio(long conteggio) {
        this.Conteggio = conteggio;
    }

    public double getNormalizzato() {
        return Normalizzato;
    }

    public void setNormalizzato(float normalizzato) {
        Normalizzato = normalizzato;
    }

    @Override
    public String toString() {
        return "BarChartBinAndamentoClasse{" +
                "GruppoVoti=" + GruppoVoti +
                ", Conteggio=" + Conteggio +
                ", Normalizzato=" + Normalizzato +
                '}';
    }
}
