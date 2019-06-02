package com.example.aesophor.dingdong.ui.chats;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.ui.messaging.MessageAdapter;

public class ChatsFragment extends Fragment {

    private MessageAdapter msgAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgAdapter = new MessageAdapter(getContext());
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
        final Activity mainActivity = (Activity) getContext();
        ListView messageListView = mainActivity.findViewById(R.id.messageListView);
        messageListView.setAdapter(msgAdapter);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //msgAdapter.add(new Message(new User("aesophor", "Marco"), "hi", true));
                //msgAdapter.add(new Message(new User("jimmy586586", "Jim"), "there", false));
            }
        });
    }

}