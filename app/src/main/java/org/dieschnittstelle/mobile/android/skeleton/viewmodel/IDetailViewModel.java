package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

public interface IDetailViewModel {
    public ToDo getTodo();
    public void onSaveTodo();
}
