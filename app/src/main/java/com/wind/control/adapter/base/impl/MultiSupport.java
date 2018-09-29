package com.wind.control.adapter.base.impl;

public interface MultiSupport<T>
{
	int getLayoutId(int position, T t);

	int getViewTypeCount();

	int getItemViewType(int postion, T t);
	
	
}
