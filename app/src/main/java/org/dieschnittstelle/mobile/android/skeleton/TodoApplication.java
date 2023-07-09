package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.util.DatabaseRepository;
import org.dieschnittstelle.mobile.android.skeleton.util.IRepository;
import org.dieschnittstelle.mobile.android.skeleton.util.WebRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import retrofit2.http.Url;

public class TodoApplication extends Application {

    private IRepository<ToDo> repository;

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            if(checkConnectivity().get()){
                this.repository = new WebRepository();
            }else{
                this.repository = new DatabaseRepository(this);
            }
            Toast.makeText(this, "Using Repository: " + repository.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            this.repository = new DatabaseRepository(this);
            Toast.makeText(this, "Error occurred. Falling back to: " + repository.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

        }
    }

    public IRepository<ToDo> getRepository(){
        return this.repository;
    }

    public CompletableFuture<Boolean> checkConnectivity(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        new Thread(()->{
            try{
                HttpURLConnection connection = (HttpURLConnection) new URL("http://10.0.2.2:8080/api/todos").openConnection();
                connection.setConnectTimeout(500);
                connection.setReadTimeout(500);
                connection.setDoInput(true);
                connection.connect();
                connection.getInputStream();
                future.complete(true);
            }catch (Exception e){
                future.complete(false);
            }
        }).start();
        return future;
    }
}
