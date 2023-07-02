package org.dieschnittstelle.mobile.android.skeleton.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.dieschnittstelle.mobile.android.skeleton.DetailActivity;
import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.OverviewActivity;
import org.dieschnittstelle.mobile.android.skeleton.R;
import org.dieschnittstelle.mobile.android.skeleton.util.AsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.util.IRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder>{

    private List<ToDo> todoList = new ArrayList<>();
    private OverviewActivity overview;

    private IRepository<ToDo> repository;
    private AsyncOperationRunner operationRunner;

    public TodoAdapter(IRepository<ToDo> repository, OverviewActivity overview){
        this.overview = overview;
        this.repository = repository;
        this.operationRunner = new AsyncOperationRunner(overview, null);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDo todo = todoList.get(position);
        holder.textView.setText(todo.getName());
        holder.checkBox.setChecked(todo.getDone());
        updateFavIcon(holder, todo.getFavourite());
        holder.date.setText(todo.getDate());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setDone(isChecked);
            operationRunner.run(
                    () -> repository.update(todo),
                    null
            );
        });
        holder.cardView.setOnClickListener(view ->{
            editTodo(position);
        });
        holder.favouriteButton.setOnClickListener(view ->{
            todo.setFavourite(!todo.getFavourite());
            operationRunner.run(
                    ()-> repository.update(todo),
                    null
            );
            updateFavIcon(holder, todo.getFavourite());
        });
    }

    private void updateFavIcon(MyViewHolder holder, boolean isFavourite){
        int resourceId = isFavourite ? R.drawable.baseline_favourite_24 : R.drawable.baseline_not_favourite_24;
        Drawable icon = ContextCompat.getDrawable(overview, resourceId);
        holder.favouriteButton.setBackground(icon);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext(){
        return overview;
    }

    public void setTodos(List<ToDo> todos){
        this.todoList = todos;
        notifyDataSetChanged();
    }

    public void deleteTodo(int position){
        ToDo todo = todoList.get(position);
        operationRunner.run(
                () -> repository.delete(todo),
                null
        );
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTodo(int position){
        ToDo todo = todoList.get(position);
        Intent intent = new Intent(overview, DetailActivity.class);
        intent.putExtra("todo", todo);
        overview.startActivity(intent);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView textView;
        CardView cardView;
        ImageButton favouriteButton;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            textView = itemView.findViewById(R.id.todo_name);
            cardView = itemView.findViewById(R.id.todo);
            favouriteButton = itemView.findViewById(R.id.todo_favourite);
            date = itemView.findViewById(R.id.todo_date);
        }
    }
}
