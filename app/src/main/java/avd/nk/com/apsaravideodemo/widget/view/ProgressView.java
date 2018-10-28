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
 * Created by Nikou Karter.
 *
 * coming soon...
 */
public class ProgressView extends View {

    private static final String TAG = ProgressView.class.getSimpleName();

    private Paint linePaint;
    private Paint pointPaint;

    private int pointRadius = 16;//default radius of point.
    private int pointColor = R.color.colorProgressPoint;//default point color.

    private int lineHeight = 4;//default height of line (in px)
    private int lineColor = R.color.colorProgressLine;//default line color.

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
            throw new IllegalArgumentException("radius can not be less than or equal to 0");
        }

        if (getWidth() == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (radius * 2 > getWidth()) {
                        throw new IllegalArgumentException("radius*2 must be less than view.getWidth() == " + getWidth());
                    }
                    pointRadius = radius;
                }
            });
        } else {
            if (radius * 2 > getWidth()) {
                throw new IllegalArgumentException("radius*2 must be less than view.getWidth() == " + getWidth());
            }
            this.pointRadius = radius;
        }
    }

    public void setPointColor(@ColorRes int color) {
        this.pointColor = color;
    }

    public void setLineHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("height can not be less than or equal to 0");
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
     * get x coordinate of the point.
     *
     * @return
     */
    private float getCx() {
        float cx;
        cx = (getWidth() - pointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView's width can not be less than 2 times of point's radius");
        }
        return cx / 100 * progressFloat + pointRadius;
    }

    /**
     * calculate the percentage while in touching.
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
