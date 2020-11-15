package com.anzer.hospitalebook.models.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;


public class ExpandableHeightGridView extends GridView {

    //	private boolean expanded = false;
    private int itemHeight;


    public ExpandableHeightGridView(Context context) {
        super(context);
    }


    public ExpandableHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ExpandableHeightGridView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		if (isExpanded()) {
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    public void autoresize() {
        // Set gridview height
//	    ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int items = getAdapter().getCount();
        int columns = items == 1 ? 1 : 2;

        setNumColumns(columns);
    }

    public int getItemHeight() {
        return itemHeight;
    }
}
