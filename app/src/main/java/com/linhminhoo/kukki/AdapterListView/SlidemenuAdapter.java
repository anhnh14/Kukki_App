package com.linhminhoo.kukki.AdapterListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhminhoo.kukki.Items.SlidemenuItems;
import com.linhminhoo.kukki.R;

import java.util.ArrayList;

/**
 * Created by linhminhoo on 6/22/2015.
 */
public class SlidemenuAdapter extends ArrayAdapter<SlidemenuItems> {

    private Context context;
    private int Layout;
    ArrayList<SlidemenuItems> arr_slidemenu_item;
    public SlidemenuAdapter(Context context, int resource, ArrayList<SlidemenuItems> objects) {
        super(context, resource, objects);
        this.context=context;
        this.Layout=resource;
        arr_slidemenu_item=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.slidemenu_item,parent,false);

        TextView tv_name= (TextView) rowView.findViewById(R.id.tv_slidemenu_selected);
        ImageView img_icon= (ImageView) rowView.findViewById(R.id.img_slidemenu_icon);
        tv_name.setText(arr_slidemenu_item.get(position).getName());
        img_icon.setImageResource(arr_slidemenu_item.get(position).getIcon());
        return rowView;
    }
}
