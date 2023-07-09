package org.dieschnittstelle.mobile.android.skeleton.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class WebRepository implements IRepository<ToDo> {

    public static interface TodoResource{
        @POST("/api/todos")
        public Call<ToDo> create(@Body ToDo todo);
        @GET("/api/todos")
        public Call<List<ToDo>> readAll();
        @GET("/api/todos/{id}")
        public Call<ToDo> readById(@Path("id")long id);
        @PUT("/api/todos/{id}")
        public Call<ToDo> update(@Path("id")long id,@Body ToDo todo);
        @DELETE("/api/todos/{id}")
        public Call<Boolean> delete(@Path("id")long id);
        @DELETE("/api/todos")
        public Call<Boolean> deleteAll();
    }

    private TodoResource todoResource;

    public WebRepository(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Retrofit webApiBase = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.todoResource = webApiBase.create(TodoResource.class);

    }

    @Override
    public ToDo create(ToDo item) {
        try{
            return this.todoResource.create(item).execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ToDo> readAll() {
        try{
            return this.todoResource.readAll().execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ToDo read(long id) {
        try{
            return this.todoResource.readById(id).execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(ToDo item) {
        try{
            this.todoResource.update(item.getId(), item).execute().body();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(ToDo item) {
        try{
            return this.todoResource.delete(item.getId()).execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteAll() {
        try{
            return this.todoResource.deleteAll().execute().body();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
