package com.wind.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wind.control.R;
import com.wind.control.util.OnItemClickLisnter;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片选择器，意见反馈图片上传
 * Created by luow on 2017/12/20.
 */

public class SelectPhotosAdapter extends RecyclerView.Adapter<SelectPhotosAdapter.SelectPhotosViewHolderr> {


    private Context mContext;
    private List<String> mUrls;
    private LayoutInflater mInflater;

    public SelectPhotosAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public SelectPhotosViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectPhotosViewHolderr(mInflater.inflate(R.layout.item_my_photo, parent, false));
    }

    private static final String TAG = "SelectPhotosAdapter";

    @Override
    public void onBindViewHolder(final SelectPhotosViewHolderr holder, final int position) {
        if (position == getItemCount() - 1 && mUrls.size() != 6) {
            holder.iv_photo.setImageResource(R.drawable.ic_my_add_photo);
        } else {
            Log.e(TAG, "onBindViewHolder: position:" + position);
            Glide.with(mContext)
                    .load(mUrls.get(position))
                    .into(holder.iv_photo);
        }
        if (mOnItemClickLisnter != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLisnter.onItemClick(holder.iv_photo, position, null);
                }
            });
    }

    @Override
    public int getItemCount() {
        int count = null == mUrls ? 1 : mUrls.size() + 1;
        return count > 6 ? 6 : count;
    }


    public void setData(List<String> urls) {
        mUrls = urls;
        notifyDataSetChanged();
    }

    public void setOnItemClickLisnter(OnItemClickLisnter l) {
        mOnItemClickLisnter = l;
    }

    static class SelectPhotosViewHolderr extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView iv_photo;

        SelectPhotosViewHolderr(View itemView) {
            super(itemView);
            SupportMultipleScreensUtil.scale(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickLisnter mOnItemClickLisnter;
}
