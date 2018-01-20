package com.wtwd.translate.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;

import java.util.List;

/**
 * time:2018/1/18
 * Created by w77996
 */

public class BlueScanListAdapter extends BaseAdapter {

    public List<BluetoothDevice> list;
    public Context mContext;
    LayoutInflater mInflater;

    public BlueScanListAdapter(Context context, List<BluetoothDevice> list) {
        this.mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // int type = list.get(position).getType();
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_blue_scan_item, null);
            viewHolder.blue_name = (TextView) convertView.findViewById(R.id.blue_name);
            viewHolder.blue_mac = (TextView) convertView.findViewById(R.id.blue_mac);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(list.get(position).getName() == null || "".equals(list.get(position).getName())){
            viewHolder.blue_name.setText("UNKOWN");
        }else{
            viewHolder.blue_name.setText(list.get(position).getName());
        }
        ;
        viewHolder.blue_mac.setText(list.get(position).getAddress());
        // viewHolder.image.setImageResource(mData.get(position).get());
        return convertView;
    }

    public class ViewHolder {
        public TextView blue_name;
        public TextView blue_mac;
    }
}
