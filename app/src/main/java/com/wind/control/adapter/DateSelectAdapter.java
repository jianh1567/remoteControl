package com.wind.control.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.model.TimerDateInfo;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Created by houjian on 2018/7/10
 * 注释：
 */
public class DateSelectAdapter extends RecyclerView.Adapter<DateSelectAdapter.ViewHolder> {
    List<TimerDateInfo> mDateList;
    private Context mContext;

    public DateSelectAdapter(List<TimerDateInfo> dateList) {
        mDateList = dateList;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_select, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTvDateSelect.setText(mDateList.get(position).getDate());
        setDateSelected(mDateList.get(position).getChecked(),  holder.mTvDateSelect);
        holder.mTvDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });
    }

    private void setDateSelected(boolean isSelect, TextView view){
        view.setBackgroundResource(isSelect ? R.drawable.shape_date_selected : R.drawable.shape_date);
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date_select)
        TextView mTvDateSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListner(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}