package com.example.chatux.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatux.R;
import com.example.chatux.models.MessageModel;
import com.example.chatux.models.MessageTypes;
import com.example.chatux.viewholder.BaseMessageViewHolder;
import com.example.chatux.viewholder.IncomingMessageViewHolder;
import com.example.chatux.viewholder.OutgoingMessageViewHolder;
import com.example.chatux.viewholder.OutgoingReplyViewHolder;
import com.example.chatux.viewholder.TypingMessageViewHolder;

import java.util.List;

/**
 * Created by Amit kumar karn on 09-11-2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();
    public List<MessageModel> items;
    private Context context;

    public MessageAdapter(Context context, List<MessageModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = new View(context);
        switch (viewType) {
            case MessageTypes.OUTGOING_REPLY:
                view = inflater.inflate(R.layout.layout_outgoing_reply, parent, false);
                return new OutgoingReplyViewHolder(view, this);
            case MessageTypes.OUTGOING_MESSAGE:
                view = inflater.inflate(R.layout.layout_outgoing_message, parent, false);
                return new OutgoingMessageViewHolder(view, this);
            case MessageTypes.INCOMING_MESSAGE:
                view = inflater.inflate(R.layout.incoming_message, parent, false);
                return new IncomingMessageViewHolder(view, this);
            case MessageTypes.TYPING_MESSAGE:
                view = inflater.inflate(R.layout.layout_typing_message, parent, false);
                return new TypingMessageViewHolder(view, this);
        }
        return new BaseMessageViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = items.get(position);
        switch (messageModel.getMessageType()) {
            case MessageTypes.OUTGOING_MESSAGE:
                ((OutgoingMessageViewHolder) holder).configure(position);
                break;
            case MessageTypes.INCOMING_MESSAGE:
                ((IncomingMessageViewHolder) holder).configure(position);
                break;
            case MessageTypes.TYPING_MESSAGE:
                ((TypingMessageViewHolder) holder).configure(position);
                break;
            case MessageTypes.OUTGOING_REPLY:
                ((OutgoingReplyViewHolder) holder).configure(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getMessageType();
    }

    public void addMessage(MessageModel message) {
        Log.d(TAG, "addMessage: " + message);
        try {
            if (getItemViewType(getItemCount() - 1) == MessageTypes.TYPING_MESSAGE) {
                this.items.remove(getItemCount() - 1);
                notifyItemRemoved(getItemCount() - 1);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("Array index", e.toString());
        }
        this.items.add(message);
        notifyItemInserted(getItemCount() - 1);
    }

    public void setMessages(List<MessageModel> messages) {
        Log.d(TAG, "setMessages: " + messages);
        this.items = messages;
        notifyDataSetChanged();
    }
}

