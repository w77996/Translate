package com.wtwd.translate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.SelectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/9
 * Created by w77996
 */
public class LanguageGrideViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelectBean> mData = new ArrayList<SelectBean>();
    private LayoutInflater mInflater;
    private int[] countryImage;
    public int selectPosition;

    public LanguageGrideViewAdapter(Context mContext, List<SelectBean> mData, int[] countryImage){
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
        this.countryImage = countryImage;
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
            convertView = mInflater.inflate(R.layout.gv_language_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_flag = (TextView) convertView.findViewById(R.id.tv_flag);
            viewHolder.img_flag = (ImageView) convertView.findViewById(R.id.img_flag);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.linear_flag);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_flag.setText(mData.get(position).getData());
        viewHolder.tv_flag.setTextColor(Color.parseColor("#686868"));
        viewHolder.tv_flag.setTextSize(12);

        if(mData.get(position).isSelect){
            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.main_title_color));
            viewHolder.tv_flag.setTextColor(mContext.getResources().getColor(R.color.color_white));
        }else{
            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.app_background));
        }



        viewHolder.img_flag.setImageResource(countryImage[position]);


        return convertView;

}
    public class ViewHolder
    {
        public TextView tv_flag;
        public ImageView img_flag;
        public LinearLayout linearLayout;
    }


}
