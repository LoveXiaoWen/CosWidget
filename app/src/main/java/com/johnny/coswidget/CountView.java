package com.johnny.coswidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Johnny on 2017/11/10.
 */

/**
 * 项目名称：CosWidget
 * 类描述：计数控件
 * 创建人：Johnny
 * 创建时间：2017/11/10 20:08
 */
public class CountView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static final String DEFAULT_TEXT_COLOR = "#cccccc";
    private int mTextSize;
    private String num;

    public CountView(Context context) {
        this(context, null);
    }

    public CountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mTextSize = (int) Utils.dpToPixel(15);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.parseColor(DEFAULT_TEXT_COLOR));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        num = (10 + 1) * 10 + "";
        Rect rect = new Rect();
        mPaint.getTextBounds(num, 0, num.length(), rect);
        float width = rect.width();
        int height = mTextSize * 3;
        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int specSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        switch (heightSpecMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                if (specSize > mTextSize && specSize < height) {
                    height = specSize;
                }
                width = Math.max(widthSpecSize, width);
                break;
            default:
        }
        setMeasuredDimension((int) width+30, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(mTextSize);
        canvas.drawText(num, 0, getHeight()-20, mPaint);
    }
}
