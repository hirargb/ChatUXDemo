package com.example.chatux.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.chatux.R;
import com.example.chatux.adapters.MessageAdapter;
import com.example.chatux.models.MessageModel;

public class OutgoingReplyViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public MessageAdapter messageAdapter;

    public OutgoingReplyViewHolder(View itemView, MessageAdapter messageAdapter) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.outgoing_message_text);
        this.messageAdapter = messageAdapter;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void configure(int position) {
        MessageModel outgoingReplyMessage = messageAdapter.items.get(position);
        if (outgoingReplyMessage != null) {
            if (!outgoingReplyMessage.getMessage().isEmpty()) {
                getTextView().setText(outgoingReplyMessage.getMessage());
            }
        }
    }
}

