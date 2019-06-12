package com.example.aesophor.dingdong.ui.messaging;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.event.EventListener;
import com.example.aesophor.dingdong.event.EventManager;
import com.example.aesophor.dingdong.event.EventType;
import com.example.aesophor.dingdong.event.message.NewMessageEvent;
import com.example.aesophor.dingdong.message.Message;
import com.example.aesophor.dingdong.user.User;

public class MessagingFragment extends Fragment {

    private MessageAdapter msgAdapter;
    private User targetUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgAdapter = new MessageAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Activity activity = (Activity) getContext();
        final ListView messageListView = activity.findViewById(R.id.messageListView);
        messageListView.setAdapter(msgAdapter);

        ImageButton sendButton = activity.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = activity.findViewById(R.id.messageEditText);
                final String msg = editText.getText().toString();
                editText.setText("");

                final MessengerActivity activity = (MessengerActivity) getContext();
                Message.create(activity.getUser().getUsername(), targetUser.getUsername(), msg);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        User currentUser = activity.getUser();
                        msgAdapter.add(new Message(currentUser, targetUser, msg, false, true));
                        messageListView.setSelection(msgAdapter.getCount() - 1);
                    }
                });
            }
        });


        EventManager.getInstance().addEventListener(EventType.NEW_MESSAGE, new EventListener<NewMessageEvent>() {
            @Override
            public void handle(final NewMessageEvent e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = e.getMessage();
                        msgAdapter.add(new Message(msg.getSender(), msg.getReceiver(), msg.getContent(), false, false));
                        messageListView.setSelection(msgAdapter.getCount() - 1);
                    }
                });
            }
        });

        update();
        messageListView.setSelection(msgAdapter.getCount() - 1);
    }


    public void update() {
        MessengerActivity activity = (MessengerActivity) getContext();
        User currentUser = activity.getUser();

        final List<Message> messages = Message.getMessagesBetween(currentUser, targetUser);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Message message : messages) {
                    msgAdapter.add(message);
                }
            }
        });
    }


    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User user) {
        targetUser = user;
    }

}