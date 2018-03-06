package com.component.circleshape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleShape extends View {

    private int progressStartColor;
    private int progressEndColor;
    private int bgColor;
    private int progress;
    private float progressWidth;
    private int startAngle;
    private int sweepAngle;
    private boolean showAnim;

    private int mMeasureHeight;
    private int mMeasureWidth;

    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    private RectF pRectF;

    private float unitAngle;

    private int curProgress = 0;

    private final int START_ANGLE_DEF = -90;
    private final int SWEEP_ANGLE_DEF = 360;
    private final int COLOR_DEF = Color.WHITE;

    public CircleShape(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleShape);
        progressStartColor = ta.getColor(R.styleable.CircleShape_pr_progress_start_color, COLOR_DEF);
        progressEndColor = ta.getColor(R.styleable.CircleShape_pr_progress_end_color, progressStartColor);
        bgColor = ta.getColor(R.styleable.CircleShape_pr_bg_color, COLOR_DEF);
        progress = ta.getInt(R.styleable.CircleShape_pr_progress, 0);
        progressWidth = ta.getDimension(R.styleable.CircleShape_pr_progress_width, 0);
        startAngle = ta.getInt(R.styleable.CircleShape_pr_start_angle, START_ANGLE_DEF);
        sweepAngle = ta.getInt(R.styleable.CircleShape_pr_sweep_angle, SWEEP_ANGLE_DEF);
        showAnim = ta.getBoolean(R.styleable.CircleShape_pr_show_anim, false);
        ta.recycle();

        unitAngle = (float) (sweepAngle / 100.0);

        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.BUTT);
        bgPaint.setStrokeWidth(progressWidth);

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.BUTT);
        progressPaint.setStrokeWidth(progressWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();
        if (pRectF == null) {
            float halfProgressWidth = progressWidth / 2;
            pRectF = new RectF(halfProgressWidth + getPaddingLeft(),
                    halfProgressWidth + getPaddingTop(),
                    mMeasureWidth - halfProgressWidth - getPaddingRight(),
                    mMeasureHeight - halfProgressWidth - getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!showAnim) {
            curProgress = progress;
        }

        drawBg(canvas);
        drawProgress(canvas);

        if (curProgress < progress) {
            curProgress++;
            postInvalidate();
        }

    }

    private void drawBg(Canvas canvas) {
        bgPaint.setColor(bgColor);
        canvas.drawArc(pRectF, startAngle, sweepAngle, false, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        for (int i = 0, end = (int) (curProgress * unitAngle); i < end; i++) {
            progressPaint.setColor(getGradient(i / (float) end, progressStartColor, progressEndColor));
            canvas.drawArc(pRectF,
                    startAngle + i,
                    2,
                    false,
                    progressPaint);
        }
    }

    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setColor(int bgColor, int progressStartColor, int progressEndColor) {
        this.bgColor = getResources().getColor(bgColor);
        this.progressStartColor = getResources().getColor(progressStartColor);
        this.progressEndColor = getResources().getColor(progressEndColor);
        invalidate();
    }

    public int getGradient(float fraction, int startColor, int endColor) {
        if (fraction > 1) fraction = 1;
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaDifference = alphaEnd - alphaStart;
        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        int redCurrent = (int) (redStart + fraction * redDifference);
        int blueCurrent = (int) (blueStart + fraction * blueDifference);
        int greenCurrent = (int) (greenStart + fraction * greenDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}