package com.example.vitaconect_movil.ui.gestionlaboratorios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GestionlaboratoriosViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public GestionlaboratoriosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}


