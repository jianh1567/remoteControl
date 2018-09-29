package com.wind.control.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.SceneList;
import com.wind.control.view.MLImageView;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by W010003373 on 2018/8/9.
 */

public class SceneListAdapter extends RecyclerView.Adapter<SceneListAdapter.ViewHolder> {
    private static final String TAG = "SceneListAdapter";
    private ArrayList<SceneList> mSceneBeanList;
    private onClickListener mOnClickListener;
    private Context mContext;
    private Animation mAnimation;

    public SceneListAdapter(ArrayList<SceneList> sceneBeanList) {
        mSceneBeanList = sceneBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_list, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mlImageView.setDrawable(mContext.getDrawable(mSceneBeanList.get(position).getImageId()));
        holder.mSceneName.setText(mSceneBeanList.get(position).getSceneName());

        /*if(mSceneBeanList.get(position).getDelBtnVisible()){
            holder.mBtnDelete.setVisibility(View.VISIBLE);
        }else {
            holder.mBtnDelete.setVisibility(View.GONE);
        }*/

        holder.mlImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onItemClick(position);
            }
        });

        holder.mBtnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onExeBtnClick(position);
            }
        });

        holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onDeleteBtnClick(position);
            }
        });
    }

    private void setSceneChecked(boolean isChecked, MLImageView view) {
        view.setmBorderWidth(isChecked ? 8 : 0);
        view.invalidate();
        if (isChecked) {
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale_animation);
            view.startAnimation(mAnimation);
        }
    }

    @Override
    public int getItemCount() {
        return mSceneBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ml_imageview)
        MLImageView mlImageView;
        @BindView(R.id.btn_execute)
        Button mBtnExecute;
        @BindView(R.id.scene_name)
        TextView mSceneName;
        @BindView(R.id.trigger_name)
        TextView mTriggerName;
        @BindView(R.id.btn_delete)
        Button mBtnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface onClickListener {
        void onItemClick(int position);

        void onExeBtnClick(int position);

        void onDeleteBtnClick(int position);
    }

    public void setClickListener(onClickListener listener) {
        mOnClickListener = listener;
    }
}
