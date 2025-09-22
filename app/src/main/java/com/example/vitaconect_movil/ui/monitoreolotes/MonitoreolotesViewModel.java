package com.example.vitaconect_movil.ui.monitoreolotes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitoreolotesViewModel  {

    private final MutableLiveData<String> mText;

    public MonitoreolotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}