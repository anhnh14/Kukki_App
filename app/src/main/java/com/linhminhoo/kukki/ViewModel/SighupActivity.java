package com.linhminhoo.kukki.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linhminhoo.kukki.FunctionCommon.PacketItem;
import com.linhminhoo.kukki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linhminhoo on 6/24/2015.
 */
public class SighupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighup);
        Init();
    }


    EditText edt_name, edt_pass1, edt_pass2, edt_email;
    Button btn_sighup;
    private ProgressDialog progress;
    RequestQueue mRequestQueue=null;
    String email=null;
    String name=null;
    String pass1=null;
    String pass2=null;
    String error=null;
    String message=null;
    int time=0;
    JSONObject user_jsonObject=null;
    JSONArray user_jsonArray=null;

    private void Init() {

        edt_email= (EditText) findViewById(R.id.edt_sighup_email);
        edt_name= (EditText) findViewById(R.id.edt_sighup_name);
        edt_pass1= (EditText) findViewById(R.id.edt_sighup_pass1);
        edt_pass2= (EditText) findViewById(R.id.edt_sighup_pass2);
        btn_sighup= (Button) findViewById(R.id.btn_sighup_commit);

        mRequestQueue= Volley.newRequestQueue(SighupActivity.this);


        btn_sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edt_email.getText().toString();
                name=edt_name.getText().toString();
                pass1=edt_pass1.getText().toString();
                pass2=edt_pass2.getText().toString();
                if(email.trim().length()==0){
                  //  Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(getApplicationContext(), "Email không được bỏ trống hoặc để khoảng trắng, xin thử lại !", Toast.LENGTH_SHORT).show();
                    showDialog("Email không được bỏ trống hoặc để khoảng trắng, xin thử lại !");

                }else if(name.trim().toString().length()==0){
                   // Toast.makeText(getApplicationContext(), "Tên người dùng không được bỏ trống hoặc để khoảng trắng, xin thử lại !", Toast.LENGTH_SHORT).show();
                    showDialog("Tên người dùng không được bỏ trống hoặc để khoảng trắng, xin thử lại !");
                }else if(pass1.trim().length()==0){
                  //  Toast.makeText(getApplicationContext(), "Mật khẩu không được bỏ trống, xin thử lại !", Toast.LENGTH_SHORT).show();
                    showDialog("Mật khẩu không được bỏ trống, xin thử lại !");
                }else if(!pass1.equals(pass2)){
                    //Toast.makeText(getApplicationContext(), "Mật khẩu không khớp, xin thử lại !", Toast.LENGTH_SHORT).show();
                    showDialog("Mật khẩu không khớp, xin thử lại !");
                }else if(!validateEmailAddress(email)){
                    //Toast.makeText(getApplicationContext(), "Không đúng định dạng Email, xin thử lại !", Toast.LENGTH_SHORT).show();
                    showDialog("Không đúng định dạng Email, xin thử lại !");
                }else if(!checkLength(email, pass1, name)){
                    //Toast.makeText(getApplicationContext(), "Email, tên, mật khẩu không vượt quá 25 ký tự", Toast.LENGTH_SHORT).show();
                    showDialog("Email, tên, mật khẩu không vượt quá 25 ký tự");
                }else{
                  //  Toast.makeText(getApplicationContext(), PacketItem.code_register+"", Toast.LENGTH_LONG).show();
                    new RegisterUser().execute();
                }

            }
        });


    }
    public boolean checkLength(String email, String pass1, String name){
        boolean check=true;
        if(email.length()>25 || pass1.length()>25 || name.length()>25){
            check=false;
        }
        return check;
    }
    private boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    private class RegisterUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(SighupActivity.this);
            progress.setMessage("Xin chờ....");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, JSONArray> postParam= new HashMap<String, JSONArray>();
            ArrayList<String>arr_str=new ArrayList<String>();
            arr_str.add(name);
            arr_str.add(pass1);
            arr_str.add(email);
            JSONArray arr_json=new JSONArray(arr_str);
            postParam.put("data", arr_json);
           // postParam.put("data[]", pass1);
           // postParam.put("data[]", email);

/*
            postParam.put("username", name);
            postParam.put("password", pass1);
            postParam.put("email", email);
*/
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    PacketItem.url_register, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.d("Response", response.toString());
                            Log.e("Detail", response.toString());
                            try {
                                error=response.getString("error");
                               // user_jsonObject=response;
                                message=response.getString("message");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //       Log.d("Response.Error",  error.getMessage());
                    Log.e("Detail", "Error: "+error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put( "Authorization", PacketItem.code_register);
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(jsonObjReq);

            while(isNull(error)){
                SystemClock.sleep(500);
                time++;
                if(time>26){
                    time=0;
                    break;
                }
            }
            if(error!=null){
                if(error.equals("false")){
                    error=null;
                    message="Tạo tài khoản thành công, môt link kích hoạt được gửi tới email bạn đã đăng ký. \n Hãy kích hoạt tài khoản để bắt đầu tham gia Kukki ";
                }else{
                    error=null;
                    /*
                    try {
                        user_jsonArray=user_jsonObject.getJSONArray("message");
                        final String mes_detail=user_jsonArray.getString(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog(mes_detail);
                            }
                        });
                    } catch (Exception e) {
                      //  Log.e("Detail", "Error: "+e.toString());
                        message=e.toString();
                    }
                    */

                }
            }else{
                message="Không có phản hồi từ máy chủ, vui lòng thử lại sau !";
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.dismiss();
        showDialog(message);
      //  Toast.makeText(getApplicationContext(), message+"", Toast.LENGTH_LONG).show();
    }

    private boolean isNull(String temp){
        boolean check=false;
        if(temp==null){
            check=true;
        }else{
            check=false;
        }
        return check;
    }

}
    private void showDialog(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(SighupActivity.this).create();
        alertDialog.setTitle("Thông báo !");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
