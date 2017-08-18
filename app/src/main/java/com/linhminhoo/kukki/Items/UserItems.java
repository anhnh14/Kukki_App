package com.linhminhoo.kukki.Items;

/**
 * Created by linhminhoo on 6/25/2015.
 */
public class UserItems {
    int id;
    String name, email, password, api_key;

    public UserItems(int id, String name, String email, String password, String api_key) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.api_key = api_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
