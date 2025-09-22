package com.example.vitaconect_movil.ui.gestionproductos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestionproductosViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public GestionproductosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
