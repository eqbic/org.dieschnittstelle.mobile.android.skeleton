package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

public class DetailActivityViewModel extends ViewModel implements IDetailViewModel {

    private ToDo todo;
    private MutableLiveData<Boolean> savedOccured = new MutableLiveData<>();

    @Override
    public ToDo getTodo() {
        return this.todo;
    }

    @Override
    public void onSaveTodo() {
        this.savedOccured.setValue(true);
    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

    public MutableLiveData<Boolean> getSavedOccured(){
        return this.savedOccured;
    }
}
