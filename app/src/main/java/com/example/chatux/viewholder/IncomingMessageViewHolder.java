package com.example.chatux.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.chatux.R;
import com.example.chatux.adapters.MessageAdapter;
import com.example.chatux.models.MessageModel;

/**
 * Created by AMIT KUMAR KARN on 12-11-2017.
 */

public class IncomingMessageViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    private MessageAdapter messageAdapter;

    public IncomingMessageViewHolder(View itemView, MessageAdapter messageAdapter) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.incoming_message_text);
        this.messageAdapter = messageAdapter;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void configure(int position) {
        MessageModel incomingTextMessage = messageAdapter.items.get(position);
        if (incomingTextMessage != null) {
            getTextView().setText(incomingTextMessage.message);
        }
    }
}
