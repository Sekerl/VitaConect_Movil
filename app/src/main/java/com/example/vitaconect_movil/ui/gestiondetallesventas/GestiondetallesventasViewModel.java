package com.example.vitaconect_movil.ui.gestiondetallesventas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestiondetallesventasViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public GestiondetallesventasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

