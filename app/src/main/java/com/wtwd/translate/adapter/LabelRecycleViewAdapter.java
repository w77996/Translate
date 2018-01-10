package com.wtwd.translate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/9
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */
public class LabelRecycleViewAdapter extends RecyclerView.Adapter<LabelRecycleViewAdapter.LabelRecycleViewHolder> {
    private Context context;
    private List<SelectBean> datas =new ArrayList<SelectBean>();
  //  int mWith ;
   // int isSelected;

    public LabelRecycleViewAdapter(Context mContext, List<SelectBean> datas) {
        this.context = mContext;
        this.datas = datas;
       // mWith = Utils.getDisplayWidth(context);
    }

    @Override
    public LabelRecycleViewAdapter.LabelRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View languageSelectListItemView = View.inflate(context, R.layout.gv_label_item,null);
       // languageSelectListItemView.setLayoutParams(new LinearLayout.LayoutParams(mWith/5, ViewGroup.LayoutParams.MATCH_PARENT));
        return new LabelRecycleViewHolder(languageSelectListItemView);
    }

    @Override
    public void onBindViewHolder(final LabelRecycleViewAdapter.LabelRecycleViewHolder holder, int position) {
        String index = datas.get(position).getData();
        holder.mitemText.setText(index);


        //Log.d("ddddd","ddddd");
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    boolean select = datas.get(pos).getisSelect();
                   // holder.isSelect = true;
                    mOnItemClickLitener.onItemClick(holder.itemView, pos,select);

                 /*   Log.d("select","s"+ select);
                    if(select){
                        holder.mitemText.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.mitemImage.setBackground(context.getResources().getDrawable(R.drawable.rl_item_normal));
                        datas.get(pos).isSelect = false;
                        Log.d("444", "ture: ");
                    }else{
                        holder.mitemText.setTextColor(Color.parseColor("#9e9e9e"));
                        holder.mitemImage.setBackground(context.getResources().getDrawable(R.drawable.rl_item_select));
                        datas.get(pos).isSelect = true;
                        Log.d("444", "false");
                    }*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();

    }

    public class LabelRecycleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mitemImage;
        public TextView mitemText;
        public boolean isSelect = false;
        public LabelRecycleViewHolder(View itemView) {
            super(itemView);
            mitemImage = (ImageView) itemView.findViewById(R.id.rl_item_btn);
            mitemText = (TextView)itemView.findViewById(R.id.rl_item_tv);
            // mLanguageTexView.setTextColor(context.getResources().getColor(R.color.main_title_color));
           // Log.d("fsdfas","fasdf");
        }
    }
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position,boolean select);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}

