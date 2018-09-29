package com.wind.control.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.DeviceListBean;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by W010003373 on 2018/8/9.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<DeviceListBean.DevicesBean> mList;
    private onItemClickListener mOnItemClickListener;
    private Context mContext;

    public HomeListAdapter(Context context, List<DeviceListBean.DevicesBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_list, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mTvName.setText(mList.get(position).getName());
        holder.mTvDesc.setText(mList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
        if (mList.get(position).getType().equals("1")) {
            holder.mImageAc.setVisibility(View.GONE);
            holder.mImageDeng.setVisibility(View.VISIBLE);
            holder.mImageDeng.setBackgroundResource(R.drawable.ic_light);
        } else if(mList.get(position).getType().equals("2")){
            holder.mImageAc.setVisibility(View.VISIBLE);
            holder.mImageDeng.setVisibility(View.GONE);
            holder.mImageAc.setBackgroundResource(R.drawable.ic_kongtiao);
        } else if(mList.get(position).getType().equals("3")){
            holder.mImageAc.setVisibility(View.VISIBLE);
            holder.mImageDeng.setVisibility(View.GONE);
            holder.mImageAc.setBackgroundResource(R.drawable.ic_chazuo);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() > 3 ? 3 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_deng_name)
        ImageView mImageDeng;
        @BindView(R.id.iv_ac_name)
        ImageView mImageAc;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}