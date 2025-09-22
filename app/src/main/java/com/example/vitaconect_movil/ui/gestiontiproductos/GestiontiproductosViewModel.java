package com.example.vitaconect_movil.ui.gestiontiproductos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestiontiproductosViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public GestiontiproductosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

