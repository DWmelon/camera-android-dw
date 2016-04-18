package com.seriousface.m.myapplication.adapter;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.seriousface.m.myapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class GridViewAdapter extends BaseAdapter {

    Context context;
    int[] resIds = {R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh,R.mipmap.laugh};

    public GridViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public Object getItem(int position) {
        return resIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridview_face,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_grid_item);
            imageView.setImageResource((int)getItem(position));
        }
        return convertView;
    }
}
