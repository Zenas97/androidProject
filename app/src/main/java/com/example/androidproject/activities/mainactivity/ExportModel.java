package com.example.androidproject.activities.mainactivity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public class ExportModel {
    public long idClasse;
    public String nomeClasse;
    public long matricolaDocente;
    public List<Valutazione> valutazioni;

    public ExportModel(){}

    public long getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(long idClasse) {
        this.idClasse = idClasse;
    }

    public long getMatricolaDocente() {
        return matricolaDocente;
    }

    public void setMatricolaDocente(long matricolaDocente) {
        this.matricolaDocente = matricolaDocente;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public List<Valutazione> getValutazioni() {
        return valutazioni;
    }

    public void setValutazioni(List<Valutazione> valutazioni) {
        this.valutazioni = valutazioni;
    }

    public static class Valutazione{

        @JsonUnwrapped
        public DatiValutazione datiValutazione;
        public List<ValoriIndicatori> valoriIndicatori;

        public Valutazione(){}

        public DatiValutazione getDatiValutazione() {
            return datiValutazione;
        }

        public void setDatiValutazione(DatiValutazione datiValutazione) {
            this.datiValutazione = datiValutazione;
        }

        public List<ValoriIndicatori> getValoriIndicatori() {
            return valoriIndicatori;
        }

        public void setValoriIndicatori(List<ValoriIndicatori> valoriIndicatori) {
            this.valoriIndicatori = valoriIndicatori;
        }

        @Override
        public String toString() {
            return "Valutazione{" +
                    "datiValutazione=" + datiValutazione +
                    ", valoriIndicatori=" + valoriIndicatori +
                    '}';
        }

        public static class DatiValutazione{
            @JsonIgnore
            public long idValutazione;
            public long matricolaStudente;
            public String nomeStudente;
            public String cognomeStudente;
            public String nomeInsegnamento;
            public int quadrimestre;

            public long getMatricolaStudente() {
                return matricolaStudente;
            }

            public void setMatricolaStudente(long matricolaStudente) {
                this.matricolaStudente = matricolaStudente;
            }

            public String getNomeInsegnamento() {
                return nomeInsegnamento;
            }

            public void setNomeInsegnamento(String nomeInsegnamento) {
                this.nomeInsegnamento = nomeInsegnamento;
            }

            public int getQuadrimestre() {
                return quadrimestre;
            }

            public void setQuadrimestre(int quadrimestre) {
                this.quadrimestre = quadrimestre;
            }

            public long getIdValutazione() {
                return idValutazione;
            }

            public void setIdValutazione(long idValutazione) {
                this.idValutazione = idValutazione;
            }

            public String getNomeStudente() {
                return nomeStudente;
            }

            public void setNomeStudente(String nomeStudente) {
                this.nomeStudente = nomeStudente;
            }

            public String getCognomeStudente() {
                return cognomeStudente;
            }

            public void setCognomeStudente(String cognomeStudente) {
                this.cognomeStudente = cognomeStudente;
            }

            @Override
            public String toString() {
                return "DatiValutazione{" +
                        "matricolaStudente=" + matricolaStudente +
                        ", nomeInsegnamento='" + nomeInsegnamento + '\'' +
                        ", quadrimestre=" + quadrimestre +
                        '}';
            }
        }

        public static class ValoriIndicatori{
            public String nomeIndicatore;
            public Integer valoreIndicatore;

            public ValoriIndicatori(){}

            public String getNomeIndicatore() {
                return nomeIndicatore;
            }

            public void setNomeIndicatore(String nomeIndicatore) {
                this.nomeIndicatore = nomeIndicatore;
            }

            public Integer getValoreIndicatore() {
                return valoreIndicatore;
            }

            public void setValoreIndicatore(Integer valoreIndicatore) {
                this.valoreIndicatore = valoreIndicatore;
            }

            @Override
            public String toString() {
                return "ValoriIndicatori{" +
                        "nomeIndicatore='" + nomeIndicatore + '\'' +
                        ", valoreIndicatore=" + valoreIndicatore +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "ExportModel{" +
                "idClasse=" + idClasse +
                ", matricolaDocente=" + matricolaDocente +
                ", valutazioni=" + valutazioni.toString() +
                '}';
    }
}
