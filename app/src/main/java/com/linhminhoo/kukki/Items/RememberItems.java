package com.linhminhoo.kukki.Items;

/**
 * Created by linhminhoo on 6/25/2015.
 */
public class RememberItems {
    String emai, password;
    int isRember;

    public RememberItems(String emai, String password, int isRember) {
        this.emai = emai;
        this.password = password;
        this.isRember = isRember;
    }

    public String getEmai() {
        return emai;
    }

    public void setEmai(String emai) {
        this.emai = emai;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsRember() {
        return isRember;
    }

    public void setIsRember(int isRember) {
        this.isRember = isRember;
    }
}
