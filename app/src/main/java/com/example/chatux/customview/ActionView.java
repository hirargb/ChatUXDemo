package com.example.chatux.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(ActionButtonBehavior.class)
public class ActionView extends FlexboxLayout {
    private static final int DURATION = 350;
    private static final int DURATION_STAGGER = 50;
    private static final int FADE_DELAY = 175;
    private static final String TAG = "ActionView";
    private AnimatorSet hideAnimatorSet = new AnimatorSet();
    private boolean isAnimating;
    private boolean shouldShow = true;
    private AnimatorSet showAnimatorSet = new AnimatorSet();

    public ActionView(Context context) {
        super(context);
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void reset() {
        boolean z = false;
        if (getChildCount() == 0) {
            this.isAnimating = false;
            return;
        }
        if (this.showAnimatorSet.isRunning() || this.hideAnimatorSet.isRunning()) {
            z = true;
        }
        this.isAnimating = z;
    }

    public void hide(boolean force) {
        reset();
        if (force) {
            forceHide();
        } else if (this.shouldShow) {
            this.shouldShow = false;
            if (!this.isAnimating) {
                checkForAnimations();
            }
        }
    }

    public void show() {
        reset();
        if (!this.shouldShow) {
            this.shouldShow = true;
            if (!this.isAnimating) {
                checkForAnimations();
            }
        }
    }

    private void checkForAnimations() {
        boolean visible = getVisibility() == VISIBLE;
        if (visible && !this.shouldShow) {
            animateHide();
        } else if (!visible && this.shouldShow) {
            animateShow();
        }
    }

    private void forceHide() {
        this.shouldShow = false;
        setVisibility(GONE);
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            view.clearAnimation();
            view.setTranslationY((float) getHeight());
            view.setAlpha(0.0f);
        }
    }

    private void animateHide() {
        Log.d(TAG, "hide: ");
        List<Animator> animators = new ArrayList();
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            view.clearAnimation();
            ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, (float) getHeight());
            slideOut.setInterpolator(new AnticipateInterpolator(1.0f));
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
            fadeOut.setInterpolator(new DecelerateInterpolator());
            fadeOut.setStartDelay(175);
            AnimatorSet fadeAndSlideOut = new AnimatorSet();
            fadeAndSlideOut.setDuration(350);
            fadeAndSlideOut.setStartDelay((long) (index * 50));
            fadeAndSlideOut.playTogether(slideOut, fadeOut);
            if (index == 0) {
                fadeAndSlideOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ActionView.this.setVisibility(GONE);
                        ActionView.this.checkForAnimations();
                    }
                });
            }
            animators.add(fadeAndSlideOut);
        }
        this.hideAnimatorSet = new AnimatorSet();
        this.hideAnimatorSet.playTogether(animators);
        this.hideAnimatorSet.start();
    }

    private void animateShow() {
        Log.d(TAG, "show: ");
        List<Animator> animators = new ArrayList();
        for (int index = 0; index < getChildCount(); index++) {
            View view = getChildAt(index);
            view.clearAnimation();
            view.setAlpha(0.0f);
            ObjectAnimator slideIn = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0.0f);
            slideIn.setInterpolator(new OvershootInterpolator(1.0f));
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            AnimatorSet fadeAndSlideIn = new AnimatorSet();
            fadeAndSlideIn.setDuration(350);
            fadeAndSlideIn.setStartDelay((long) (index * 50));
            fadeAndSlideIn.playTogether(slideIn, fadeIn);
            if (index == 0) {
                fadeAndSlideIn.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ActionView.this.checkForAnimations();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        ActionView.this.setVisibility(View.VISIBLE);
                    }
                });
            }
            animators.add(fadeAndSlideIn);
        }
        this.showAnimatorSet = new AnimatorSet();
        this.showAnimatorSet.playTogether(animators);
        this.showAnimatorSet.start();
    }
}
