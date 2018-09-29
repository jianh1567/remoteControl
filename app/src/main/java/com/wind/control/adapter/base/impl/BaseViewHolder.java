package com.wind.control.adapter.base.impl;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseViewHolder
{
	final static int DEFULT_POSITION = -1;
	private View convertView;
	private int position;
	private final Context context;
	private final SparseArray<View> cacheViews;
	public int layoutId;

	Object associatedObject;
	private boolean isNew =true;

	public BaseViewHolder(Context context, ViewGroup parent, int layoutId, int position)
	{
		if (context == null)
		{
			throw new NullPointerException("Context  is null");
		}
		this.context = context;
		this.position = position;
		this.layoutId = layoutId;
		this.cacheViews = new SparseArray<View>();
		this.convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, parent, false);
		convertView.setTag(this);
	}

	public static BaseViewHolder getHolder(Context context, View convertView, ViewGroup parent, int layoutId)
	{
		return getHolder(context, convertView, parent, layoutId, DEFULT_POSITION);
	}

	public static BaseViewHolder getHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position)
	{
		if (convertView == null)
		{
			return new BaseViewHolder(context, parent, layoutId, position);
		}

		BaseViewHolder oldViewHolder = (BaseViewHolder) convertView.getTag();
		if (oldViewHolder.layoutId != layoutId)
		{
			return new BaseViewHolder(context, parent, layoutId, position);
		}

		oldViewHolder.isNew = false;
		oldViewHolder.position = position;
		return oldViewHolder;
	}

	/**
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		return getCacheView(viewId);
	}

	public View getConvertView()
	{
		return convertView;
	}

	public int getPosition()
	{
		if (position == DEFULT_POSITION)
			throw new IllegalStateException();
		return position;
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T getCacheView(int viewId)
	{
		View view = cacheViews.get(viewId);
		if (view == null)
		{
			view = convertView.findViewById(viewId);
			cacheViews.put(viewId, view);
		}
		return (T) view;
	}

	public Context getContext()
	{
		return context;
	}

	public Object getAssociatedObject()
	{
		return associatedObject;
	}

	public void setAssociatedObject(Object associatedObject)
	{
		this.associatedObject = associatedObject;
	}

	public boolean isNew() {
		return isNew;
	}
}
