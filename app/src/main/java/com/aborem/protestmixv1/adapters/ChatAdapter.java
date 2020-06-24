package com.aborem.protestmixv1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aborem.protestmixv1.models.Chat;
import com.aborem.protestmixv1.activities.ConversationActivity;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.models.ContactModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ContactModel> chats;
    private Context context;

    public ChatAdapter(List<ContactModel> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatNameTextView;
        LinearLayout containerLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
        }

        public void bind(final ContactModel chat) {
            chatNameTextView.setText(chat.getPhoneNumber());
            containerLayout.setOnClickListener(view -> {
                ConversationActivity.start(context, chat.getContactId(), chat.getPhoneNumber());
            });
        }
    }
}
