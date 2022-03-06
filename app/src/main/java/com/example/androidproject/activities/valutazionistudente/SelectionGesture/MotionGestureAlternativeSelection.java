package com.example.androidproject.activities.valutazionistudente.SelectionGesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MotionGestureAlternativeSelection extends ObservableAlternativeSelection implements SensorEventListener {

    /*
     * Stato interno riguardante i sensori
     */
    private SensorManager sensorManager;
    private Sensor accelerometer;


    /*
     * Stato interno riguardante la finestra di registrazioni, cioè un insieme di registrazioni
     * in un dato intervallo di tempo, le registrazioni di una finestra vengono processate al fine
     * di capire se è avvenuto un particolare motion gesture in un certo intervallo di tempo
     */
    private long windowLastTime = 0;
    private int windowCounter = 0;
    boolean windowFlagFirstRegistration = false;
    private float[] measuringWindow = new float[1000];

    /*
     * Stato interno usato per il filtraggio delle registrazioni
     */
    private float filterBias = 0.8f;
    private float lastAccelerationRegistered = 0;


    public MotionGestureAlternativeSelection(Context context) throws Exception{
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }else throw new Exception("Non è stato trovato l'accelerometro, la selezione tramite gesture motion" +
                " non sarà possibile");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue = event.values[0];
        long currentTime = System.currentTimeMillis();

        if(currentTime - windowLastTime >= 250){
            processReadingsWindow(currentTime);
        }

        //Filtra di smoothing per i dati letti
        float reading = lastAccelerationRegistered * (1 - filterBias) + xValue * filterBias;

        //Filtro di soglia per leggere segnali di accelerazione significativi
        if(Math.abs(reading) > 10){

            //Solo se c'è ancora spazio nella finestra, aggiungi l'attuale registrazione
            if(windowCounter < 1000){
                /*
                 * Se la finestra è nuova perchè è stata recentemente resettata oppure è la prima
                 * setta il tempo della prima registrazione e setta la flag di prima registrazione
                 * a false
                 */
                if(windowFlagFirstRegistration == true){
                    windowLastTime = currentTime;
                    windowFlagFirstRegistration = false;
                }
                //Aggiungi lettura filtrata alla finestra
                measuringWindow[windowCounter++] = reading;

            }
        }

        lastAccelerationRegistered = reading;
    }

    private void processReadingsWindow(long currentTime) {
        int positiveAccelerationsCounter = 0;
        int negativeAccelerationsCounter = 0;
        float firstHalfTotalCumulative = 0;
        int firstHalf = (int) Math.round((windowCounter /2.0));

        //Processa la finestra solo se ha misurazioni a sufficienza
        if(windowCounter < 5) return;


        for(int i = 0; i < windowCounter; i++) {
            //Fai la sommatoria della prima metà dei segnali nella finestra
            if(i < firstHalf){
                firstHalfTotalCumulative += measuringWindow[i];
            }

            /*
             * Conta le accelerazioni rispettivamente positive e negative
             */
            if (measuringWindow[i] > 0) {
                positiveAccelerationsCounter+=1;
            } else {
                negativeAccelerationsCounter+=1;
            }
        }

        /*
         * Prendi le misurazioni che tendono ad essere simmetriche, cioè quelle che hanno un numero simile
         * di registrazione positive e negative
         */
        if(Math.abs(positiveAccelerationsCounter - negativeAccelerationsCounter) <= 2){
            /*
             * Fai la sommatoria della prima metà dei segnali della finestra, se è negativa con buona probabilità
             * è un movimento a sinistra, altrimenti a destra
             */
            if(firstHalfTotalCumulative < 0){
                left();
            } else {
                right();
            }
        }
        windowCounter = 0;
        windowLastTime = currentTime;
        windowFlagFirstRegistration = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void closeSensor(){
        sensorManager.unregisterListener(this);
    }

    public void activateSensor(){
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

}
