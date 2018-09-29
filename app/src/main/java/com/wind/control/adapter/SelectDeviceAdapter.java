package com.wind.control.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.SelectDeviceBean;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

public class SelectDeviceAdapter extends ArrayAdapter<SelectDeviceBean.TriadinfoBean> {

    private final LayoutInflater mInflater;
    private Context mContext;

    public SelectDeviceAdapter(Context context, List<SelectDeviceBean.TriadinfoBean> items) {
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
        TextView tvName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SelectDeviceBean.TriadinfoBean item = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_select_device, parent,
                    false);
            SupportMultipleScreensUtil.scale(convertView);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getTriadTitle());

        return convertView;
    }

}
