package com.johnny.coswidget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Johnny on 2017/11/10.
 */

/**
 * 项目名称：CosWidget
 * 类描述：点赞计数控件
 * 创建人：Johnny
 * 创建时间：2017/11/10 20:10
 */
public class LikeCountView extends LinearLayout {
    private LikeView mLikeView;
    private CountView mCountView;

    public LikeCountView(Context context) {
        super(context);
    }

    public LikeCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOrientation(LinearLayout.HORIZONTAL);
        addLikeView();
        addCountView();
    }

    private void addLikeView() {
        mLikeView  = new LikeView(getContext());
        addView(mLikeView);
    }

    private void addCountView() {
        mCountView = new CountView(getContext());
        addView(mCountView);
    }

}
