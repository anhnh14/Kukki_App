package com.linhminhoo.kukki.ViewModel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.linhminhoo.kukki.AdapterListView.NewFeedAdapter;
import com.linhminhoo.kukki.FunctionCommon.PacketItem;
import com.linhminhoo.kukki.Items.NewFeedItems;
import com.linhminhoo.kukki.Model.ImageNetwork.AppController;
import com.linhminhoo.kukki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linhminhoo on 7/6/2015.
 */
public class Home_fragment_all extends Fragment {

    ArrayList<NewFeedItems>arr_items;
    NewFeedAdapter feedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container,false);

        arr_items=new ArrayList<NewFeedItems>();
        feedAdapter=new NewFeedAdapter(getActivity(), arr_items);

        ListView lv_newfeed= (ListView) view.findViewById(R.id.lv_newfeed);
        lv_newfeed.setAdapter(feedAdapter);

        Cache cache = AppController.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(PacketItem.url_newFeed_all+"/1");

        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            /*
            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("limit", "0|5");
            */
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    PacketItem.url_newFeed_all+"/1", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //   VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        Log.e("Detail", "Response: " + response.toString());
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                   // VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Log.e("Detail", "error in get response: "+error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance(getActivity()).addToRequestQueue(jsonReq);
        }

        lv_newfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), arr_items.get(position).getId()+"", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void parseJsonFeed(JSONObject response) {
        Log.e("Detail", "ParseJson");
        try {
            JSONArray feedArray = response.getJSONArray("message");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                NewFeedItems item = new NewFeedItems();
                item.setId(feedObj.getInt("id"));
                item.setTitle(feedObj.getString("title"));
                item.setArtist(feedObj.getString("name"));
                item.setTime_finish(feedObj.getString("total_time_finish"));
                item.setTotal_comment(feedObj.getString("total_comment"));
                item.setTotal_like(feedObj.getString("total_like"));
                item.setTotal_kcal(feedObj.getString("total_kcal"));
                item.setTotal_view(feedObj.getString("total_view"));
                item.setFeedImage(feedObj.getString("img_api_url"));

                // Image might be null sometimes
                /*
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                Log.e("Detail", "ProfilePic Url: " + item.getProfilePic());
                item.setTimeStamp(feedObj.getString("timeStamp"));
                */

                // url might be null sometimes
            //    String feedUrl = feedObj.isNull("url") ? null : feedObj
            //            .getString("url");
           //     item.setUrl(feedUrl);
                arr_items.add(item);
                //feedItems.add(item);
            }

            // notify data changes to list adapater
            feedAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
           // e.printStackTrace();
            Log.e("Detail", "Loi get du lieu: "+e.getMessage());
        }
    }
}
