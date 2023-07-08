package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.content.Intent;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

public class DetailActivityViewModel extends ViewModel implements IDetailViewModel {

    private MutableLiveData<String> nameError = new MutableLiveData<>();
    private MutableLiveData<String> dateError = new MutableLiveData<>();

    private MutableLiveData<String> timeError = new MutableLiveData<>();

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

    @Override
    public MutableLiveData<String> getNameError() {
        return this.nameError;
    }

    @Override
    public MutableLiveData<String> getDateError() {
        return this.dateError;
    }

    @Override
    public MutableLiveData<String> getTimeError() {
        return this.timeError;
    }


    @Override
    public boolean validateName() {
        if(todo.getName() != null && todo.getName().length() > 0){
            return true;
        }
        this.nameError.setValue("You must set a name.");
        return false;
    }

    @Override
    public boolean validateDate() {
        if(todo.getDate() != null && todo.getDate().length() > 0){
            return true;
        }
        this.dateError.setValue("You must set a date.");
        return false;
    }

    @Override
    public boolean validateTime() {
        if(todo.getTime() != null && todo.getDate().length() > 0){
            return true;
        }
        this.timeError.setValue("You must set a time.");
        return false;
    }

    public boolean validateForm(){
        boolean valid = validateName();
        valid &= validateDate();
        valid &= validateTime();
        return valid;
    }

//    @Override
//    public boolean onInputChanged() {
//        return false;
//    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

    public MutableLiveData<Boolean> getSavedOccured(){
        return this.savedOccured;
    }
}
