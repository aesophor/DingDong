package com.example.aesophor.dingdong.ui.chats;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.message.Message;

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

        convertView = messageInflater.inflate(R.layout.msg_bubble_others, null);
        holder.avatar = convertView.findViewById(R.id.avatar);
        holder.name = convertView.findViewById(R.id.name);
        holder.message = convertView.findViewById(R.id.message_body);
        convertView.setTag(holder);

        holder.name.setText(message.getSender().getUsername());
        holder.message.setText(message.getContent());
        holder.unreadCount.setText("5");

        return convertView;
    }

    private class ChatViewHolder {
        public View avatar;
        public TextView name;
        public TextView message;
        public View unreadBubble;
        public TextView unreadCount;
    }

}