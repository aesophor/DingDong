package com.example.aesophor.dingdong;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.aesophor.dingdong.message.Message;
import com.example.aesophor.dingdong.user.User;

import org.jetbrains.annotations.Nullable;

public class ChatsFragment extends Fragment {

    private MessageAdapter msgAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgAdapter = new MessageAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chats_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Activity mainActivity = (Activity) getContext();
        ListView messageListView = mainActivity.findViewById(R.id.messages_view);
        messageListView.setAdapter(msgAdapter);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgAdapter.add(new Message(new User("aesophor", "Marco"), "hi", true));
                msgAdapter.add(new Message(new User("jimmy586586", "Jim"), "there", false));
                msgAdapter.notifyDataSetChanged();
            }
        });
    }

}