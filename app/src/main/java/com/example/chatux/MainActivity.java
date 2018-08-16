package com.example.chatux;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chatux.adapters.MessageAdapter;
import com.example.chatux.customview.ActionView;
import com.example.chatux.customview.BackgroundRipple;
import com.example.chatux.customview.BaseAnimationListener;
import com.example.chatux.customview.MessageAnimator;
import com.example.chatux.models.MessageModel;
import com.example.chatux.models.MessageTypes;
import com.example.chatux.models.UserAction;
import com.example.chatux.viewholder.OutgoingReplyViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit kumar karn on 09-11-2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final long ACTION_BUTTON_STAGGER = 200;
    public static final long ACTION_BUTTON_START_DELAY = 100;
    public static final int BOTTOM_BUFFER = 40;
    public static final int DELAY_BETWEEN_MESSAGES = 500;
    public static final long DURATION_ACTION_BUTTON_FLY = 450;
    public static final int FADE_DURATION = 200;
    public static final int TENSION = 1;
    public static final String KEY_SCROLL_POSITION = "SCROLL_POSITION";
    private static final float DELAY_MULTIPLIER = 0.008f;
    private static final int IMAGE_DELAY = 1200;
    private static final int MAX_DELAY = 2000;
    private static final int MIN_DELAY = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.chat_action_view)
    public ActionView actionView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.backgroundRipple)
    BackgroundRipple ripple;
    @BindView(R.id.inputMessagebar)
    AutoCompleteTextView inputMessagebar;
    @BindView(R.id.sendMessageButton)
    ImageButton sendMessageButton;
    private MessageAdapter messageAdapter;
    private MessageAnimator messageAnimator;
    private List<MessageModel> messageModelList;
    private LinearLayoutManager linearLayoutManager;
    private Parcelable scrollState;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.scrollState = savedInstanceState.getParcelable(KEY_SCROLL_POSITION);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpRecyclerView();
        sendMessageButton.setOnClickListener(this);

    }


    private void setUpRecyclerView() {
        messageModelList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(this.linearLayoutManager);
        this.messageAdapter = new MessageAdapter(this, messageModelList);
        this.recyclerView.setAdapter(this.messageAdapter);
        this.messageAnimator = new MessageAnimator();
        this.recyclerView.setItemAnimator(this.messageAnimator);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (atBottomOfList()) {
                    recyclerView.setPadding(0, 0, 0, actionView.getHeight());
                    actionView.show();
                    return;
                }
                actionView.hide(false);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.scrollState = this.linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_SCROLL_POSITION, this.scrollState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMessageButton:
                if (TextUtils.isEmpty(inputMessagebar.getText())) {
                    Toast.makeText(this, "Enter something dude", Toast.LENGTH_SHORT).show();
                } else {
                    MessageModel messageModel = new MessageModel();
                    messageModel.setMessage(inputMessagebar.getText().toString());
                    messageModel.setMessageType(MessageTypes.OUTGOING_REPLY); // Change MessageTypes for different types of message view holder
                    inputMessagebar.setText("");
                    messageAdapter.addMessage(messageModel);

                    List<UserAction> userActions = new ArrayList<>();
                    UserAction userAction = new UserAction();
                    userAction._message = messageModel;
                    userActions.add(userAction);
                    userActions.add(userAction);
                    showUserActions(userActions);
                }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.handler.removeCallbacksAndMessages(null);
    }

    private boolean atBottomOfList() {
        return this.recyclerView.computeVerticalScrollRange() - (this.recyclerView.computeVerticalScrollExtent() + this.recyclerView.computeVerticalScrollOffset()) < 40;
    }


    public void showUserActions(List<UserAction> userActions) {
        this.actionView.removeAllViews();
        long delay = 100;
        boolean first = true;
        for (UserAction userAction : userActions) {
            View buttonWrapper = LayoutInflater.from(this).inflate(R.layout.button_action, this.actionView, false);
            this.actionView.addView(buttonWrapper);
            final Button actionButton = ButterKnife.findById(buttonWrapper, R.id.button);
            actionButton.setText(userAction._message.getMessage());
            final long startDelay = delay;
            delay += 200;
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleActionButtonClick(actionButton);
                    int[] actionButtonPos = new int[2];
                    actionButton.getLocationInWindow(actionButtonPos);
                    ripple.performRipple(actionButtonPos[0] + (actionButton.getWidth() / 2), actionButtonPos[1] + (actionButton.getHeight() / 2), R.color.ripple_color);
                }
            });
            final boolean finalFirst = first;
            this.actionView.post(new Runnable() {
                @Override
                public void run() {
                    Animator appearingAnimator = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.action_view_enter);
                    appearingAnimator.setTarget(actionButton);
                    appearingAnimator.setStartDelay(startDelay);
                    appearingAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            if (finalFirst) {
                                adjustForUserActions();
                                actionView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!atBottomOfList()) {
                                            actionView.setVisibility(View.GONE);
                                            actionView.hide(true);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    appearingAnimator.start();
                }
            });
            first = false;
        }
    }

    private void handleActionButtonClick(final Button actionButton) {
        Log.d(TAG, "Action button clicked");
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(messageAdapter.getItemCount() - 1);
                if (viewHolder instanceof OutgoingReplyViewHolder) {
                    final OutgoingReplyViewHolder outgoingReplyViewHolder = (OutgoingReplyViewHolder) viewHolder;
                    View messageView = outgoingReplyViewHolder.getTextView();
                    int[] actionButtonPos = new int[2];
                    actionButton.getLocationInWindow(actionButtonPos);
                    int startX = actionButtonPos[0];
                    int startY = actionButtonPos[1];
                    int[] newMessagePos = new int[2];
                    messageView.getLocationInWindow(newMessagePos);
                    int x = newMessagePos[0] - startX;
                    int y = newMessagePos[1] - startY;
                    AnimatorSet flySet = new AnimatorSet();
                    Animator flyX = ObjectAnimator.ofFloat(actionButton, View.TRANSLATION_X, (float) x);
                    Animator flyY = ObjectAnimator.ofFloat(actionButton, View.TRANSLATION_Y, (float) y);
                    flySet.playTogether(flyX, flyY);
                    flySet.setDuration(250);
                    flySet.setInterpolator(new OvershootInterpolator(1.0f));
                    flySet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            outgoingReplyViewHolder.itemView.setVisibility(View.VISIBLE);
                        }
                    });
                    AnimatorSet flyAndFadeSet = new AnimatorSet();
                    ObjectAnimator fade = ObjectAnimator.ofFloat(actionButton, View.ALPHA, new float[]{0.0f}).setDuration(200);
                    flyAndFadeSet.playSequentially(flySet, fade);
                    final Button button = actionButton;
                    flyAndFadeSet.addListener(new BaseAnimationListener() {
                        public void onAnimationEnd(Animator animation) {
                            button.setAlpha(0.0f);
                            MainActivity.this.hideUserActions();
                        }
                    });
                    flyAndFadeSet.start();
                    hideOtherActions(actionButton, false);
                }
            }
        }, this.messageAnimator.getAddDuration());
    }

    public void hideUserActions() {
        hideOtherActions(null, true);
    }

    public void hideOtherActions(@Nullable View v, boolean removeFromLayout) {
        for (int i = this.actionView.getChildCount() - 1; i >= 0; i--) {
            if (v != this.actionView.getChildAt(i)) {
                hideAction(this.actionView.getChildAt(i), removeFromLayout);
            }
        }
    }

    public void hideAction(final View v, boolean removeFromLayout) {
        Animator disappearingAnimator = AnimatorInflater.loadAnimator(this, R.animator.action_view_exit);
        disappearingAnimator.setInterpolator(new AnticipateInterpolator(1.0f));
        disappearingAnimator.setTarget(v);
        disappearingAnimator.setDuration(300);
        if (removeFromLayout) {
            disappearingAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    MainActivity.this.actionView.removeView(v);
                }
            });
        }
        disappearingAnimator.start();
    }

    public void insertActionMessage(MessageModel actionMessage) {
        this.messageAdapter.addMessage(actionMessage);
        scrollToBottom();
    }

    public void scrollToBottom() {
        this.recyclerView.scrollToPosition(this.messageAdapter.getItemCount() - 1);
    }

    public void adjustForUserActions() {
        boolean atBottom = atBottomOfList();
        this.recyclerView.setPadding(0, 0, 0, this.actionView.getHeight());
        if (atBottom) {
            scrollToBottom();
        }
    }

    public void addMessages(List<MessageModel> messages) {
        long delay;
        Log.d(TAG, "adding messages: " + messages.toString());
        if (this.messageAdapter.getItemCount() == 0) {
            delay = 0;
        } else {
            delay = this.messageAnimator.getAddDuration() + 450;
        }
        for (int i = 0; i < messages.size(); i++) {
            final MessageModel message = messages.get(i);
            long delayMillis = calculateDelayMillis(message);
            long addDuration = this.recyclerView.getItemAnimator().getAddDuration();
            long typingViewDelay = delayMillis + addDuration;
            this.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertMessage(message);
                }
            }, typingViewDelay + delay);
            long nextMessageDelay = typingViewDelay + this.recyclerView.getItemAnimator().getRemoveDuration();
            scrollToBottom();
            delay += (nextMessageDelay + addDuration) + 500;
        }

    }


    public void insertMessage(MessageModel message) {
        this.messageAdapter.addMessage(message);
        if (this.linearLayoutManager.findLastVisibleItemPosition() == this.linearLayoutManager.getItemCount() - 2) {
            scrollToBottom();
        }
    }

    public void setMessages(List<MessageModel> messages) {
        this.messageAdapter.setMessages(messages);
        if (this.scrollState == null) {
            scrollToBottom();
            return;
        }
        this.linearLayoutManager.onRestoreInstanceState(this.scrollState);
        this.scrollState = null;
    }

    private long calculateDelayMillis(MessageModel messageModel) {
        long delayInMillis = (long) (1000.0f * (DELAY_MULTIPLIER * ((float) (messageModel.getMessage() != null ? messageModel.getMessage().length() : 0))));
        if (delayInMillis < 1000) {
            return 1000;
        }
        if (delayInMillis > 2000) {
            return 2000;
        }
        return delayInMillis;
    }
}
