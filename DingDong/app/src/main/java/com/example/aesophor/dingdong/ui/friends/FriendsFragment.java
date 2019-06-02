package com.example.aesophor.dingdong.ui.friends;

import android.app.Activity;
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

import java.util.List;

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
        Activity mainActivity = (Activity) getContext();
        ListView messageListView = mainActivity.findViewById(R.id.friendsListView);
        messageListView.setAdapter(userAdapter);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userAdapter.clear();
                userAdapter.add(new User("aesophor", "Marco"));

                List<User> friends = ((MessengerActivity) getActivity()).getUser().getFriends();
                for (User friend : friends) {
                    userAdapter.add(friend);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

}