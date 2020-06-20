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
import com.aborem.protestmixv1.ConversationActivity;
import com.aborem.protestmixv1.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chats;
    private Context context;

    public ChatAdapter(List<Chat> chats, Context context) {
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

        public void bind(final Chat chat) {
            chatNameTextView.setText(chat.getPhoneNumber());
            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo add phone number field for starter (not sure how to find this yet)
                    ConversationActivity.start(context, chat.getGuid(), chat.getPhoneNumber());
                }
            });
        }
    }
}
