package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

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
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.OverviewActivityViewModel;

public class OverviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private ProgressBar progressBar;
    private AsyncOperationRunner operationRunner;
    private OverviewActivityViewModel viewModel;
    private ActivityOverviewBinding binding;
    private IRepository<ToDo> repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup viewmodel and register create method
        this.viewModel = new ViewModelProvider(this).get(OverviewActivityViewModel.class);
        this.viewModel.getNewOccured().observe(this, occured->{
            createNewTodo();
        });

        // setup progressbar
        this.progressBar = findViewById(R.id.progressBar);

        // setup data binding
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_overview);
        this.binding.setViewmodel(this.viewModel);

        this.operationRunner = new AsyncOperationRunner(this, this.progressBar);

        this.repository = ((TodoApplication)getApplication()).getRepository();
        this.todoAdapter = new TodoAdapter(this.repository, OverviewActivity.this, this.viewModel);

        // update todos on the first time and get them by viewmodel otherwise
        if(this.viewModel.getTodos() == null){
            updateTodos();
        }else{
            this.todoAdapter.update();
        }

        // setup recyclerview
        this.recyclerView = findViewById(R.id.itemList);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(todoAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(todoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void showSortMenu(View view){
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.activity_overview_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this::selectMenuItem);
        popup.show();
    }

    private boolean selectMenuItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.sortByName:
                this.viewModel.setSortMode(OverviewActivityViewModel.SORT_BY_NAME);
                break;
            case R.id.sortByDate:
                this.viewModel.setSortMode(OverviewActivityViewModel.SORT_BY_DATE);
                break;
            case R.id.sortByFavourite:
                this.viewModel.setSortMode(OverviewActivityViewModel.SORT_BY_FAVOURITE);
                break;
        }
        this.viewModel.sort();
        this.todoAdapter.update();
        return true;
    }

    private void createNewTodo() {
        Intent callDetailViewIntent = new Intent(this, DetailActivity.class);
        startActivity(callDetailViewIntent);
    }

    public void refresh(View view){
        updateTodos();
    }

    public void updateTodos(){
        operationRunner.run(
                () -> this.repository.readAll(),
                items -> {
                    this.viewModel.setTodos(items);
                    this.todoAdapter.update();
                }
        );
    }

    @Override
    protected void onStart() {
        if(this.repository != null){
            updateTodos();
        }
        super.onStart();
    }


}
