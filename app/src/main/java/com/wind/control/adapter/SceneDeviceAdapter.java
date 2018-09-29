package com.wind.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.DeviceListBean;
import com.wind.control.model.SceneDeviceBean;
import com.wind.control.util.ToastUtils;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jameson on 8/30/16.
 */
public class SceneDeviceAdapter extends RecyclerView.Adapter<SceneDeviceAdapter.ViewHolder> {
    private List<DeviceListBean.DevicesBean> mList = new ArrayList<>();
    private Context mContext;

    public SceneDeviceAdapter(List<DeviceListBean.DevicesBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_list, parent, false);
        SupportMultipleScreensUtil.scale(itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIvDevice.setBackgroundResource(getResId(mList.get(position).getType()));
        holder.mTvDeviceName.setText(mList.get(position).getName());
        holder.mRlSceneDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });
    }

    private int getResId(String type){
        if(type.equals("1")){
            return R.drawable.ic_light;
        }else if(type.equals("2")){
            return R.drawable.ic_airc;
        }
        return R.drawable.ic_launcher_background;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_device)
        ImageView mIvDevice;
        @BindView(R.id.tv_device_name)
        TextView mTvDeviceName;
        @BindView(R.id.rl_scene_device)
        RelativeLayout mRlSceneDevice;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListner(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
