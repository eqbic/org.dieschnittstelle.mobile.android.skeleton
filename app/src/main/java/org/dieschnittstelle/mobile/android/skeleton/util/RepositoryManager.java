//package org.dieschnittstelle.mobile.android.skeleton.util;
//
//import android.app.Activity;
//import android.util.Log;
//
//import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
//
//import java.util.List;
//
//public class RepositoryManager implements IRepository<ToDo> {
//    private static IRepository<ToDo> localRepo;
//    private static IRepository<ToDo> webRepo;
//    private AsyncOperationRunner operationRunner;
//
//    public RepositoryManager(Activity activity){
//        this.operationRunner = new AsyncOperationRunner(activity, null);
//        localRepo = new DatabaseRepository(activity.getApplicationContext());
//        webRepo = new WebRepository();
//        operationRunner.run(
//                this::initialSync,
//                null);
//    }
//
//    private boolean initialSync(){
//        try{
//            List<ToDo> webTodos = webRepo.readAll();
//            clearLocalDb();
//            for (ToDo todo : webTodos){
//                if(localRepo.read(todo.getId()) == null){
//                    localRepo.create(todo);
//                }else{
//                    localRepo.update(todo);
//                }
//            }
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }
//
//    private void clearLocalDb(){
//        for(ToDo todo : localRepo.readAll()){
//            localRepo.delete(todo);
//        }
//    }
//
//    public List<ToDo> readAll(){
//        List<ToDo> result = localRepo.readAll();
//        try{
//            result = webRepo.readAll();
//        }catch (Exception e){
//            Log.w(null, "Could only read from local database.");
//        }
//        return result;
//    }
//
//    public ToDo create(ToDo todo){
//        ToDo result = localRepo.create(todo);
//        try{
//            result = webRepo.create(todo);
//        }catch (Exception e){
//            Log.w(null, "Could only create locally.");
//        }
//        return result;
//    }
//
//    public ToDo read(long id){
//        ToDo result = localRepo.read(id);
//        try{
//            result =  webRepo.read(id);
//        }catch (Exception e){
//            Log.w(null, "Could only read from local database.");
//        }
//        return result;
//    }
//
//    public boolean update(ToDo todo){
//        boolean success = localRepo.update(todo);
//        try{
//            success |= webRepo.update(todo);
//        }catch (Exception e){
//            Log.w(null, "Could only update locally.");
//        }
//        return success;
//    }
//
//    public boolean delete(ToDo todo){
//        boolean success = localRepo.delete(todo);
//        try{
//           success |= webRepo.delete(todo);
//        }catch (Exception e){
//            Log.w(null, "Could only delete locally.");
//        }
//        return success;
//    }
//}
