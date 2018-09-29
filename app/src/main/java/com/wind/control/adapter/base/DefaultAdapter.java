package com.wind.control.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wind.control.adapter.base.impl.BaseDefaultAdapter;
import com.wind.control.adapter.base.impl.BaseViewHolder;
import com.wind.control.adapter.base.impl.MultiSupport;

import java.util.ArrayList;
import java.util.List;

import static com.wind.control.adapter.base.impl.BaseViewHolder.getHolder;

public abstract class DefaultAdapter<T> extends BaseDefaultAdapter<T, BaseViewHolder>
{

	public DefaultAdapter(Context context, int layoutId)
	{
		super(context, layoutId);
	}

	public DefaultAdapter(List<T> data, Context context, int layoutid)
	{
		super(data, context, layoutid);
	}

	/**
	 * listView 多布局支持
	 * 
	 * @param context
	 * @param data
	 * @param multiItemSupport
	 */
	public DefaultAdapter(Context context, ArrayList<T> data, MultiSupport<T> multiItemSupport)
	{
		super(context, data, multiItemSupport);

	}

	@Override
	protected BaseViewHolder getViewHolder(int position, View convertView, ViewGroup parent)
	{

		if (mMultiItemSupport != null)
		{
			return getHolder(context, convertView, parent, mMultiItemSupport.getLayoutId(position, data.get(position)), position);
		} else
		{
			return getHolder(context, convertView, parent, layoutResId, position);
		}
	}

}
