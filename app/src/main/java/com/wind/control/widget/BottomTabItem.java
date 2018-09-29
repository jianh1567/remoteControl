package com.wind.control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;

/**
 * Created by luow on 2017/11/25.
 * 注释：底部tab封装
 */
public class BottomTabItem extends RelativeLayout {
    private static final String TAG = "BottomTabItem";
    // 布局控件
    private ImageView ibTab;
    private TextView tvTab;
    // 自定义属性值
    private String tabText;
    private int tabImageUnSelected;
    private int tabImageSelected;
    private int tabTextColorUnSelected;
    private int tabTextColorSelected;

    public BottomTabItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView(context);
    }

    public BottomTabItem(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 初始化我们自定义的组合控件
     */
    private void initView(Context context) {
        // 转化布局文件————>View对象，这个view对象直接挂载在自己（组合控件）身上
        View view = View.inflate(context, R.layout.view_layout_bottom_tab, this); // this代表挂载到自己身上
        SupportMultipleScreensUtil.scale(view);
        ibTab = (ImageView) this.findViewById(R.id.layout_image);
        tvTab = (TextView) this.findViewById(R.id.layout_text);
    }

    public BottomTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        try {
            // 从attrs.xml获取自定义的控件属性
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabItem);
            tabText = typedArray.getString(R.styleable.TabItem_tabText);
            tabImageUnSelected = typedArray.getResourceId(R.styleable.TabItem_tabImageUnSelected, 0);
            tabImageSelected = typedArray.getResourceId(R.styleable.TabItem_tabImageSelected, 0);
            tabTextColorUnSelected = typedArray
                    .getColor(
                            R.styleable.TabItem_tabTextColorUnSelected,
                            getResources().getColor(
                                    R.color.color_tab_bottom_text_unselected));
            tabTextColorSelected = typedArray.getColor(
                    R.styleable.TabItem_tabTextColorSelected, getResources()
                            .getColor(R.color.color_tab_bottom_text_selected));

            initTabTextColor(tabTextColorSelected, tabTextColorUnSelected);
            initTabImage(tabImageSelected, tabImageUnSelected);

            setTabText(tabText);
            setTabSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTabSelected(boolean selected) {
        try {
            if (selected) {
                ibTab.setImageResource(tabImageSelected);
                tvTab.setTextColor(tabTextColorSelected);
            } else {
                ibTab.setImageResource(tabImageUnSelected);
                tvTab.setTextColor(tabTextColorUnSelected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置按钮文字颜色
     *
     * @param textColorSelected   选中时颜色
     * @param textColorUnSelected 未选中时颜色
     */
    public void initTabTextColor(int textColorSelected, int textColorUnSelected) {
        tabTextColorSelected = textColorSelected;
        tabTextColorUnSelected = textColorUnSelected;
    }

    /**
     * 设置按钮图片
     *
     * @param imageSelected   选中时图片
     * @param imageUnSelected 未选中时图片
     */
    private void initTabImage(int imageSelected, int imageUnSelected) {
        tabImageSelected = imageSelected;
        tabImageUnSelected = imageUnSelected;
    }

    /**
     * 设置按钮文本
     */
    private void setTabText(String text) {
        tabText = text == null ? "" : text;
        tvTab.setText(tabText);
    }

}