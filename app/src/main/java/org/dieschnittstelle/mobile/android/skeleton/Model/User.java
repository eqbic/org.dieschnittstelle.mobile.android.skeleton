package org.dieschnittstelle.mobile.android.skeleton.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String pwd;

    public User(String email, String password){
        this.email = email;
        this.pwd = password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPwd(){
        return this.pwd;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }
}
