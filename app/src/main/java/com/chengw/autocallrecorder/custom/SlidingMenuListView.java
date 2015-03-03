package com.chengw.autocallrecorder.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Cheng on 3/2/2015.
 */
public class SlidingMenuListView extends ListView {
    public SlidingMenuListView(Context context) {
        super(context);
    }

    public SlidingMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
