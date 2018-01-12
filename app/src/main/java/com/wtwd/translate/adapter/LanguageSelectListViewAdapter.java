package com.wtwd.translate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/2
 * Created by w77996
 */
public class LanguageSelectListViewAdapter extends RecyclerView.Adapter<LanguageSelectListViewAdapter.LanguageSelectListViewHolder> {
    private Context context;
    private List<SelectBean> datas =new ArrayList<SelectBean>();
    int mWith ;

    public LanguageSelectListViewAdapter(Context mContext, List<SelectBean> datas) {
        this.context = mContext;
        this.datas = datas;
       mWith =Utils.getDisplayWidth(context);
    }

     @Override
     public LanguageSelectListViewAdapter.LanguageSelectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View languageSelectListItemView = View.inflate(context, R.layout.language_select_recyclerview_item,null);
         languageSelectListItemView.setLayoutParams(new LinearLayout.LayoutParams(mWith/5, ViewGroup.LayoutParams.MATCH_PARENT));
         return new LanguageSelectListViewHolder(languageSelectListItemView);
     }

     @Override
     public void onBindViewHolder(final LanguageSelectListViewAdapter.LanguageSelectListViewHolder holder, int position) {
         String index = datas.get(position).getData();
         holder.mLanguageTexView.setText(index);
        holder.mLanguageTexView.setTextColor(Color.parseColor("#686868"));
         if (mOnItemClickLitener != null)
         {
             holder.itemView.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View v)
                 {

                     int pos = holder.getLayoutPosition();
                     mOnItemClickLitener.onItemClick(holder.itemView, pos);

                 }
             });
         }
         if (datas.get(position).getisSelect()){
             holder.mLanguageTexView.setTextColor(Color.parseColor("#000000"));
         }/*else{
             holder.mLanguageTexView.setTextColor(Color.parseColor("#686868"));
         }*/

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
