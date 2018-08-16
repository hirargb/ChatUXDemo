package com.example.chatux.customview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

public class ActionButtonBehavior extends CoordinatorLayout.Behavior<FlexboxLayout> {
    public ActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        child.setTranslationY(Math.min(0.0f, dependency.getTranslationY() - ((float) dependency.getHeight())));
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        child.setTranslationY(0.0f);
    }

    /*
    public boolean layoutDependsOn(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        child.setTranslationY(Math.min(0.0f, dependency.getTranslationY() - ((float) dependency.getHeight())));
        return true;
    }

    public void onDependentViewRemoved(CoordinatorLayout parent, FlexboxLayout child, View dependency) {
        child.setTranslationY(0.0f);
    }*/
}
