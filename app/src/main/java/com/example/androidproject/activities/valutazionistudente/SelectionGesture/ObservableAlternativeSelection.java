package com.example.androidproject.activities.valutazionistudente.SelectionGesture;

public abstract class ObservableAlternativeSelection implements AlternativeSelection {
    AlternativeSelectionObserver observer;

    public void registerListener(AlternativeSelectionObserver listener) {
        observer = listener;
    }

    public void unregisterListener(){
        observer = null;
    }

    public AlternativeSelectionObserver getListener(){
        return observer;
    }

    @Override
    public void left() {
        if(observer != null) observer.leftEvent();
    }

    @Override
    public void right() {
        if(observer != null) observer.rightEvent();
    }
}
