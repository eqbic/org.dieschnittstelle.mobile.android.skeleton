package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.R;

import java.util.Comparator;
import java.util.List;

public class OverviewActivityViewModel extends ViewModel implements IOverviewViewModel{

    private List<ToDo> todos;

    public final static Comparator<ToDo> SORT_BY_NAME = Comparator.comparing(ToDo::getDone).thenComparing(ToDo::getName);
    public final static Comparator<ToDo> SORT_BY_DATE = Comparator.comparing(ToDo::getDone).thenComparing(ToDo::getExpiry);
    public final static Comparator<ToDo> SORT_BY_FAVOURITE = Comparator.comparing(ToDo::getDone).thenComparing(ToDo::getFavourite, Comparator.reverseOrder());


    private Comparator<ToDo> sortMode = SORT_BY_DATE;

    private MutableLiveData<Boolean> newOccured = new MutableLiveData<>();
    @Override
    public List<ToDo> getTodos() {
        return this.todos;
    }

    @Override
    public void onNewTodo() {
        this.newOccured.setValue(true);
    }

    @Override
    public Comparator<ToDo> getSortMode() {
        return this.sortMode;
    }

    @Override
    public void setSortMode(Comparator<ToDo> sortMode) {
        this.sortMode = sortMode;
    }

    @Override
    public void sort() {
        this.todos.sort(this.sortMode);
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
        this.sort();
    }

    public MutableLiveData<Boolean> getNewOccured(){
        return this.newOccured;
    }

}
