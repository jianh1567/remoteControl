package com.wind.control.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.SceneActionInfo;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 1003373 on 2018/8/27.
 */

public class SceneActionAdapter extends RecyclerView.Adapter<SceneActionAdapter.ViewHolder> {
    private List<SceneActionInfo> mSceneAddActionList;
    private onClickListener mOnClickListener;

    public SceneActionAdapter(List<SceneActionInfo> sceneAddActionInfos) {
        this.mSceneAddActionList = sceneAddActionInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_action, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTvAddAction.setText(mSceneAddActionList.get(position).getActionName());
        holder.mIvAddAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onDelteBtnClick(holder.getAdapterPosition());
            }
        });
        holder.mLlAddAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSceneAddActionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_add_action)
        RelativeLayout mLlAddAction;
        @BindView(R.id.iv_add_action)
        ImageView mIvAddAction;
        @BindView(R.id.tv_add_action)
        TextView mTvAddAction;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onClickListener {
        void onDelteBtnClick(int position);

        void onItemClick(int position);
    }

    public void setOnDelClickListener(onClickListener listener) {
        mOnClickListener = listener;
    }
}
