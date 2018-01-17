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
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/16
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */

public class LanguageSelectAdapter extends BaseAdapter {

    public List<SelectBean> list = new ArrayList<>();
    public Context context;
    public LayoutInflater inflater;
    public LanguageSelectAdapter(Context context, List<SelectBean> list){
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

        String type = list.get(position).getData();
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.language_select_item, null);
            viewHolder.language_select_item_text = (TextView) convertView.findViewById(R.id.language_select_item_text);
            viewHolder.language_select_item_select = (ImageView) convertView.findViewById(R.id.language_select_item_select);
            viewHolder.language_select_item_head = (ImageView) convertView.findViewById(R.id.language_select_item_head);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.language_select_item_text.setText(type);
        viewHolder.language_select_item_head.setImageDrawable(context.getDrawable(list.get(position).getImg()));
        if(list.get(position).getisSelect()){
            viewHolder.language_select_item_select.setImageDrawable(context.getDrawable(R.drawable.language_select_selected));

        }else{
            viewHolder.language_select_item_select.setImageDrawable(context.getDrawable(R.drawable.language_select_unselect));
        }

        // viewHolder.image.setImageResource(mData.get(position).get());
        return convertView;
    }

    public class ViewHolder
    {
        public TextView language_select_item_text;
        public ImageView language_select_item_head;
        public ImageView language_select_item_select;
    }
}
