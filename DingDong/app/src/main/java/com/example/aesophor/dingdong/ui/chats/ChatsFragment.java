package com.example.aesophor.dingdong.ui.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.message.Message;
import com.example.aesophor.dingdong.user.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatAdapter = new ChatAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        update();
    }

    public void update() {
        chatAdapter.clear();

        final MessengerActivity activity = (MessengerActivity) getContext();
        ListView chatsListView = activity.findViewById(R.id.chatsListView);
        chatsListView.setAdapter(chatAdapter);

        final User currentUser = activity.getUser();
        final List<User> friends = currentUser.getFriends();
        final List<Message> chatMsgs = new ArrayList<>();

        for (User friend : friends) {
            final List<Message> messages = Message.getMessagesBetween(currentUser, friend);
            chatMsgs.add(messages.get(messages.size() - 1));
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Message message : chatMsgs) {
                    chatAdapter.add(message);
                }
            }
        });
    }

}