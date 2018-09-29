package com.wind.control.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Created by luow on 2018/7/10
 * 注释：
 */
public class BleDeviceListAdapter extends RecyclerView.Adapter<BleDeviceListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BluetoothDevice> mLeDevices;
    private ArrayList<String> mDeviceType;

    public BleDeviceListAdapter(Context context, ArrayList<BluetoothDevice> mLeDevices,ArrayList<String> mDeviceType) {
        this.mContext = context;
        this.mLeDevices = mLeDevices;
        this.mDeviceType = mDeviceType;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ble_device_list, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (TextUtils.isEmpty(mLeDevices.get(position).getName())){
            holder.mTvDevice.setText("UnKnown device");
        }else{
            holder.mTvDevice.setText(mLeDevices.get(position).getName().trim());
        }
        holder.mTvMacAddress.setText(mLeDevices.get(position).getAddress().trim());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDevice(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLeDevices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device)
        TextView mTvDevice;
        @BindView(R.id.tv_mac_address)
        TextView mTvMacAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnRecycleOnDeviceClickListener mListener;

    public void setRecycleOnDeviceClickListener(OnRecycleOnDeviceClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnRecycleOnDeviceClickListener {
        void onClickDevice(View view, int position);
    }
}