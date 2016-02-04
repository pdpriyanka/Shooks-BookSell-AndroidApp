package com.cs442.project.team13;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ItemTextView extends TextView {

    private Paint linePaint;


    public ItemTextView (Context context, AttributeSet ats, int ds) {
        super(context, ats, ds);
        init();
    }

    public ItemTextView (Context context) {
        super(context);
        init();
    }

    public ItemTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Resources myResources = getResources();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.white));

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
        //setTextColor(getResources().getColor(R.color.textview_text_color));
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }

}