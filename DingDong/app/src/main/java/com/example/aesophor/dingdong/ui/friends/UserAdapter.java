package com.example.aesophor.dingdong.ui.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.ui.Fragments;
import com.example.aesophor.dingdong.ui.chats.ChatsFragment;
import com.example.aesophor.dingdong.ui.messaging.MessagingFragment;
import com.example.aesophor.dingdong.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {

    List<User> users = new ArrayList<>();
    Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void add(User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        FriendViewHolder holder = new FriendViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final User user = users.get(i);

        convertView = messageInflater.inflate(R.layout.layout_user_profile_item, null);
        holder.avatar = (View) convertView.findViewById(R.id.avatar);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        convertView.setTag(holder);

        GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
        drawable.setColor(Color.parseColor("#a2c9b4"));
        holder.name.setText(user.getFullname());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked: " + user.getUsername());

                MessengerActivity messengerActivity = (MessengerActivity) context;
                MessagingFragment msgFragment = (MessagingFragment) messengerActivity.getFragment(Fragments.MESSAGING);

                msgFragment.setTargetUser(user);
                messengerActivity.show(Fragments.MESSAGING);
            }
        });

        return convertView;
    }

    private class FriendViewHolder {
        public View avatar;
        public TextView name;
    }

}