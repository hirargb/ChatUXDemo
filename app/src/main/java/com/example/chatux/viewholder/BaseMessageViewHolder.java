package com.example.chatux.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chatux.adapters.MessageAdapter;

/**
 * Created by AMIT KUMAR KARN on 12-11-2017.
 */

public class BaseMessageViewHolder extends RecyclerView.ViewHolder {
    private MessageAdapter messageAdapter;

    public BaseMessageViewHolder(View itemView, MessageAdapter messageAdapter) {
        super(itemView);
        this.messageAdapter = messageAdapter;
    }
}
