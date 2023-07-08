package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

import java.util.Comparator;
import java.util.List;

public interface IOverviewViewModel {
    public List<ToDo> getTodos();
    public void onNewTodo();
    public Comparator<ToDo> getSortMode();
    public void setSortMode(Comparator<ToDo> sortMode);

    public void sort();
}
