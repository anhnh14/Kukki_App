package com.linhminhoo.kukki.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.linhminhoo.kukki.DbAdapter.DbAdapter;

import java.sql.SQLException;

/**
 * Created by linhminhoo on 6/25/2015.
 */
public class RememberModel {
    private SQLiteDatabase mdatabase;
    private Context context;
    private DbAdapter db;

    public RememberModel(Context mcontext){
        this.context=mcontext;
        db=new DbAdapter(context);
        try{
            openDB();
        }catch(Exception e){
            Log.e("Detail", "Error in open RememberModel: " + e.toString());
        }

    }
    public void openDB() throws SQLException {
        mdatabase=db.getWritableDatabase();
    }
    public void closeDB(){
        db.close();
    }
    public boolean isEmpty(){
        boolean check=true;
        String sql="select * from Remember";
        Cursor c=mdatabase.rawQuery(sql, null);
        if(c.getCount()>0){
            check=false;
        }
        c.close();
        return check;
    }
    public boolean insertRemember(String email, String password, int isRemember){
        boolean check=true;
        ContentValues content=new ContentValues();
        content.put("email", email);
        content.put("password", password);
        content.put("isRember", isRemember);

        long num=mdatabase.insert("Remember", null, content);
        if(num==-1){
            check=false;
        }
        return check;
    }
    public boolean isCLearRemember(){
        boolean check=true;
        //String sql="delete from User_Lesson";
        int num=mdatabase.delete("Remember",null,null);
        if(num==0){
            check=false;
        }
        return check;
    }
    public String getRememberEmail(){
        String sql="select * from Remember";
        String email="";
        Cursor c=mdatabase.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                email=c.getString(0);
            }while (c.moveToNext());
        }
        c.close();
        return email;
    }
    public String getRememberPassword(){
        String sql="select * from Remember";
        String pass="";
        Cursor c=mdatabase.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                pass=c.getString(1);
            }while (c.moveToNext());
        }
        c.close();
        return pass;
    }
    public int getRememberCount(){
        String sql="select * from Remember";
        Cursor c = mdatabase.rawQuery(sql, null);
        int count =c.getCount();
        c.close();
        return count;
    }
}
