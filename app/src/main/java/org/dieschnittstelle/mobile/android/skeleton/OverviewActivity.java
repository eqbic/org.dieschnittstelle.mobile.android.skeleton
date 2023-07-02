package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dieschnittstelle.mobile.android.skeleton.Adapter.TodoAdapter;
import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityOverviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.util.AsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.util.IRepository;
import org.dieschnittstelle.mobile.android.skeleton.util.RepositoryManager;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.OverviewActivityViewModel;

public class OverviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private static RepositoryManager repositoryManager;
    private AsyncOperationRunner operationRunner;
    private OverviewActivityViewModel viewModel;
    private ActivityOverviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup viewmodel and register create method
        this.viewModel = new ViewModelProvider(this).get(OverviewActivityViewModel.class);
        this.viewModel.getNewOccured().observe(this, occured->{
            createNewTodo();
        });

        // setup data binding
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_overview);
        this.binding.setViewmodel(this.viewModel);

        this.operationRunner = new AsyncOperationRunner(this, null);

        repositoryManager = new RepositoryManager(this.getApplicationContext());

        this.todoAdapter = new TodoAdapter(repositoryManager, OverviewActivity.this);

        // setup recyclerview
        this.recyclerView = findViewById(R.id.itemList);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(todoAdapter);

        // update todos on the first time and get them by viewmodel otherwise
        if(this.viewModel.getTodos() == null){
            updateTodos();
        }else{
            this.todoAdapter.setTodos(this.viewModel.getTodos());
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(todoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_overview_menu, menu);
        return true;
    }

    public void showSortMenu(View view){
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.activity_overview_menu, popup.getMenu());
        popup.show();
    }

    public static IRepository<ToDo> getRepository(){
        return repositoryManager;
    }

    private void createNewTodo() {
        Intent callDetailViewIntent = new Intent(this, DetailActivity.class);
        startActivity(callDetailViewIntent);
    }

    private void updateTodos(){
        operationRunner.run(
                () -> repositoryManager.readAll(),
                items -> {
                    this.todoAdapter.setTodos(items);
                    this.viewModel.setTodos(items);
                }
        );
    }

    @Override
    protected void onStart() {
        if(repositoryManager != null){
            updateTodos();
        }
        super.onStart();
    }


}
