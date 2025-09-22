package com.example.vitaconect_movil.ui.gestionproveedores;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestionproveedoresViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GestionproveedoresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}