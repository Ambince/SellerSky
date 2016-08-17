package com.example.amence_a.customswitch_second;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Amence_A on 2016/8/17.
 */
public class SwitchToggle extends View {
    //定义背景图片
    private Bitmap switchBackgroupBitmap;
    //定义滑块图片
    private Bitmap slideButtonBitmap;
    //定义画笔
    private Paint mPaint;
    //定义触摸标记
    private boolean isTouchMode = false;
    //定义开关
    private boolean mSwitchState = true;
    //定义现在的位置
    private int currentX;
    //定义监听接口
    private onSwitchStateUpdateListener onSwitchStateUpdateListener;

    public SwitchToggle(Context context) {
        super(context, null);
    }

    public SwitchToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchToggle);
        int switchBackgroundResource = typedArray.getResourceId(R.styleable.SwitchToggle_switch_background, R.drawable.switch_background);
        int slideButtonResource = typedArray.getResourceId(R.styleable.SwitchToggle_switch_button, R.styleable.SwitchToggle_switch_button);
        setSwitchBackgroundResource(switchBackgroundResource);
        setSlideButtonResource(slideButtonResource);
    }

    //初始化信息
    private void init() {
        //初始化画笔
        mPaint = new Paint();
    }

    /**
     * 开始设置图片的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //这里需要自定义
        setMeasuredDimension(switchBackgroupBitmap.getWidth(), switchBackgroupBitmap.getHeight());
    }

    /**
     * 开始画东西
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(switchBackgroupBitmap, 0, 0, mPaint);
        //绘制滑块

        //判断是否触摸，没有触摸则是默认选择
        if (isTouchMode) {
            //根据当前用户触摸到的位置画滑块，让滑块向左移动自身一半大小的位置
            float newLeft = currentX - slideButtonBitmap.getWidth() / 2.0f;
            int maxLeft = switchBackgroupBitmap.getWidth() - slideButtonBitmap.getWidth();
            //设置限定范围
            if (newLeft < 0) {
                newLeft = 0;//左边范围
            } else if (newLeft > maxLeft) {
                newLeft = maxLeft;//右边范围
            }
            //开始绘制
            canvas.drawBitmap(slideButtonBitmap, newLeft, 0, mPaint);

        } else {
            if (mSwitchState) {
                //开的情况
                int newLeft = switchBackgroupBitmap.getWidth() - slideButtonBitmap.getWidth();
                canvas.drawBitmap(slideButtonBitmap, newLeft, 0, mPaint);
            } else {
                //关闭的情况
                canvas.drawBitmap(slideButtonBitmap, 0, 0, mPaint);
            }
        }
    }

    /**
     * 重写触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;
                currentX = (int) event.getX();


                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode = false;
                currentX = (int) event.getX();
                float center = switchBackgroupBitmap.getWidth() / 2.0f;
                //根据当前按下的位置和控件中心位置进行比较
                boolean state = currentX > center;
                boolean listenerState = onSwitchStateUpdateListener != null ? true : false;
                Log.v("Amence", "state=" + state + "  mSwitchState=" + mSwitchState + "  listenerState=" + listenerState);
                if (state != mSwitchState && onSwitchStateUpdateListener != null) {
                    Log.v("Amence", "现在的状态state=" + state);
                    //把最新的状态传出去
                    onSwitchStateUpdateListener.onStateUpdate(state);
                }
                mSwitchState = state;
                break;

        }
        //重绘画面
        invalidate();//onDraw()再次被调用

        //在本类中要处理事件
        return true;
    }

    /**
     * 要显示的button图加上资源
     *
     * @param slideButtonResource
     */
    private void setSlideButtonResource(int slideButtonResource) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButtonResource);
    }

    /**
     * 对背景图加上资源
     *
     * @param switchBackgroundResource
     */
    private void setSwitchBackgroundResource(int switchBackgroundResource) {
        switchBackgroupBitmap = BitmapFactory.decodeResource(getResources(), switchBackgroundResource);
    }

    //定义接口
    public interface onSwitchStateUpdateListener {
        //把状态值传出去
        void onStateUpdate(boolean state);
    }

    public void setOnSwitchStateUpdateListener(onSwitchStateUpdateListener listener) {
        this.onSwitchStateUpdateListener = listener;
    }

    //设置开关状态
    public void setSwitchState(boolean mSwitchState) {
        this.mSwitchState = mSwitchState;
    }

}
