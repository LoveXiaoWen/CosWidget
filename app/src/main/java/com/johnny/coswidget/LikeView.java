package com.johnny.coswidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Johnny on 2017/11/7.
 */

/**
 * 项目名称：CosWidget
 * 类描述：点赞控件
 * 创建人：Johnny
 * 创建时间：2017/11/7 10:23
 */
public class LikeView extends View {

    private int mLikeCount;
    private Bitmap mUnSelectBitmap;
    private Bitmap mSelectedBitmap;
    private Bitmap mShiningBitmap;
    private Paint mPaint;

    private float mWidth;
    private float mHeight;
    private float mCenterX;
    private float mCenterY;
    private boolean mIsLike;//标志位
    private boolean mIsThumbInProgress;
    private boolean mIsUnThumbInnProgress;


    float scale = 1;

    private float mAnimatProgress;

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnSelectBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_unselected);
        mSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_selected);
        mShiningBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_selected_shining);
    }

    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLike) {
                    mIsLike = false;
                    if (!mIsUnThumbInnProgress) {
                        //执行取消点赞动画
                        unThumbAnimator();
                        mIsUnThumbInnProgress = true;
                    }
                } else {
                    mIsLike = true;
                    if (!mIsThumbInProgress) {
                        //执行点赞动画
                        upThumbAnimator();
                        mIsThumbInProgress = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = getPaddingLeft() + mUnSelectBitmap.getWidth() + getPaddingRight()
                + (int) Utils.dpToPixel(15);
        int widthSpec = View.MeasureSpec.getMode(widthMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int defaultHeight = getPaddingTop() + mUnSelectBitmap.getHeight() + mShiningBitmap.getHeight()
                + getPaddingBottom() + (int) Utils.dpToPixel(5);
        int heightSpec = View.MeasureSpec.getMode(heightMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        switch (widthSpec) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = Math.max(defaultWidth, width);
                break;
            default:
        }
        switch (heightSpec) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = Math.max(defaultHeight, height);
                break;
            default:
        }
        setMeasuredDimension(defaultWidth, defaultHeight);
    }

    private void upThumbAnimator() {
        ObjectAnimator unThumbAnimator = ObjectAnimator.ofFloat(this, "scale", 1.0f, 0.9f);
        unThumbAnimator.setDuration(100);

        ObjectAnimator thumbAnimator = ObjectAnimator.ofFloat(this, "scale", 1.1f, 1.0f);
        thumbAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(unThumbAnimator, thumbAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsThumbInProgress = false;
                mIsUnThumbInnProgress = false;
            }
        });
        animatorSet.start();
    }

    private void unThumbAnimator() {
        ObjectAnimator unThumbAnimator = ObjectAnimator.ofFloat(this, "scale", 1.0f, 0.9f);
        unThumbAnimator.setDuration(200);
        unThumbAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsThumbInProgress = false;
                mIsUnThumbInnProgress = false;
            }
        });
        unThumbAnimator.start();
    }


    public int getLikeCount() {
        return mLikeCount;
    }

    public void setLikeCount(int likeCount) {
        this.mLikeCount = likeCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        drawIcon(canvas);
    }

    private void drawIcon(Canvas canvas) {
        if (mIsThumbInProgress) {
            if (scale <= 1) {
                float iconWidth = mUnSelectBitmap.getWidth();
                float iconHeight = mUnSelectBitmap.getHeight();
                canvas.save();
                canvas.scale(scale, scale, mCenterX, mCenterY);
                canvas.drawBitmap(mUnSelectBitmap, mCenterX - iconWidth / 2, mCenterY - iconHeight / 2, mPaint);
                canvas.restore();
            } else {
                float iconWidth = mSelectedBitmap.getWidth();
                float iconHeight = mSelectedBitmap.getHeight();
                canvas.save();
                canvas.scale(scale, scale, mCenterX, mCenterY);
                canvas.drawBitmap(mSelectedBitmap, mCenterX - iconWidth / 2, mCenterY - iconHeight / 2, mPaint);
                canvas.restore();

                float shiningWidth = mShiningBitmap.getWidth();
                float shiningHeight = mShiningBitmap.getHeight();
                canvas.save();
                float shiningScale = (1 - (scale - 1f) * 10);
                canvas.scale(shiningScale, shiningScale, mCenterX, mCenterY);
                Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                alphaPaint.setAlpha((int) (shiningScale * 100));
                canvas.drawBitmap(mShiningBitmap, mCenterX - shiningWidth / 2, mCenterY - shiningHeight / 2 - iconHeight / 2, alphaPaint);
                canvas.restore();

                Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                colorPaint.setStyle(Paint.Style.STROKE);
                colorPaint.setStrokeWidth(5);
                colorPaint.setColor(0xFFE1583D);
                colorPaint.setAlpha(80);
                canvas.save();
                float circleScale = (1 - (scale - 1f) * 10) + 0.2f;
                canvas.scale(circleScale, circleScale, mCenterX, mCenterY);
                canvas.drawCircle(mCenterX, mCenterY, iconWidth * 0.6f, colorPaint);
                canvas.restore();
            }
            return;
        }

        if (mIsUnThumbInnProgress) {
            float iconWidth = mSelectedBitmap.getWidth();
            float iconHeight = mSelectedBitmap.getHeight();
            canvas.save();
            canvas.scale(scale, scale, mCenterX, mCenterY);
            canvas.drawBitmap(mSelectedBitmap, mCenterX - iconWidth / 2, mCenterY - iconHeight / 2, mPaint);
            canvas.restore();
            return;
        }

        if (mIsLike) {
            float iconWidth = mSelectedBitmap.getWidth();
            float iconHeight = mSelectedBitmap.getHeight();
            canvas.drawBitmap(mSelectedBitmap, mCenterX - iconWidth / 2, mCenterY - iconHeight / 2, mPaint);

            float shiningWidth = mShiningBitmap.getWidth();
            float shiningHeight = mShiningBitmap.getHeight();
            canvas.drawBitmap(mShiningBitmap, mCenterX - shiningWidth / 2, mCenterY - shiningHeight / 2 - iconHeight / 2, mPaint);
        } else {
            float iconWidth = mUnSelectBitmap.getWidth();
            float iconHeight = mUnSelectBitmap.getHeight();
            canvas.drawBitmap(mUnSelectBitmap, mCenterX - iconWidth / 2, mCenterY - iconHeight / 2, mPaint);
        }
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        invalidate();
    }
}
