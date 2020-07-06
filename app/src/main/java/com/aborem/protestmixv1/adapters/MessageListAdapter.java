package com.aborem.protestmixv1.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.models.MessageModelWrapper;

import java.util.Date;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<MessageModelWrapper> messageModelWrapperList;

    public MessageListAdapter(Context context, List<MessageModelWrapper> messageModelWrappers) {
        this.messageModelWrapperList = messageModelWrappers;
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
        MessageModelWrapper messageModelWrapper = messageModelWrapperList.get(position);
        if (messageModelWrapper.isSentByUser()) {
            ((SentMessageHolder) holder).bind(messageModelWrapper);
        } else {
            ((ReceivedMessageHolder) holder).bind(messageModelWrapper);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageModelWrapper messageModelWrapper = messageModelWrapperList.get(position);
        if (messageModelWrapper.isSentByUser()) {
            return VIEW_TYPE_MESSAGE_SENT;
        }
        return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @Override
    public int getItemCount() {
        return messageModelWrapperList.size();
    }

    // - Classes for message view holders

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageModelWrapper messageModelWrapper) {
            messageText.setText(messageModelWrapper.getText());
            timeText.setText(DateFormat.format("hh:mm", messageModelWrapper.getCreatedAt()));
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageModelWrapper messageModelWrapper) {
            messageText.setText(messageModelWrapper.getText());
            timeText.setText(DateFormat.format("hh:mm", messageModelWrapper.getCreatedAt()));
        }

    }
}
