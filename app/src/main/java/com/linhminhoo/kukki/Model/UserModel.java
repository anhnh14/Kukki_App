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
public class UserModel {
    private SQLiteDatabase mdatabase;
    private Context context;
    private DbAdapter db;

    public UserModel(Context mcontext){
        this.context=mcontext;
        db=new DbAdapter(context);
        try{
            openDB();
        }catch(Exception e){
            Log.e("Detail", "Error in open UserModel: "+e.toString());
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
        String sql="select * from User";
        Cursor c=mdatabase.rawQuery(sql, null);
        if(c.getCount()>0){
            check=false;
        }
        c.close();
        return check;
    }
    public boolean insertUser(int id, String name, String email, String api_key, String avatar_url){
        boolean check=true;
        ContentValues content=new ContentValues();
        content.put("id", id);
        content.put("name", name);
        content.put("email", email);
        content.put("api_key", api_key);
        content.put("avatar_url", avatar_url);

        long num=mdatabase.insert("User", null, content);
        if(num==-1){
            check=false;
        }
        return check;
    }
    public boolean updateUser(int id, String name, String email){
        String where="id="+id;
        ContentValues content=new ContentValues();
        //scontent.put("seri", seri);
        content.put("name", name);
        content.put("email", email);
        return mdatabase.update("User", content, where, null)!=0;
    }
    public boolean updateAvatar(int id, String url){
        String where="id="+id;
        ContentValues content=new ContentValues();
        //scontent.put("seri", seri);
        content.put("avatar_url", url);
        return mdatabase.update("User", content, where, null)!=0;
    }

    public boolean isCLearUser(){
        boolean check=true;
        //String sql="delete from User_Lesson";
        int num=mdatabase.delete("User",null,null);
        if(num==0){
            check=false;
        }
        return check;
    }
    public String getAvatarUrl(){
        String sql="select avatar_url from User";
        String url="";
        Cursor c=mdatabase.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                url=c.getString(0);
            }while (c.moveToNext());
        }
        c.close();
        return url;
    }
    public String getApiKeyUser(){
        String sql="select api_key from User";
        String api_key="";
        Cursor c=mdatabase.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                api_key=c.getString(0);
            }while (c.moveToNext());
        }
        c.close();
        return api_key;
    }
    public int getIdUser(){
        String sql="select id from User";
        int id=0;
        Cursor c=mdatabase.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }
        c.close();
        return id;
    }

}
