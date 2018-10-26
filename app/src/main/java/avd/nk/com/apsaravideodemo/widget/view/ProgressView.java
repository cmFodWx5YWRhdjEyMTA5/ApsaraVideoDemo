package avd.nk.com.apsaravideodemo.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import avd.nk.com.apsaravideodemo.R;

/**
 * Description:
 * <p></p>
 * Usage:
 * <p></p>
 *
 * <h4>Created by Nikou Karter on 2018/10/23.</h4>
 * <h5>Email: nikou.karter@outlook.com</h5>
 * <h5>Tim: 2482 17909(as QQ)</h5>
 */
public class ProgressView extends View {

    private static final String TAG = ProgressView.class.getSimpleName();

    private Paint linePaint;
    private Paint pointPaint;

    private int pointRadius = 16;//圆点默认半径,单位px
    private int pointColor = R.color.colorProgressPoint;//圆点默认颜色

    private int lineHeight = 4;//线默认高度,单位px
    private int lineColor = R.color.colorProgressLine;//线默认颜色

    private float progressFloat = 0f;
    private final float PROGRESS_MIN = 0f;
    private final float PROGRESS_MAX = 100f;

    private OnProgressChangedListener progressChangedListener;

    public interface OnProgressChangedListener {
        void onProgressChanged(float progress);

        void onProgressChanging(float progress);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineHeight);
        linePaint.setColor(getResources().getColor(lineColor));

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(getResources().getColor(pointColor));
    }

    public void setPointRadius(final int radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("radius 不可以小于等于0");
        }

        if (getWidth() == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (radius * 2 > getWidth()) {
                        throw new IllegalArgumentException("radius*2 必须小于 view.getWidth() == " + getWidth());
                    }
                    pointRadius = radius;
                }
            });
        } else {
            if (radius * 2 > getWidth()) {
                throw new IllegalArgumentException("radius*2 必须小于 view.getWidth() == " + getWidth());
            }
            this.pointRadius = radius;
        }
    }

    public void setPointColor(@ColorRes int color) {
        this.pointColor = color;
    }

    public void setLineHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("height 不可以小于等于0");
        }

        this.lineHeight = height;
    }

    public void setLineColor(@ColorRes int color) {
        this.lineColor = color;
    }

    public void setProgress(float progressFloat) {
        if (progressFloat < 0f || progressFloat > 100f) {
            return;
        }

        this.progressFloat = progressFloat;

        invalidate();

        /*if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(progressFloat);
        }*/
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() < pointRadius) {
            setProgress(PROGRESS_MIN);
            return true;
        } else if (event.getX() > getWidth() - pointRadius) {
            setProgress(PROGRESS_MAX);
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setProgress(calculateProgressByFloat(event.getX()));
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setProgress(calculateProgressByFloat(event.getX()));
                    if (progressChangedListener != null) {
                        progressChangedListener.onProgressChanging(progressFloat);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    //setProgress(calculateProgressByFloat(event.getX()));
                    if (progressChangedListener != null) {
                        progressChangedListener.onProgressChanged(progressFloat);
                    }
                    return true;
            }
        }
        //return super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, linePaint);
        canvas.drawCircle(getCx(), getHeight() / 2, pointRadius, pointPaint);
    }

    /**
     * 获取圆点的x轴坐标
     *
     * @return
     */
    private float getCx() {
        float cx;
        cx = (getWidth() - pointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView 宽度不可以小于 2 倍 pointRadius");
        }
        return cx / 100 * progressFloat + pointRadius;
    }

    /**
     * 计算触摸点的百分比
     *
     * @param eventX
     * @return
     */
    private int calculateProgress(float eventX) {
        float proResult = (eventX - pointRadius) / (getWidth() - pointRadius * 2);
        return (int) (proResult * 100);
    }

    private float calculateProgressByFloat(float eventX) {
        return (eventX - pointRadius) / (getWidth() - pointRadius * 2) * 100;
    }
}
