package org.dieschnittstelle.mobile.android.skeleton.util;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

import java.io.Serializable;
import java.util.List;

public class DatabaseRepository implements IRepository<ToDo> {
    @Override
    public ToDo create(ToDo item) {
        long id = db.getDao().create(item);
        item.setId(id);
        return item;
    }

    @Override
    public List<ToDo> readAll() {
        return db.getDao().readAll();
    }

    @Override
    public ToDo read(long id) {
        return db.getDao().readById(id);
    }

    @Override
    public boolean update(ToDo item) {
        db.getDao().update(item);
        return true;
    }

    @Override
    public boolean delete(ToDo item) {
        db.getDao().delete(item);
        return true;
    }

    @Override
    public boolean deleteAll() {
        for(ToDo todo : readAll()){
            delete(todo);
        }
        return true;
    }

    @Dao
    public static interface TodoDao{

        @Query("SELECT * FROM todos")
        public List<ToDo> readAll();

        @Query("SELECT * FROM todos WHERE id == (:id)")
        public ToDo readById(long id);

        @Insert
        public long create(ToDo todo);

        @Update
        public void update(ToDo todo);

        @Delete
        public void delete(ToDo todo);
    }

    @Database(entities = {ToDo.class}, version = 1)
    public static abstract class ToDoDatabase extends RoomDatabase{
        public abstract TodoDao getDao();
    }

    private ToDoDatabase db;

    public DatabaseRepository(Context context){
        db = Room.databaseBuilder(context, ToDoDatabase.class, "todos.db").build();
    }
}
