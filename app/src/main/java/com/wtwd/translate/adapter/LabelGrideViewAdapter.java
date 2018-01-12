package com.wtwd.translate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.SelectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/9
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */
public class LabelGrideViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelectBean> mData = new ArrayList<SelectBean>();
    private LayoutInflater mInflater;


    public LabelGrideViewAdapter(Context mContext,List<SelectBean> mData){
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.gv_label_item, null);
            viewHolder = new ViewHolder();
            viewHolder.gv_tv = (TextView) convertView.findViewById(R.id.rl_item_tv);
            viewHolder.gv_img = (ImageView) convertView.findViewById(R.id.rl_item_btn);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.gv_tv.setText(mData.get(position).getData());
       // viewHolder.image.setImageResource(mData.get(position).get());
        return convertView;

}
    public class ViewHolder
    {
        public TextView gv_tv;
        public ImageView gv_img;
    }

}
