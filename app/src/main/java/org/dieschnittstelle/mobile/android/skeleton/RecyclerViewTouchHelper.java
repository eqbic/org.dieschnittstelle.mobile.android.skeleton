package org.dieschnittstelle.mobile.android.skeleton;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.dieschnittstelle.mobile.android.skeleton.Adapter.TodoAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TodoAdapter todoAdapter;

    public RecyclerViewTouchHelper(TodoAdapter taskAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.todoAdapter = taskAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(todoAdapter.getContext());
            builder.setTitle("Delete Todo");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                todoAdapter.deleteTodo(position);
            });
            builder.setNegativeButton("Cancel", (dialog, which)->{
               todoAdapter.notifyItemChanged(position);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            todoAdapter.editTodo(position);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(todoAdapter.getContext(), R.color.red))
                .addSwipeLeftActionIcon(R.drawable.baseline_delete_forever_24)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
