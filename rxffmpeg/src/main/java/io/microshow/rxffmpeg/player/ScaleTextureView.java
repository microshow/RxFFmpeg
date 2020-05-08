package io.microshow.rxffmpeg.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;


/**
 * 带手势缩放的 TextureView 支持双指缩放、平移、旋转
 * <p>
 * 同时也可以换成继承 RelativeLayout 或其它View,这样内部包含的view也会一起跟着变化
 * <p>
 * Created by Super on 2020/5/5.
 */
public class ScaleTextureView extends TextureView {

    /**
     * 移动X
     */
    private float translationX;
    /**
     * 移动Y
     */
    private float translationY;
    /**
     * 伸缩比例
     */
    private float scale = 1;
    /**
     * 旋转角度
     */
    private float rotation;

    // 移动过程中临时变量
    private float actionX;
    private float actionY;
    private float spacing;
    private float degree;
    private int moveType; // 0=未选择，1=拖动，2=缩放

    /**
     * 默认开启缩放 Touch
     */
    private boolean enabledTouch = true;

    //默认启用旋转功能
    private boolean enabledRotation = true;

    //默认启用移动功能
    private boolean enabledTranslation = true;

    public ScaleTextureView(Context context) {
        this(context, null);
    }

    public ScaleTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabledTouch) {//整体 Touch 开关
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    moveType = 1;
                    actionX = event.getRawX();
                    actionY = event.getRawY();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    moveType = 2;
                    spacing = getSpacing(event);
                    degree = getDegree(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (moveType == 1) {
                        if (enabledTranslation) {//启用了移动操作
                            translationX = translationX + event.getRawX() - actionX;
                            translationY = translationY + event.getRawY() - actionY;
                            setTranslationX(translationX);
                            setTranslationY(translationY);
                            actionX = event.getRawX();
                            actionY = event.getRawY();
                        }
                    } else if (moveType == 2) {
                        //启用缩放操作
                        scale = scale * getSpacing(event) / spacing;
                        setScaleX(scale);
                        setScaleY(scale);
                        if (enabledRotation) {//启用了旋转操作
                            rotation = rotation + getDegree(event) - degree;
                            if (rotation > 360) {
                                rotation = rotation - 360;
                            }
                            if (rotation < -360) {
                                rotation = rotation + 360;
                            }
                            setRotation(rotation);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    moveType = 0;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 触碰两点间距离, 通过三角函数得到两点间的距离
     */
    private float getSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 取旋转角度. 得到两个手指间的旋转角度
     */
    private float getDegree(MotionEvent event) {
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * 整体手势开关设置
     * 设置是否启用 Touch true:启用（默认）；false:禁用
     */
    public void setEnabledTouch(boolean enabled) {
        this.enabledTouch = enabled;
    }

    /**
     * 设置是否启用旋转功能 true:启用（默认）；false:禁用
     */
    public void setEnabledRotation(boolean enabled) {
        this.enabledRotation = enabled;
    }

    /**
     * 设置是否启用移动功能 true:启用（默认）；false:禁用
     */
    public void setEnabledTranslation(boolean enabled) {
        this.enabledTranslation = enabled;
    }

    /**
     * 重置为最原始默认状态
     *
     * @param saveEnabled 是否保留已经开启或禁用的 移动 旋转 缩放动作
     *                    true：保留；false:不保留
     */
    public void reset(boolean saveEnabled) {
        translationX = 0;
        translationY = 0;
        scale = 1;
        rotation = 0;

        // 移动过程中临时变量
        actionX = 0;
        actionY = 0;
        spacing = 0;
        degree = 0;
        moveType = 0;

        if (!saveEnabled) {
            //enabled 开关
            enabledTouch = true;
            enabledRotation = true;
            enabledTranslation = true;
        }

        setScaleX(1.0f);
        setScaleY(1.0f);
        setRotation(0);
        setTranslationX(0);
        setTranslationY(0);
    }

}
