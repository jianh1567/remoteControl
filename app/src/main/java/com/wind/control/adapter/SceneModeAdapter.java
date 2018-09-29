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
import com.wind.control.model.SceneModelInfo;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by W010003373 on 2018/8/9.
 */

public class SceneModeAdapter extends RecyclerView.Adapter<SceneModeAdapter.ViewHolder> {
    private static final String TAG = "SceneModeAdapter";
    private ArrayList<SceneModelInfo> mSceneModelInfoList;
    private onItemClickListener mOnItemClickListener;

    public SceneModeAdapter(ArrayList<SceneModelInfo> sceneModelInfoList) {
        mSceneModelInfoList = sceneModelInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_mode, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SceneModelInfo sceneModelInfo = mSceneModelInfoList.get(position);
        holder.mIvSceneMode.setBackgroundResource(sceneModelInfo.getImageId());
        holder.mTvSceneMode.setText(sceneModelInfo.getDescbId());
        setSceneModeChecked(mSceneModelInfoList.get(position).getChecked(), holder.mRlSceneMode);
        holder.mIvSceneMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    private void setSceneModeChecked(boolean isChecked, RelativeLayout rlSceneMode) {
        rlSceneMode.setBackgroundResource(isChecked ? R.drawable.shape_scene_mode_selected : R.drawable.shape_scene_mode);
    }

    @Override
    public int getItemCount() {
        return mSceneModelInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_scene_mode)
        ImageView mIvSceneMode;
        @BindView(R.id.rl_scene_mode)
        RelativeLayout mRlSceneMode;
        @BindView(R.id.tv_scene_mode)
        TextView mTvSceneMode;

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
