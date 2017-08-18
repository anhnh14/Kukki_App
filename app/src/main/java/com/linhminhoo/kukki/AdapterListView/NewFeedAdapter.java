package com.linhminhoo.kukki.AdapterListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.linhminhoo.kukki.Items.NewFeedItems;
import com.linhminhoo.kukki.Model.ImageNetwork.AppController;
import com.linhminhoo.kukki.Model.ImageNetwork.FeedImageView;
import com.linhminhoo.kukki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhminhoo on 7/6/2015.
 */
public class NewFeedAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<NewFeedItems> feedItems;
    ImageLoader imageLoader = AppController.getInstance(activity).getImageLoader();
    public NewFeedAdapter(Activity context, ArrayList<NewFeedItems> objects) {
        this.activity=context;
        feedItems=objects;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.new_feed, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance(activity).getImageLoader();

        TextView title= (TextView) convertView.findViewById(R.id.tv_newfeed_title);
        TextView artist= (TextView) convertView.findViewById(R.id.tv_newfeed_artist);
        TextView time_finish= (TextView) convertView.findViewById(R.id.tv_newfeed_time);
        TextView total_like= (TextView) convertView.findViewById(R.id.tv_newfeed_total_like);
        TextView total_comment= (TextView) convertView.findViewById(R.id.tv_newfeed_total_comment);
        TextView total_view= (TextView) convertView.findViewById(R.id.tv_newfeed_total_view);
        TextView total_kcal= (TextView) convertView.findViewById(R.id.tv_newfeed_kcal);
        ImageView img_like= (ImageView) convertView.findViewById(R.id.newfeed_img_like);

        FeedImageView feedImageView= (FeedImageView) convertView.findViewById(R.id.feedImage1);

        final NewFeedItems items=feedItems.get(position);

        title.setText(items.getTitle());
        artist.setText(items.getArtist());
        if(items.getTime_finish()==null){
            time_finish.setText("Chưa cập nhật");
        }else{
            time_finish.setText(items.getTime_finish()+" phút");
        }
        total_like.setText(items.getTotal_like());
        total_comment.setText(items.getTotal_comment());
        total_view.setText(items.getTotal_view());
        if(items.getTotal_kcal()==null){
            total_kcal.setText("Chưa cập nhật");
        }else{
            total_kcal.setText(items.getTotal_kcal()+" kcal");
        }
        feedImageView.setImageUrl(items.getFeedImage(), imageLoader);
        feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                    @Override
                    public void onError() {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "like "+items.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
