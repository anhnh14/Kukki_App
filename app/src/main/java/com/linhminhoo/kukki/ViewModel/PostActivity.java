package com.linhminhoo.kukki.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.linhminhoo.kukki.Model.UserModel;
import com.linhminhoo.kukki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhminhoo on 6/26/2015.
 */
public class PostActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Init();
    }

    Spinner spinner_style, spinner_area;
    Button btn_add_m, btn_add_step;
    ImageView img_cover, img_post_ok, post_back;
    EditText edt_title, edt_description, edt_time_finish, edt_kcal;

    String img_cover_encode=null;

    private static final int SELECT_PICTURE = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_IMG_COVER = 2;
    private ImageView mImageView_2;
    int count=0;
    boolean isImageSet=false; // xac dinh lieu image step co dc set img hay ko
    boolean isCoverImg=true; // true = imgCover; false= imgStep
    boolean isDefaultImgCover=true;

    RequestQueue mRequestQueue=null;
    private ProgressDialog progress;

    JSONObject obj_special=null;
    JSONArray arr_special_continent=null;
    JSONArray arr_special_area=null;


    ArrayList<ImageView>arr_view=new ArrayList<ImageView>();
    ArrayList<EditText>arr_step_des=new ArrayList<EditText>();
    ArrayList<String>arr_img_step_encode=new ArrayList<String>();
    int index=0;

    UserModel userModel;
    String error=null;
    String message=null;
    int time=0;

    String arr_spinner_style[];
    String arr_spinner_area[];
    ArrayAdapter<String> adapter_spinner_style;
    ArrayAdapter<String> adapter_spinner_area;

    String mix_img_step_encode=null;
    String mix_step_description=null;

    String receipt_title=null;
    String receipt_des=null;
    String receipt_time_finish=null;
    String receipt_kcal=null;
    String receipt_continent=null;
    String receipt_are=null;
    ArrayList<String>arr_receipt_raw=new ArrayList<String>();
    ArrayList<String>arr_receipt_img_encode=new ArrayList<String>();
    ArrayList<String>arr_receipt_step_des=new ArrayList<String>();
    ArrayList<String>arr_data=new ArrayList<String>();

    int step=1;
    private void Init() {

        spinner_area= (Spinner) findViewById(R.id.spinner_area);
        spinner_style= (Spinner) findViewById(R.id.spinner_style);
        btn_add_m= (Button) findViewById(R.id.btn_add_m);
        btn_add_step= (Button) findViewById(R.id.btn_post_add_step);
        img_cover= (ImageView) findViewById(R.id.post_img_recipe_cover);
        img_post_ok= (ImageView) findViewById(R.id.img_post_ok);
        edt_title= (EditText) findViewById(R.id.edt_title);
        edt_description= (EditText) findViewById(R.id.edt_description);
        edt_time_finish= (EditText) findViewById(R.id.edt_time_finish);
        edt_kcal= (EditText) findViewById(R.id.edt_kcal);
        post_back= (ImageView) findViewById(R.id.img_post_back);

        mRequestQueue= Volley.newRequestQueue(PostActivity.this);

        new GetSpeciality().execute();

        /// Init Spinner
        /*
        String arr_spinner_style[]=new String[arr_special_continent.length()];
        String arr_spinner_area[]=new String[arr_special_area.length()];
        for(int i=0;i<arr_special_continent.length();i++){
            try {
                arr_spinner_style[i]=arr_special_continent.getString(i);
            } catch (JSONException e) {
                Log.e("Detail", "Error in put value to spinner continent: "+e.toString());
            }
        }

*/

        post_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner_style.setOnItemSelectedListener(new SpinnerStyleEvent());
        spinner_area.setOnItemSelectedListener(new SpinnerAreaEvent());

        ////// End Init Spinner


        // Init Add row Nguyen Lieu
        final ArrayList<EditText> arr_edt_raw=new ArrayList<EditText>();

        btn_add_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater vi= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout=(LinearLayout)findViewById(R.id.content_3);
                final View view=vi.inflate(R.layout.child_edittext, null);
                linearLayout.addView(view);
                arr_edt_raw.add((EditText) view.findViewById(R.id.edt_raw));
                Button btn_remove= (Button) view.findViewById(R.id.btn_remove_m);
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<arr_edt_raw.size();i++){
                            if(arr_edt_raw.get(i)==view.findViewById(R.id.edt_raw)){
                                arr_edt_raw.remove(i);
                            }
                        }
                        view.setVisibility(View.GONE);
                    }
                });
            }
        });
        // End - Init Add row Nguyen Lieu

        // Init Add step
        btn_add_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(step>5){
                    showDialog_2("Bạn chỉ có thể thêm tối đa 5 bước !");
                }else {

                    if(step==1){
                        showDialog_2("Bạn có thể thêm tối đa 5 bước !");
                    }

                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout layout = (LinearLayout) findViewById(R.id.content_1);
                    View view = vi.inflate(R.layout.child_step, null);

                    if (count == 0) {
                        final ImageView mImageView;
                        layout.addView(view);
                        count++;
                        TextView tv_step = (TextView) view.findViewById(R.id.tv_step);
                        tv_step.setText("Bước " + step);
                        mImageView = (ImageView) view.findViewById(R.id.img_step);
                        EditText edt_step_description = (EditText) view.findViewById(R.id.edt_step_description);
                        arr_view.add(mImageView);                 // add img to arr
                        arr_step_des.add(edt_step_description);   // add edt to arr
                        if (arr_view.size() != 0) {
                            index = arr_view.size() - 1;
                            arr_img_step_encode.add("");
                        } else {

                            index = 0;
                            arr_img_step_encode.add("");
                        }

                        mImageView.setTag(index);
                        Log.e("Detail", "size arr_view: " + arr_view.size());
                        Log.e("Detail", "Array newest: " + arr_view.get(index).toString());
                        Log.e("Detail", "Image Tag: " + mImageView.getTag());
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isCoverImg = false;
                                for (int i = 0; i < arr_view.size(); i++) {
                                    if (mImageView.getTag() == i) {
                                        arr_view.get(i).setBackgroundResource(0);
                                        selectImage(arr_view.get(i));
                                        mImageView_2 = arr_view.get(i);
                                    }
                                }
                            }
                        });
                        //  arr_img.add(mImageView);
                    } else {
                        if (isImageSet) {
                            final ImageView mImageView;
                            layout.addView(view);
                            TextView tv_step = (TextView) view.findViewById(R.id.tv_step);
                            tv_step.setText("Bước " + step);
                            mImageView = (ImageView) view.findViewById(R.id.img_step);
                            EditText edt_step_description = (EditText) view.findViewById(R.id.edt_step_description);
                            //       arr_img.add(mImageView);
                            isImageSet = false;
                            arr_view.add(mImageView);                     // add img to arr
                            arr_step_des.add(edt_step_description);       // add edt to arr
                            if (arr_view.size() != 0) {
                                index = arr_view.size() - 1;
                                arr_img_step_encode.add("");
                            } else {
                                index = 0;
                                arr_img_step_encode.add("");
                            }
                            mImageView.setTag(index);
                            mImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isCoverImg = false;
                                    for (int i = 0; i < arr_view.size(); i++) {
                                        if (mImageView.getTag() == i) {
                                            arr_view.get(i).setBackgroundResource(0);
                                            selectImage(arr_view.get(i));
                                            mImageView_2 = arr_view.get(i);
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please choose image for the step above before add new step", Toast.LENGTH_SHORT).show();
                        }
                    }
                    step++;
                }
            }
        });


        img_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCoverImg=true;
                img_cover.setBackgroundResource(0);
                selectImage(img_cover);
            }
        });

        img_post_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel=new UserModel(PostActivity.this);
                receipt_title=edt_title.getText().toString().trim();
                receipt_des=edt_description.getText().toString().trim();
                receipt_time_finish=edt_time_finish.getText().toString().trim();
                receipt_kcal=edt_kcal.getText().toString().trim();
                receipt_continent=spinner_style.getSelectedItem().toString();
                receipt_are=spinner_area.getSelectedItem().toString();
                arr_receipt_raw.clear();
                arr_receipt_img_encode.clear();
                arr_receipt_step_des.clear();
                arr_data.clear();
                for(int i=0;i<arr_edt_raw.size();i++){
                    arr_receipt_raw.add(arr_edt_raw.get(i).getText().toString().trim());
                }
                for(int i=0;i<arr_img_step_encode.size();i++){
                    arr_receipt_img_encode.add(arr_img_step_encode.get(i));
                }
                for(int i=0;i<arr_step_des.size();i++){
                    arr_receipt_step_des.add(arr_step_des.get(i).getText().toString().trim());
                }
                if(isEmpty(receipt_title) || receipt_title.length()>25){
                    showDialog_2("Tên món ăn không được bỏ trống và không quá 25 ký tự !");
                }else if(isEmpty(receipt_des) || receipt_des.length()>255){
                    showDialog_2("Miêu tả món ăn không được bỏ trống và không quá 255 ký tự !");
                }else if(arr_edt_raw.size()==0){
                    showDialog_2("Bạn chưa thêm nguyên liệu, hãy kiểm tra lại !");
                }else if(arr_step_des.size()==0){
                    showDialog_2("Bạn chưa thêm các bước làm món ăn, hãy kiểm tra lại !");
                }else if(isEmptyArrayIndex(arr_receipt_raw)){
                    showDialog_2("Nguyên liệu không được bỏ trống và mỗi nguyên liệu không quá 255 ký tự !");
                }else if(isEmptyArrayIndex(arr_receipt_step_des)){
                    showDialog_2("Miêu tả cho mỗi bước không được bỏ trống và không vược quá 255 ký tự !");
                }else if(isDefaultImgCover){
                    showDialog_2("Bạn chưa chọn ảnh minh họa cho món ăn !");
                }else{
                    arr_data.add(userModel.getIdUser()+"");
                    arr_data.add(receipt_title);
                    arr_data.add(receipt_des);
                    arr_data.add(receipt_time_finish);
                    arr_data.add(receipt_kcal);
                    arr_data.add(img_cover_encode);
                    arr_data.add(receipt_continent);
                    arr_data.add(receipt_are);
                    new UploadPost().execute();
                //    Log.e("Detail", "xxx: "+arr_data.size()+"/"+arr_receipt_raw.size());
                  //  showDialog_2("OKK");
                }
             //   Log.e("Detail", "arr receipt  size: "+arr_edt_raw.size()+"/"+arr_receipt_raw.size());

