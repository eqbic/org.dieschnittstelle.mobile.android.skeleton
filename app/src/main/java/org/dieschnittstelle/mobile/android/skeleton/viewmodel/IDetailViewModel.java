package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

public interface IDetailViewModel {
    public ToDo getTodo();
    public void onSaveTodo();

    public MutableLiveData<String> getNameError();
    public MutableLiveData<String> getDateError();

    public MutableLiveData<String> getTimeError();

    public boolean validateName();
    public boolean validateDate();
    public boolean validateTime();
//    public boolean onInputChanged();
}
