package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.R;

import java.util.List;

public class OverviewActivityViewModel extends ViewModel implements IOverviewViewModel{

    private List<ToDo> todos;

    private MutableLiveData<Boolean> newOccured = new MutableLiveData<>();
    @Override
    public List<ToDo> getTodos() {
        return this.todos;
    }

    @Override
    public void onNewTodo() {
        this.newOccured.setValue(true);
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
    }

    public MutableLiveData<Boolean> getNewOccured(){
        return this.newOccured;
    }

}
