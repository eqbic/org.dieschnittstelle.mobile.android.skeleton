package org.dieschnittstelle.mobile.android.skeleton.util;

import java.util.List;

public interface IRepository<T> {
    public T create(T item);
    public List<T> readAll();
    public T read(long id);
    public boolean update(T item);
    public boolean delete(T item);
}
