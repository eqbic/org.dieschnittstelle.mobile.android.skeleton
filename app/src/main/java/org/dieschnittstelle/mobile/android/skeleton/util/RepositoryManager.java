package org.dieschnittstelle.mobile.android.skeleton.util;

import android.content.Context;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

import java.util.List;

public class RepositoryManager implements IRepository<ToDo> {
    private static IRepository<ToDo> localRepo;
    private static IRepository<ToDo> webRepo;

    public RepositoryManager(Context context){
        localRepo = new DatabaseRepository(context);
        webRepo = new WebRepository();
    }

    public List<ToDo> readAll(){
        try{
            List<ToDo> todos = webRepo.readAll();
            for (ToDo todo : todos){
                if(localRepo.read(todo.getId()) == null){
                    localRepo.create(todo);
                }else{
                    localRepo.update(todo);
                }
            }
            return todos;
        }catch (Exception e){
            return localRepo.readAll();
        }
    }

    public ToDo create(ToDo todo){
        try{
            return webRepo.create(todo);
        }catch (Exception e){
            return localRepo.create(todo);
        }
    }

    public ToDo read(long id){
        try{
            return webRepo.read(id);
        }catch (Exception e){
            return localRepo.read(id);
        }
    }

    public boolean update(ToDo todo){
        try{
            return webRepo.update(todo);
        }catch (Exception e){
            return localRepo.update(todo);
        }
    }

    public boolean delete(ToDo toDo){
        try{
            return webRepo.update(toDo);
        }catch (Exception e){
            return localRepo.update(toDo);
        }
    }


}
