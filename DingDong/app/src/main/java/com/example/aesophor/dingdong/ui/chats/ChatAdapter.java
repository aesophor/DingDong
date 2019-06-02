package com.example.aesophor.dingdong.ui.chats;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.message.Message;
import com.example.aesophor.dingdong.ui.Fragments;
import com.example.aesophor.dingdong.ui.messaging.MessagingFragment;
import com.example.aesophor.dingdong.user.User;
import com.example.aesophor.dingdong.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<>();
    Context context;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatViewHolder holder = new ChatViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        convertView = messageInflater.inflate(R.layout.layout_chat_item, null);
        holder.avatar = convertView.findViewById(R.id.avatar);
        holder.name = convertView.findViewById(R.id.name);
        holder.message = convertView.findViewById(R.id.message);
        holder.unreadBubble = convertView.findViewById(R.id.unreadBubble);
        holder.unreadCount = convertView.findViewById(R.id.unreadCount);
        convertView.setTag(holder);

        final User currentUser = ((MessengerActivity) context).getUser();
        final User targetUser = (currentUser.getUsername().equals(message.getReceiver().getUsername())) ? message.getSender() : message.getReceiver();
        holder.name.setText(targetUser.getFullname());
        ImageUtils.b64LoadImage(holder.avatar, targetUser.getB64Avatar());

        holder.message.setText(message.getContent());
        holder.unreadCount.setText("5");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user clicks on a friend, take them to the chat view.
                MessengerActivity messengerActivity = (MessengerActivity) context;
                MessagingFragment msgFragment = (MessagingFragment) messengerActivity.getFragment(Fragments.MESSAGING);

                msgFragment.setTargetUser(targetUser);
                messengerActivity.show(Fragments.MESSAGING);

                BottomNavigationView menu = messengerActivity.findViewById(R.id.navigation);
                menu.getMenu().getItem(1).setChecked(true);

                // Set the damn ActionBar god dammit
                ActionBar bar = ((MessengerActivity) context).getSupportActionBar();
                bar.setTitle(targetUser.getFullname());
            }
        });

        return convertView;
    }

    private class ChatViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView message;
        public View unreadBubble;
        public TextView unreadCount;
    }

}