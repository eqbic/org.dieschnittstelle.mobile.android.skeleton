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
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.OverviewActivityViewModel;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoElementHolder>{

//    private List<ToDo> todoList = new ArrayList<>();
    private OverviewActivity overview;

    private IRepository<ToDo> repository;
    private AsyncOperationRunner operationRunner;
    private OverviewActivityViewModel viewModel;

    public TodoAdapter(IRepository<ToDo> repository, OverviewActivity overview, OverviewActivityViewModel viewModel){
        this.overview = overview;
        this.repository = repository;
        this.viewModel = viewModel;
        this.operationRunner = new AsyncOperationRunner(overview, null);
    }

    @NonNull
    @Override
    public TodoElementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_layout, parent, false);
        return new TodoElementHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoElementHolder holder, int position) {
        final ToDo todo = this.viewModel.getTodos().get(position);

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
        holder.textView.setText(todo.getName());
        holder.checkBox.setChecked(todo.getDone());
        updateFavIcon(holder, todo.getFavourite());
        holder.date.setText(todo.getDate());
    }

    private void updateFavIcon(TodoElementHolder holder, boolean isFavourite){
        int resourceId = isFavourite ? R.drawable.baseline_favourite_24 : R.drawable.baseline_not_favourite_24;
        Drawable icon = ContextCompat.getDrawable(overview, resourceId);
        holder.favouriteButton.setBackground(icon);
    }

    @Override
    public int getItemCount() {
        if(this.viewModel == null || this.viewModel.getTodos() == null){
            return 0;
        }
        return this.viewModel.getTodos().size();
    }

    public Context getContext(){
        return overview;
    }

    public void update(){
        notifyDataSetChanged();
    }

    public void deleteTodo(int position){
        ToDo todo = this.viewModel.getTodos().get(position);
        operationRunner.run(
                () -> repository.delete(todo),
                null
        );
        this.viewModel.getTodos().remove(position);
        notifyItemRemoved(position);
    }

    public void editTodo(int position){
        ToDo todo = this.viewModel.getTodos().get(position);
        Intent intent = new Intent(overview, DetailActivity.class);
        intent.putExtra("todo", todo);
        overview.startActivity(intent);
    }

    public static class TodoElementHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView textView;
        CardView cardView;
        ImageButton favouriteButton;
        TextView date;

        public TodoElementHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            textView = itemView.findViewById(R.id.todo_name);
            cardView = itemView.findViewById(R.id.todo);
            favouriteButton = itemView.findViewById(R.id.todo_favourite);
            date = itemView.findViewById(R.id.todo_date);
        }
    }
}
