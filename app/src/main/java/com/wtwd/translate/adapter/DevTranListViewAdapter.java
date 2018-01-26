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
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/16
 * Created by w77996
 */

public class DevTranListViewAdapter extends BaseAdapter implements View.OnClickListener{

    public List<RecorderBean> list = new ArrayList<>();
    public Context context;
    public LayoutInflater inflater;
    public PlayVoceClick mCallback;
    public DevTranListViewAdapter(Context context, List<RecorderBean> list,PlayVoceClick playVoceClick){
        this.list = list;
        this.context = context;
        this.mCallback = playVoceClick;
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
               // String rightLanguage = SpUtils.getString(context,Constants.RIGHT_LANGUAGE,Constants.en_US);
                convertView = inflater.inflate(R.layout.chat_right, null);
                viewHolder.tv_recv_txt = (TextView) convertView.findViewById(R.id.tv_chat_recv);
                viewHolder.tv_recorder_txt = (TextView) convertView.findViewById(R.id.tv_chat_send);
                viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_chat_right_head);
                viewHolder.img_chat_play = (ImageView) convertView.findViewById(R.id.img_chat_play);

                convertView.setTag(viewHolder);
            }else{
               // String leftLanguage = SpUtils.getString(context,Constants.LEFT_LANGUAGE,Constants.zh_CN);
                convertView = inflater.inflate(R.layout.chat_left, null);
                viewHolder.tv_recv_txt = (TextView) convertView.findViewById(R.id.tv_chat_recv);
                viewHolder.tv_recorder_txt = (TextView) convertView.findViewById(R.id.tv_chat_send);
                viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_chat_left_head);
                viewHolder.img_chat_play = (ImageView) convertView.findViewById(R.id.img_chat_play);
                //Utils.setLanguageHead(context, viewHolder.img_head,leftLanguage);
                convertView.setTag(viewHolder);
            }

       /* } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }*/
        viewHolder.tv_recorder_txt.setText(list.get(position).getmRecorderTxt());
        viewHolder.tv_recv_txt.setText(list.get(position).getmResultTxt());
        viewHolder.img_chat_play.setTag(position);
        viewHolder.img_chat_play.setOnClickListener(this);

        if(type == Constants.ITEM_RIGHT){
            Utils.setLanguageHead(context,viewHolder.img_head,list.get(position).getLanguage_type());
        }else if(type == Constants.ITEM_LEFT){
            Utils.setLanguageHead(context,viewHolder.img_head,list.get(position).getLanguage_type());
        }

        //viewHolder.img_head.setImageDrawable();

        // viewHolder.image.setImageResource(mData.get(position).get());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    public class ViewHolder{
        public TextView tv_recorder_txt;
        public ImageView img_head;
        public ImageView img_chat_play;
        public TextView tv_recv_txt;
    }


     public interface PlayVoceClick {
         public void click(View v);
     }
}
