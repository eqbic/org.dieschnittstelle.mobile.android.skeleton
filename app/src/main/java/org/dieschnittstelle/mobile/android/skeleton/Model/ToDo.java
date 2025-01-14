package org.dieschnittstelle.mobile.android.skeleton.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;

import org.dieschnittstelle.mobile.android.skeleton.util.ListConverters;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@Entity(tableName = "todos")
public class ToDo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private long expiry;
    @Expose
    private boolean done;
    @Expose
    private boolean favourite;

    @Expose
    @TypeConverters(ListConverters.class)
    private List<String> contacts;

    @Ignore
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    @Ignore
    SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Ignore
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public String getDate(){
        Date date = new Date(this.expiry);
        return dateFormat.format(date);
    }

    public String getTime(){
        Date date = new Date(this.expiry);
        return timeFormat.format(date);
    }

    public Date getTimeAsDate(){
        return new Date(this.expiry);
    }

    public long getDateInMilliseconds(){
        try{
            String dateString = parseFormat.format(new Date(this.expiry));
            Date date = parseFormat.parse(dateString);
            return date.getTime();
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public long getTimeInMilliseconds(){
        try{
            String timeString = timeFormat.format(new Date(this.expiry));
            Date date = timeFormat.parse(timeString);
            return date.getTime();
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }

    public ToDo(){
        TimeZone tz = TimeZone.getDefault();
        this.dateFormat.setTimeZone(tz);
        this.dateFormat.setTimeZone(tz);
        this.parseFormat.setTimeZone(tz);
        this.description = "";
        Calendar cal = Calendar.getInstance();
        this.expiry = cal.getTimeInMillis();
    }

    public ToDo(String name){
        super();
        this.name = name;
        this.done = false;

    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getDone() {
        return this.done;
    }

    public void setDone(boolean status) {
        this.done = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo taskModel = (ToDo) o;
        return id == taskModel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }


    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public List<String> getContacts() {
        if(contacts == null){
            contacts = new ArrayList<>();
        }
        return contacts;
    }

    public void setContacts(List<String> contactIds) {
        this.contacts = contactIds;
    }
}
