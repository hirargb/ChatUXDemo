package com.example.chatux.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.example.chatux.R;


public class BackgroundRipple extends View {
    public static final float ALPHA = 0.65f;
    public static final int DURATION = 400;
    private final Paint paint = new Paint(1);
    private int currentX;
    private int currentY;
    private float radius;
    private Property<BackgroundRipple, Float> radiusProperty = new Property<BackgroundRipple, Float>(Float.class, "radius") {
        public Float get(BackgroundRipple object) {
            return object.getRadius();
        }

        public void set(BackgroundRipple object, Float value) {
            object.setRadius(value);
        }
    };

    public BackgroundRipple(Context context) {
        super(context);
    }

    public BackgroundRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundRipple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void performRipple(int x, int y, @ColorRes int color) {
        this.paint.setColor(ContextCompat.getColor(getContext(), color));
        this.currentX = x;
        this.currentY = y;
        setRadius(0.0f);
        setAlpha(ALPHA);
        ObjectAnimator ripple = ObjectAnimator.ofFloat(this, this.radiusProperty, this.radius, (float) getContext().getResources().getDimensionPixelSize(R.dimen.background_ripple_radius));
        ripple.setDuration(400);
        ripple.setInterpolator(new DecelerateInterpolator());
        ripple.addListener(new C04301());
        ripple.start();
        ObjectAnimator fade = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f);
        fade.setInterpolator(new AccelerateInterpolator());
        fade.setDuration(400);
        fade.addListener(new C04312());
        fade.start();
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) this.currentX, (float) this.currentY, this.radius, this.paint);
        super.draw(canvas);
    }

    private float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    class C04301 extends AnimatorListenerAdapter {
        C04301() {
        }

        public void onAnimationEnd(Animator animation) {
            BackgroundRipple.this.setRadius(0.0f);
        }
    }

    class C04312 extends AnimatorListenerAdapter {
        C04312() {
        }

        public void onAnimationEnd(Animator animation) {
            BackgroundRipple.this.setAlpha(BackgroundRipple.ALPHA);
        }
    }
}