/*
                String material="";
                for(int i=0;i<arr_edt_raw.size();i++){
                    material+=arr_edt_raw.get(i).getText().toString()+" || ";
                }

                for(int i=0;i<arr_step_des.size();i++){
                    mix_step_description+=arr_step_des.get(i).getText().toString()+" |description| ";
                }


                arr_view.get(0).buildDrawingCache();
                Bitmap bitmap=arr_view.get(0).getDrawingCache();
                String encode=encodeTobase64(bitmap);
                Log.e("Detail", "img encode: "+encode);


                for(int i=0;i<arr_img_step_encode.size();i++){
                    String temp=arr_img_step_encode.get(i);
                    mix_img_step_encode=mix_img_step_encode+temp+"  "+arr_step_des.get(i);
                    Log.e("Detail", mix_img_step_encode);
                }
                 */
             //   userModel.closeDB();
            }
        });

    }

    private class GetSpeciality extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            userModel=new UserModel(PostActivity.this);
            progress = new ProgressDialog(PostActivity.this);
            progress.setMessage("Đang tải dữ liệu...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("id", userModel.getIdUser()+"");
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    PacketItem.url_get_speciality+userModel.getIdUser(), new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                    //        Log.e("Detail", response.toString());
                            try {
                                obj_special=response.getJSONObject("message");
                                error=response.getString("error");

                            } catch (JSONException e) {
                                Log.e("Detail", "Error in get Response: "+e.toString());
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
                    headers.put( "Authorization", userModel.getApiKeyUser());
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
                    try {
                        arr_special_continent = obj_special.getJSONArray("continent");
                        arr_special_area = obj_special.getJSONArray("area");
                    }catch (Exception ex){
                        message=ex.toString();
                    }
                    message=null;
                }else{
                    error=null;

                }
                message=null;
            }else{
                message="Không có phản hồi từ máy chủ, vui lòng thử lại sau !";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        //    userModel.closeDB();
            progress.dismiss();
            if(message!=null){
                showDialog(message);
            }else{
                arr_spinner_style=new String[arr_special_continent.length()];
                arr_spinner_area=new String[arr_special_area.length()];
                adapter_spinner_style=new ArrayAdapter<String>
                        (
                                PostActivity.this,
                                android.R.layout.simple_spinner_item,
                                arr_spinner_style
                        );
                adapter_spinner_area=new ArrayAdapter<String>
                        (
                                PostActivity.this,
                                android.R.layout.simple_spinner_item,
                                arr_spinner_area
                        );

                adapter_spinner_style.setDropDownViewResource
                        (android.R.layout.simple_list_item_single_choice);
                adapter_spinner_area.setDropDownViewResource
                        (android.R.layout.simple_list_item_single_choice);
                spinner_style.setAdapter(adapter_spinner_style);
                spinner_area.setAdapter(adapter_spinner_area);

                for(int i=0;i<arr_special_continent.length();i++){
                    try {
                        arr_spinner_style[i]=arr_special_continent.getString(i);
                    } catch (JSONException e) {
                        Log.e("Detail", "Error in put value to spinner continent: "+e.toString());
                    }
                }
                for(int i=0;i<arr_special_area.length();i++){
                    try {
                        arr_spinner_area[i]=arr_special_area.getString(i);
                    } catch (JSONException e) {
                        Log.e("Detail", "Error in put value to spinner area: "+e.toString());
                    }
                }
                adapter_spinner_area.notifyDataSetChanged();
                adapter_spinner_style.notifyDataSetChanged();
            }
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

    private class UploadPost extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(PostActivity.this);
            progress.setMessage("Xin chờ....");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            Map<String, JSONArray> postParam= new HashMap<String, JSONArray>();
            JSONArray arr_json=new JSONArray(arr_data);
            JSONArray arr_raw=new JSONArray(arr_receipt_raw);
            JSONArray arr_json_img=new JSONArray(arr_receipt_img_encode);
            JSONArray arr_json_des=new JSONArray(arr_receipt_step_des);
            postParam.put("data", arr_json);
            postParam.put("raw", arr_raw);
            postParam.put("image", arr_json_img);
            postParam.put("des", arr_json_des);
           // postParam.put("data", arr_data);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    PacketItem.url_create_receipt, new JSONObject(postParam),
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
                             //   e.printStackTrace();
                                Log.e("Detail", "error in response: "+e.toString());
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
                    headers.put( "Authorization", userModel.getApiKeyUser());
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
                    message="success";
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
            userModel.closeDB();
            progress.dismiss();
            showDialog(message);
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
        AlertDialog alertDialog = new AlertDialog.Builder(PostActivity.this).create();
        alertDialog.setTitle("Thông báo !");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void showDialog_2(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(PostActivity.this).create();
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

    private void selectImage(final ImageView mImageView) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_PICTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    if(isCoverImg){
                        mImageView.setBackgroundResource(R.drawable.img_recipe_cover_3);
                    }else{
                        mImageView.setBackgroundResource(R.drawable.img_recipe_cover_4);
                    }

                    if(isImageSet==true){
                        isImageSet=true;
                    }else{
                        isImageSet=false;
                    }
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    btmapOptions.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

             //       arr_img_step_encode.add(encodeTobase64(bm)); // adddddddddddd

                    if(isCoverImg==false){
                        mImageView_2.setImageBitmap(bm);
                        arr_img_step_encode.set(index, encodeTobase64(bm));
                        isImageSet=true;
                    }else{
                        img_cover_encode=encodeTobase64(bm);
                        img_cover.setImageBitmap(bm);

                        isDefaultImgCover=false;
                    }

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "test";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    fOut = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, PostActivity.this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                btmapOptions.inSampleSize = 2;
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
           //     arr_img_step_encode.add(encodeTobase64(bm)); // adddddddddddd
           //    arr_img_step_encode.set

                if(isCoverImg==false){
                    mImageView_2.setImageBitmap(bm);
                    arr_img_step_encode.set(index, encodeTobase64(bm));
                    isImageSet=true;
                }else{
                    img_cover_encode=encodeTobase64(bm);
                    img_cover.setImageBitmap(bm);
                    isDefaultImgCover=false;
                }
            }
        }
    }
    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private class SpinnerStyleEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class SpinnerAreaEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    public boolean isEmpty(String temp){
        boolean check=false;
        if(temp==null || temp.equalsIgnoreCase("")){
            check=true;
        }
        return check;
    }
    public boolean isEmptyArrayIndex(ArrayList arr){
        boolean check=false;
        for(int i=0;i<arr.size();i++){
            if(isEmpty(arr.get(i).toString()) || arr.get(i).toString().length()>255){
                check=true;
                break;
            }
        }
        return check;
    }
}
