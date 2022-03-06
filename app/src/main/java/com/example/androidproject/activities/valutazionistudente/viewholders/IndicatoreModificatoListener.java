package com.example.androidproject.activities.valutazionistudente.viewholders;

import com.example.androidproject.LocalPersistenceModel.entities.ValoreIndicatore;
import com.example.androidproject.activities.valutazionistudente.ValutazioneStudente;

public interface IndicatoreModificatoListener {
    void indicatoreCambiato(ValutazioneStudente.IndicatoriValori valoreIndicatore);
}
