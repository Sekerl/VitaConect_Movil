package com.example.vitaconect_movil.ui.gestionpresentaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestionpresentacionesViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public GestionpresentacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}




