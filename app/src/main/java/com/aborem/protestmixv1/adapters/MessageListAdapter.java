package com.aborem.protestmixv1.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.models.MessageWrapper;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<MessageWrapper> messageWrapperList;

    public MessageListAdapter(Context context, List<MessageWrapper> messageWrappers) {
        this.messageWrapperList = messageWrappers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        }
        // assumes VIEW_TYPE_MESSAGE_RECEIVED
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_received, parent, false);
        return new ReceivedMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageWrapper messageWrapper = messageWrapperList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(messageWrapper);
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(messageWrapper);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageWrapper messageWrapper = messageWrapperList.get(position);
        if (messageWrapper.isSentByUser()) {
            return VIEW_TYPE_MESSAGE_SENT;
        }
        return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @Override
    public int getItemCount() {
        return messageWrapperList.size();
    }

    /**
     * Adds MessageWrapper to messageWrapperList and notifies of data set change
     * @param messageWrapper the newly added message
     */
    public void addItem(MessageWrapper messageWrapper) {
        messageWrapperList.add(messageWrapper);
        notifyDataSetChanged();
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageWrapper messageWrapper) {
            messageText.setText(messageWrapper.getText());
            timeText.setText(DateFormat.format("hh:mm", messageWrapper.getCreatedAt()));
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageWrapper messageWrapper) {
            messageText.setText(messageWrapper.getText());
            timeText.setText(DateFormat.format("hh:mm", messageWrapper.getCreatedAt()));
        }

    }
}
