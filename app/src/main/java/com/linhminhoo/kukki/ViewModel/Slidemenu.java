package com.linhminhoo.kukki.ViewModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.linhminhoo.kukki.AdapterListView.SlidemenuAdapter;
import com.linhminhoo.kukki.FunctionCommon.PacketItem;
import com.linhminhoo.kukki.ImageCrop.CropOption;
import com.linhminhoo.kukki.ImageCrop.CropOptionAdapter;
import com.linhminhoo.kukki.Items.SlidemenuItems;
import com.linhminhoo.kukki.Model.ImageNetwork.AppController;
import com.linhminhoo.kukki.Model.ImageNetwork.CustomVolleyRequestQueue;
import com.linhminhoo.kukki.Model.UserModel;
import com.linhminhoo.kukki.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linhminhoo on 6/22/2015.
 */
public class Slidemenu extends Fragment {

    ListView lv_slidemenu;
    ArrayList<SlidemenuItems> arr_slidemenu_item;
    private TypedArray navMenuIcons;
    BaseActivity baseParent;
    NetworkImageView mNetworkImageView ;
    private ImageLoader mImageLoader;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    String avatar_encode=null;

    UserModel usermodel;
    String error=null;
    String message=null;
    String allowImage=null;
    RequestQueue mRequestQueue=null;
    int time=0;
    private ProgressDialog progress;

    Bitmap photo=null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseParent=(MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.slidemenu, null);
        lv_slidemenu= (ListView) rootView.findViewById(R.id.lv_slidemenu);
        mNetworkImageView= (NetworkImageView) rootView.findViewById(R.id.profileAvatar);
//        mImageLoader = CustomVolleyRequestQueue.getInstance(getActivity())
//                .getImageLoader();
        mImageLoader= AppController.getInstance(getActivity()).getImageLoader();

        mRequestQueue= Volley.newRequestQueue(getActivity());

        arr_slidemenu_item=new ArrayList<SlidemenuItems>();
        navMenuIcons = getResources().obtainTypedArray(R.array.slidemenu_icon);
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(0, -1), "TRANG CHỦ"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(1, -1), "CÁ NHÂN"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(2, -1), "THÔNG BÁO"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(3, -1), "GHI CHÚ NGUYÊN LIỆU"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(4, -1), "LỊCH ĐẢM ĐANG"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(5, -1), "CÔNG THỨC TẢI VỀ"));
        arr_slidemenu_item.add(new SlidemenuItems(navMenuIcons.getResourceId(6, -1), "CÀI ĐẶT"));
        navMenuIcons.recycle();
        ArrayAdapter<SlidemenuItems> adapter_slidemenu=new SlidemenuAdapter(getActivity(), R.layout.slidemenu_item, arr_slidemenu_item);
        lv_slidemenu.setAdapter(adapter_slidemenu);
        adapter_slidemenu.notifyDataSetChanged();

        lv_slidemenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment newFragment =null;
                FragmentManager fm = getFragmentManager();
                switch (position){
                    case 0:
                   //     v.setBackgroundColor(Color.parseColor("#27ae60"));
                        newFragment =  MainView.newInstance(baseParent);
                        break;
                    case 1:
                        newFragment = new Item2();
                        break;
                    case 2:
                        newFragment = new Item3();
                        break;
                    /*
                    case 3:
                        newFragment = new Item4();
                        break;
                    case 4:
                        newFragment = new Item5();
                        break;
                        */
                }
                if (newFragment != null)
                    switchContent(newFragment);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                colorMenuRow(lv_slidemenu, position);
            }
        });
        //////
        //Builder
        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (getActivity(), android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();
        /*
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        });
        */
        UserModel userModel=new UserModel(getActivity());
        final String url = userModel.getAvatarUrl();
        mNetworkImageView.setImageUrl(url, mImageLoader);
        mNetworkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        return rootView;
    }
    private class UpdateAvatarUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Đang cập nhật ảnh đại diện...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            usermodel=new UserModel(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("img_url", avatar_encode);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    PacketItem.url_update_avatar+usermodel.getIdUser(), new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.d("Response", response.toString());
                    //        Log.e("Detail", response.toString());
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
                    headers.put( "Authorization", usermodel.getApiKeyUser());
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
                    usermodel.updateAvatar(usermodel.getIdUser(), message);
                    allowImage="yes";
                    message="Cập nhật ảnh đại diện thành công !";
                }else{
                    error=null;
                    allowImage="no";
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
                allowImage="no";
                message="Không có phản hồi từ máy chủ, vui lòng thử lại sau !";
            }
        //    Log.e("Detail", "allowImage: "+allowImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            usermodel.closeDB();
            progress.dismiss();
            if(allowImage.equalsIgnoreCase("yes")){
                saveImage(photo);
                mNetworkImageView.setImageBitmap(photo);
            }
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    photo = extras.getParcelable("data");
                    String img_encode=encodeTobase64(photo);
                    avatar_encode=img_encode;
                    new UpdateAvatarUser().execute();
                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }
    }
    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    private void switchContent(Fragment fragment){
        MainActivity ma = (MainActivity)getActivity();
        ma.switchContent(fragment);
    }
    private void colorMenuRow(ListView lv, int position)
    {
        // Changing all rows to default background color
        for (int i = 0; i < lv.getCount(); i++) {
            View listRow = (View) lv.getChildAt(i);
            if(listRow != null)
                listRow.setBackgroundColor(Color.parseColor("#ecf0f1"));
        }

        // Changing current row color
        View view = (View) lv.getChildAt(position);
        view.setBackgroundColor(Color.parseColor("#ffffff"));
    }
    private long getAvatarName(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        long avatar_name=0;
        try {
            Date dt_time=sdf.parse(strDate);
            avatar_name=dt_time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return avatar_name;
    }
    private void saveImage(Bitmap bm){
        try {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Kukki");
        myDir.mkdirs();
    //    Random generator = new Random();
    //    int n = 10000;
    //    n = generator.nextInt(n);
        String fname = getAvatarName()+ ".jpg";
        File file = new File(myDir, fname);
      //  Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();

            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
           // e.printStackTrace();
            Log.e("Detail", "Error in save Image: "+e.toString());
        }
    }

    public String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    private void showDialog(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
