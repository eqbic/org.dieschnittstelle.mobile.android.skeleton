package org.dieschnittstelle.mobile.android.skeleton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.Model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailBinding;
import org.dieschnittstelle.mobile.android.skeleton.util.AsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailActivityViewModel;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit2.http.PUT;

public class DetailActivity extends AppCompatActivity{

    private TextInputEditText dateText;
    private TextInputEditText timeText;

    private boolean isUpdate = false;

    private AsyncOperationRunner operationRunner;

    private ActivityDetailBinding binding;
    private DetailActivityViewModel viewModel;
    private ActivityResultLauncher<Intent> showContactsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    onContactSelected(result.getData());
                }
            }
    );

    MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
    private final MaterialDatePicker<Long> datePicker = materialDateBuilder.build();
    private boolean isDatePickerVisible = false;

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
        this.binding.setLifecycleOwner(this);
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
                if(!this.isDatePickerVisible){
                    showDatePicker();
                    this.isDatePickerVisible = true;
                }
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

    public void addContacts(View view){
        showContactsLauncher.launch(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
    }

    private long lastContactId = -1;
    @SuppressLint("Range")
    private void onContactSelected(Intent result) {
        Uri selectedContactUri = result.getData();
        Cursor cursor = getContentResolver().query(selectedContactUri, null, null, null);
        if(cursor.moveToFirst()){
            long internalContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            this.viewModel.getTodo().getContacts().add(String.valueOf(internalContactId));
            showContactDetails(internalContactId);
        }

    }

    public void showContactDetails(long contactId){
        lastContactId = contactId;
        if(hasContactPermission()){
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);
            if(cursor.moveToFirst()){
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i(null, "name: " + contactName);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(lastContactId != -1){
            showContactDetails(lastContactId);
        }
    }

    public boolean hasContactPermission(){
        int hasReadContactPermisson = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if(hasReadContactPermisson == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        return false;
    }

    private void showTimePicker() {
        int hour;
        int minute;
        if(this.isUpdate){
            hour = this.viewModel.getTodo().getTimeAsDate().getHours();
            minute = this.viewModel.getTodo().getTimeAsDate().getMinutes();
        }else{
            Calendar cal = Calendar.getInstance();
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
        }

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
        this.isDatePickerVisible = false;
    }

    private void showDatePicker(){
        datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    public void saveTodo(){
        if(!this.viewModel.validateForm()) return;
        operationRunner.run(
                () -> {
                    if(isUpdate){
                        return ((TodoApplication)getApplication()).getRepository().update(this.viewModel.getTodo());
                    }else{
                        return ((TodoApplication)getApplication()).getRepository().create(this.viewModel.getTodo());
                    }
                },result -> finish()
        );
    }

    public void deleteTodo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Todo");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            operationRunner.run(
                    () -> ((TodoApplication)getApplication()).getRepository().delete(this.viewModel.getTodo()), result -> finish()
            );
        });
        builder.setNegativeButton("Cancel", (dialog, which)->{
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}