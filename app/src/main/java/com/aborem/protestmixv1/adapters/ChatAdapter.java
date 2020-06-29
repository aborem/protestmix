package com.aborem.protestmixv1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.conversation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    /**
     * Class responsible for maintaining recycler view of all the conversation contact cards
     */
    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPhoneNumber;
        TextView textViewUnreadMessages;
        RelativeLayout cardViewRelativeLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPhoneNumber = itemView.findViewById(R.id.text_view_phone_number);
            textViewUnreadMessages = itemView.findViewById(R.id.text_view_unread_messages);
            cardViewRelativeLayout = itemView.findViewById(R.id.card_view_relative_layout);
        }

        public void bind(final ContactModel chat) {
            textViewPhoneNumber.setText(chat.getPhoneNumber());
            // todo below: figure out how to get number of unread messages from this. Probably have to store that in the contacts table somehow
            textViewUnreadMessages.setText("");
            cardViewRelativeLayout.setOnClickListener(view -> {
                ConversationActivity.start(context, chat.getContactId(), chat.getPhoneNumber());
            });
        }
    }
}
