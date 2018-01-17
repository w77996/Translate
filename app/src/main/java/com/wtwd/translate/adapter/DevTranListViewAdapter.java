package com.wtwd.translate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/16
 * Created by w77996
 */

public class DevTranListViewAdapter extends BaseAdapter {

    public List<RecorderBean> list = new ArrayList<>();
    public Context context;
    public LayoutInflater inflater;
    public DevTranListViewAdapter(Context context, List<RecorderBean> list){
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = list.get(position).getType();
        ViewHolder viewHolder;
       /* if (convertView == null)
        {*/

            viewHolder = new ViewHolder();
            if(type == Constants.ITEM_RIGHT){
                convertView = inflater.inflate(R.layout.chat_right, null);
                viewHolder.tv_recv_txt = (TextView) convertView.findViewById(R.id.tv_chat_recv);
                viewHolder.tv_recorder_txt = (TextView) convertView.findViewById(R.id.tv_chat_send);
                viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_chat_left_head);
                convertView.setTag(viewHolder);
            }else{
                convertView = inflater.inflate(R.layout.chat_left, null);
                viewHolder.tv_recv_txt = (TextView) convertView.findViewById(R.id.tv_chat_recv);
                viewHolder.tv_recorder_txt = (TextView) convertView.findViewById(R.id.tv_chat_send);
                viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_chat_left_head);
                convertView.setTag(viewHolder);
            }

       /* } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }*/
        viewHolder.tv_recorder_txt.setText("send");
        viewHolder.tv_recv_txt.setText("fasfdafdfdsafasdddddddddddddddddddddddddddfdsafasdfasdfasdfasdfasdfdasfasdfasdfasdfasdfdasfasdfdasfdasfdasfdasfdasfdasfasdfasdfdasfasdfdasfdasfdsfddd");
        // viewHolder.image.setImageResource(mData.get(position).get());
        return convertView;
    }

    public class ViewHolder
    {
        public TextView tv_recorder_txt;
        public ImageView img_head;
        public TextView tv_recv_txt;
    }
}
