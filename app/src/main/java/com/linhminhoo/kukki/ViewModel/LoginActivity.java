package com.linhminhoo.kukki.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linhminhoo.kukki.FunctionCommon.PacketItem;
import com.linhminhoo.kukki.Model.RememberModel;
import com.linhminhoo.kukki.Model.UserModel;
import com.linhminhoo.kukki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linhminhoo on 6/24/2015.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
    }

    EditText edt_email, edt_pass;
    TextView tv_sighup_link;
    Button btn_login;
    CheckBox cb_remember;
    private ProgressDialog progress;
    RequestQueue mRequestQueue=null;
    int time=0;
    JSONObject user_jsonObject=null;
    JSONArray user_jsonArray=null;
    String email=null;
    String pass=null;
    String error=null;
    String message=null;
    UserModel userModel;
    RememberModel rememberModel;

    private void Init() {

        edt_email= (EditText) findViewById(R.id.edt_login_email);
        edt_pass= (EditText) findViewById(R.id.edt_login_pass);
        tv_sighup_link= (TextView) findViewById(R.id.tv_sighup_link);
        btn_login= (Button) findViewById(R.id.btn_login_commit);
        cb_remember= (CheckBox) findViewById(R.id.cb_remember);

        mRequestQueue= Volley.newRequestQueue(LoginActivity.this);


        //Init remmember Username


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edt_email.getText().toString();
                pass=edt_pass.getText().toString();
                if(email.trim().length()==0){
                    showDialog("Email không được để khoảng trắng hoặc bỏ trống !");
                }else if(pass.trim().length()==0){
                    showDialog("Mật khẩu không được để khoảng trắng hoặc bỏ trống !");
                }else if(!validateEmailAddress(email)){
                    showDialog("Không đúng định dạng Email, xin thử lại !");
                }else {
                    new LoginUser().execute();
                 //   Intent myItent=new Intent(LoginActivity.this, MainActivity.class);
                 //   startActivity(myItent);
                }
            }
        });

        tv_sighup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myItent=new Intent(LoginActivity.this, SighupActivity.class);
                startActivity(myItent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel=new UserModel(this);
        rememberModel=new RememberModel(this);
        if(!rememberModel.isEmpty()){
            edt_email.setText(rememberModel.getRememberEmail());
            edt_pass.setText(rememberModel.getRememberPassword());
            cb_remember.setChecked(true);
        }else{
            edt_pass.setText("");
            edt_email.setText("");
            cb_remember.setChecked(false);
        }
    }

    private class LoginUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Xin chờ....");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("email", email);
            postParam.put("password", pass);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    PacketItem.url_login, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.d("Response", response.toString());
                      //      Log.e("Detail", response.toString());
                            try {
                                error=response.getString("error");

                                user_jsonObject=response;
                                message=response.getString("message");

                            } catch (JSONException e) {
                               // e.printStackTrace();
                                Log.e("Detail", "Error in catch response: "+e.toString());
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //       Log.d("Response.Error",  error.getMessage());
                    Log.e("Detail", "Error in Volley: "+error.getMessage());
                }
            }) {
                /*
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put( "Authorization", PacketItem.code_register);
                    return headers;
                }
                */
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
                    try {
                        user_jsonArray = user_jsonObject.getJSONArray("message");
                        message="success";
                        int id=user_jsonArray.getInt(0);
                        String name=user_jsonArray.getString(1);
                        String email2=user_jsonArray.getString(2);
                        String api_key=user_jsonArray.getString(3);
                        String avatar_url=user_jsonArray.getString(4);
                        if(userModel.isEmpty()){
                            userModel.insertUser(id, name, email2, api_key, avatar_url);
                        }else{
                            userModel.isCLearUser();
                            userModel.insertUser(id, name, email2, api_key, avatar_url);
                        }
                        if(cb_remember.isChecked()){
                           // Log.e("Detail", "Starting Insert Remember !");
                            if(rememberModel.isEmpty()){
                                rememberModel.insertRemember(email, pass, 1);
                            //    Log.e("Detail", "Remember Count: "+rememberModel.getRememberCount()+"");
                            //    Log.e("Detail", "Inserted !");
                            }else{
                                if(rememberModel.isCLearRemember()){
                           //         Log.e("Detail", "Clear and Inserted !");
                                    rememberModel.insertRemember(email, pass, 1);
                                }

                            }
                        }else{
                         //   Log.e("Detail", "Not checked and Clear");
                            rememberModel.isCLearRemember();
                        }


                    }catch (Exception ex){
                        message="no success";
                        Log.e("Detail", "Error in get User JsonArray: "+ex.toString());
                    }
                 //   message="Tạo tài khoản thành công, môt link kích hoạt được gửi tới email bạn đã đăng ký. \n Hãy kích hoạt tài khoản để bắt đầu tham gia Kukki ";
                }else{
                    error=null;

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

            if(message.equals("success")){
                userModel.closeDB();
                rememberModel.closeDB();
                   Intent myItent=new Intent(LoginActivity.this, MainActivity.class);
                   myItent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(myItent);
                finish();
            }else{
                showDialog(message);
            }
            userModel.closeDB();
            rememberModel.closeDB();
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
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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
    private boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }


}
