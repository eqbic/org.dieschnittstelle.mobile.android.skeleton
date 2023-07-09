package org.dieschnittstelle.mobile.android.skeleton.util;

import android.app.Activity;
import android.content.Context;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

import java.util.List;

public class SyncedRepository implements IRepository<ToDo> {
    private IRepository<ToDo> localRepository;
    private IRepository<ToDo> remoteRepository;

    public SyncedRepository(Context context){
        this.localRepository = new DatabaseRepository(context);
        this.remoteRepository = new WebRepository();

        new Thread(()->{
            Sync();
        }).start();
    }

    public void Sync(){
        List<ToDo> localTodos = localRepository.readAll();
        List<ToDo> remoteTodos = remoteRepository.readAll();
        if(localTodos.size() > 0){
            // there are todos in local database -> remove all remote todos and use local ones
            remoteRepository.deleteAll();
            for(ToDo todo : localTodos){
                remoteRepository.create(todo);
            }
        }else{
            // there are no todos in local database -> use all todos from remote
            for(ToDo todo : remoteTodos){
                localRepository.create(todo);
            }
        }
    }

    @Override
    public ToDo create(ToDo item) {
        item = localRepository.create(item);
        remoteRepository.create(item);
        return item;
    }

    @Override
    public List<ToDo> readAll() {
        Sync();
        return localRepository.readAll();
    }

    @Override
    public ToDo read(long id) {
        return localRepository.read(id);
    }

    @Override
    public boolean update(ToDo item) {
        localRepository.update(item);
        remoteRepository.update(item);
        return true;
    }

    @Override
    public boolean delete(ToDo item) {
        localRepository.delete(item);
        remoteRepository.delete(item);
        return true;
    }

    @Override
    public boolean deleteAll() {
        localRepository.deleteAll();
        remoteRepository.deleteAll();
        return true;
    }
}
