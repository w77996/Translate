package com.wtwd.translate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wtwd.translate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/2
 * Created by w77996
 */
public class LanguageSelectListViewAdapter extends RecyclerView.Adapter<LanguageSelectListViewAdapter.LanguageSelectListViewHolder> {
    private Context context;
    private List<String> datas =new ArrayList<String>();


    public LanguageSelectListViewAdapter(Context mContext, List<String> datas) {
        this.context = mContext;
        this.datas = datas;
    }

     @Override
     public LanguageSelectListViewAdapter.LanguageSelectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View languageSelectListItemView = View.inflate(context, R.layout.language_select_recyclerview_item,null);
         return new LanguageSelectListViewHolder(languageSelectListItemView);
     }

     @Override
     public void onBindViewHolder(final LanguageSelectListViewAdapter.LanguageSelectListViewHolder holder, int position) {
         String index = datas.get(position);
         Log.d("fasdfasdfa","fasdfas"+index);
         holder.mLanguageTexView.setText(index);
         if (mOnItemClickLitener != null)
         {
             holder.itemView.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View v)
                 {
                     int pos = holder.getLayoutPosition();
                     mOnItemClickLitener.onItemClick(holder.itemView, pos);
                     holder.mLanguageTexView.setTextColor(Color.parseColor("#FFFFFF"));
                 }
             });
         }
     }

     @Override
     public int getItemCount() {
         return datas.size();

     }

      class LanguageSelectListViewHolder extends RecyclerView.ViewHolder{
         public TextView mLanguageTexView;
         public LanguageSelectListViewHolder(View itemView) {
             super(itemView);
             mLanguageTexView = (TextView)itemView.findViewById(R.id.tv_language_select);
         }
     }
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
