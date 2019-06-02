package com.example.aesophor.dingdong.ui.friends;

import java.util.List;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.aesophor.dingdong.MessengerActivity;
import com.example.aesophor.dingdong.R;
import com.example.aesophor.dingdong.user.User;

public class FriendsFragment extends Fragment {

    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAdapter = new UserAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        update();
    }

    public void update() {
        final MessengerActivity activity = (MessengerActivity) getContext();
        final ListView messageListView = activity.findViewById(R.id.friendsListView);
        messageListView.setAdapter(userAdapter);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userAdapter.clear();
                userAdapter.add(activity.getUser());

                List<User> friends = ((MessengerActivity) getActivity()).getUser().getFriends();
                for (User friend : friends) {
                    userAdapter.add(friend);
                }

                // OnClickListener is defined in UserAdapter
            }
        });
    }

}