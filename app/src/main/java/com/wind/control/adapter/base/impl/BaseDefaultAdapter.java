package com.wind.control.adapter.base.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDefaultAdapter<T, H extends BaseViewHolder> extends BaseAdapter
{
	final static String TAG = BaseDefaultAdapter.class.getName();

	protected final List<T> data;

	protected final Context context;

	protected int layoutResId;

	protected boolean displayProgress = false;

	protected MultiSupport<T> mMultiItemSupport;

	public BaseDefaultAdapter(Context context, int layoutResId)
	{
		this(null, context, layoutResId);
	}

	/**
	 * @param data
	 * @param context
	 * @param layoutResId
	 */
	public BaseDefaultAdapter(List<T> data, Context context, int layoutResId)
	{
		this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
		this.context = context;
		this.layoutResId = layoutResId;

	}

	public BaseDefaultAdapter(Context context, List<T> data, MultiSupport<T> multiItemSupport)
	{
		this.mMultiItemSupport = multiItemSupport;
		this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
		this.context = context;
	}

	/**
	 * 
	 * @param viewHolder
	 * @param bean
	 */
	protected abstract void convertBeanToView(H viewHolder, T bean);

	protected abstract H getViewHolder(int position, View convertView, ViewGroup parent);

	@Override
	public int getCount()
	{
		int extra = displayProgress ? 1 : 0;
		return data.size() + extra;
	}

	@Override
	public T getItem(int position)
	{
		if (position < 0 || position >= data.size())
			return null;
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public int getViewTypeCount()
	{
		if (mMultiItemSupport != null)
			return mMultiItemSupport.getViewTypeCount() + 1;
		return 2;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (displayProgress)
		{
			if (mMultiItemSupport != null)
				return position >= data.size() ? 0 : mMultiItemSupport
						.getItemViewType(position, data.get(position));
		} else
		{
			if (mMultiItemSupport != null)
				return mMultiItemSupport.getItemViewType(position,
						data.get(position));
		}

		return position >= data.size() ? 0 : 1;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		if (getItemViewType(position) == 0 && displayProgress)
		{
			return createIndeterminateProgressView(convertView, parent);
		}
		final H viewHolder = getViewHolder(position, convertView, parent);
		T bean = getItem(position);
		viewHolder.setAssociatedObject(bean);
		convertBeanToView(viewHolder, bean);
		return viewHolder.getConvertView();
	}

	private View createIndeterminateProgressView(View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			FrameLayout container = new FrameLayout(context);
			container.setForegroundGravity(Gravity.CENTER);
			ProgressBar progress = new ProgressBar(context);
			container.addView(progress);
			convertView = container;
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return position < data.size();
	}

	/**************************************** list method ****************************************************/

	public void add(T elem)
	{
		data.add(elem);
		notifyDataSetChanged();
	}

	public void addAll(List<T> elem)
	{
		data.addAll(elem);
		notifyDataSetChanged();
	}
	
	public void addAllUniq(List<T> elem){
		if (elem != null) {
			elem.removeAll(data);
			data.addAll(elem);
			notifyDataSetChanged();
		}
	}

	public void set(T oldElem, T newElem)
	{
		set(data.indexOf(oldElem), newElem);
	}

	public void set(int index, T elem)
	{
		data.set(index, elem);
		notifyDataSetChanged();
	}

	public void remove(T elem)
	{
		data.remove(elem);
		notifyDataSetChanged();
	}

	public void remove(int index)
	{
		data.remove(index);
		notifyDataSetChanged();
	}

	public void replaceAll(List<T> elem)
	{
		data.clear();
		data.addAll(elem);
		notifyDataSetChanged();
	}

	public boolean contains(T elem)
	{
		return data.contains(elem);
	}

	public void clear()
	{
		data.clear();
		notifyDataSetChanged();
	}

	/**************************************** list method end! ****************************************************/

	public void showIndeterminateProgress(boolean display)
	{
		if (display == displayProgress)
			return;
		displayProgress = display;
		notifyDataSetChanged();
	}

}
