package com.example.androidproject;

import java.util.ArrayList;
import java.util.List;

public class ValutazioneStudente {
    public Long idValutazione;
    public String nomeInsegnamento;
    public List<IndicatoriValori> indicatori = new ArrayList<>();

    public Long getIdValutazione() {
        return idValutazione;
    }

    public void setIdValutazione(Long idValutazione) {
        this.idValutazione = idValutazione;
    }

    public String getNomeInsegnamento() {
        return nomeInsegnamento;
    }

    public void setNomeInsegnamento(String nomeInsegnamento) {
        this.nomeInsegnamento = nomeInsegnamento;
    }

    @Override
    public String toString() {
        return "ValutazioneStudente{" +
                "idValutazione=" + idValutazione +
                ", nomeInsegnamento='" + nomeInsegnamento + '\'' +
                ", indicatori=" + indicatori +
                '}';
    }

    public List<IndicatoriValori> getIndicatori() {
        return indicatori;
    }

    public void setIndicatori(List<IndicatoriValori> indicatori) {
        this.indicatori = indicatori;
    }

    public static class IndicatoriValori {
        public Long idIndicatoreValore;
        public String nomeIndicatore;
        public Integer valore;

        public Long getIdIndicatoreValore() {
            return idIndicatoreValore;
        }

        public void setIdIndicatoreValore(Long idIndicatoreValore) {
            this.idIndicatoreValore = idIndicatoreValore;
        }

        public String getNomeIndicatore() {
            return nomeIndicatore;
        }

        public void setNomeIndicatore(String nomeIndicatore) {
            this.nomeIndicatore = nomeIndicatore;
        }

        public Integer getValore() {
            return valore;
        }

        public void setValore(Integer valore) {
            this.valore = valore;
        }

        @Override
        public String toString() {
            return "IndicatoriValori{" +
                    "idIndicatoreValore=" + idIndicatoreValore +
                    ", nomeIndicatore='" + nomeIndicatore + '\'' +
                    ", valore=" + valore +
                    '}';
        }
    }
}
