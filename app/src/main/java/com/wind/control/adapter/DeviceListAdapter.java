package com.wind.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.DeviceListBean;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<DeviceListBean.DevicesBean> {

    private final LayoutInflater mInflater;
    private Context mContext;

    public DeviceListAdapter(Context context, List<DeviceListBean.DevicesBean> items) {
        super(context, 0, items);
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvName;
        ImageView iv_image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DeviceListBean.DevicesBean item = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device_fragment_list, parent,
                    false);
            SupportMultipleScreensUtil.scale(convertView);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(item.getTitle());
        holder.tvName.setText(item.getName());
        if (item.getType().equals("1")){
            holder.iv_image.setBackgroundResource(R.drawable.ic_light);
        }else if(item.getType().equals("2")){
            holder.iv_image.setBackgroundResource(R.drawable.ic_kongtiao);
        }else if(item.getType().equals("3")){
            holder.iv_image.setBackgroundResource(R.drawable.ic_chazuo);
        }

        return convertView;
    }

}
