package org.dieschnittstelle.mobile.android.skeleton;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailBinding;
import org.dieschnittstelle.mobile.android.skeleton.util.AsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.util.IRepository;
import org.dieschnittstelle.mobile.android.skeleton.util.WebRepository;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailActivityViewModel;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity{

    private TextInputEditText dateText;
    private TextInputEditText timeText;

    private boolean isUpdate = false;

    private AsyncOperationRunner operationRunner;

    private ActivityDetailBinding binding;
    private DetailActivityViewModel viewModel;

    MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
    private final MaterialDatePicker<Long> datePicker = materialDateBuilder.build();

    private final Calendar cal = Calendar.getInstance();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup viewmodel and register save method
        this.viewModel = new ViewModelProvider(this).get(DetailActivityViewModel.class);
        this.viewModel.getSavedOccured().observe(this, occured -> {
            saveTodo();
        });

        // setup data binding
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        this.binding.setViewmodel(this.viewModel);
        this.dateText = findViewById(R.id.detailDate);
        this.timeText = findViewById(R.id.detailTime);

        this.operationRunner = new AsyncOperationRunner(this, null);

        //        crud = new RoomCRUDOperations(this.getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if(this.viewModel.getTodo() == null){
            if(bundle != null){
                this.isUpdate = true;
                this.viewModel.setTodo(bundle.getSerializable("todo", ToDo.class));

            }else{
                this.isUpdate = false;
                this.viewModel.setTodo(new ToDo());
            }
        }

        // show date picker when user touches on date field
        this.dateText.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP){
                showDatePicker();
            }
            return false;
        });

        // set date of task when user confirms date selection
        this.datePicker.addOnPositiveButtonClickListener(this::selectDate);



        // show time picker when user touches on time field
        this.timeText.setOnTouchListener((v,event)->{
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showTimePicker();
            }
            return false;
        });
    }

    private void showTimePicker() {
        int hour = this.cal.get(Calendar.HOUR_OF_DAY);
        int minute = this.cal.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this, this::selectTime, hour, minute, true);
        timePicker.show();
    }

    private void selectTime(TimePicker picker, int hour, int minute){
        long timeInMilliseconds = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute);
        long expiry = this.viewModel.getTodo().getDateInMilliseconds() + timeInMilliseconds;
        this.viewModel.getTodo().setExpiry(expiry);
        this.timeText.setText(this.viewModel.getTodo().getTime());
    }

    private void selectDate(long dateInMilliseconds) {
        long expiry = dateInMilliseconds + this.viewModel.getTodo().getTimeInMilliseconds();
        this.viewModel.getTodo().setExpiry(expiry);
        this.dateText.setText(this.viewModel.getTodo().getDate());
    }

    private void showDatePicker(){
        datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    public void saveTodo(){
        operationRunner.run(
                () -> {
                    if(isUpdate){
                        return OverviewActivity.getRepository().update(this.viewModel.getTodo());
                    }else{
                        return OverviewActivity.getRepository().create(this.viewModel.getTodo());
                    }
                },result -> finish()
        );
    }
}