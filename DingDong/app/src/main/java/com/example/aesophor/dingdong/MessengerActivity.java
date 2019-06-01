package com.example.aesophor.dingdong;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.example.aesophor.dingdong.ui.ChatsFragment;
import com.example.aesophor.dingdong.ui.FriendsFragment;
import com.example.aesophor.dingdong.ui.SettingsFragment;
import com.example.aesophor.dingdong.user.User;

public class MessengerActivity extends AppCompatActivity {

    private User user;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger_activity);

        user = new User(getIntent().getStringExtra("username"));

        fragments = new ArrayList<>();
        fragments.add(new FriendsFragment());
        fragments.add(new ChatsFragment());
        fragments.add(new SettingsFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_friends: {
                                selectedFragment = fragments.get(0);
                                break;
                            }
                            case R.id.navigation_chats: {
                                selectedFragment = fragments.get(1);
                                break;
                            }
                            case R.id.navigation_settings: {
                                selectedFragment = fragments.get(2);
                                break;
                            }
                            default:
                                break;
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        // Show the first segment.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragments.get(0));
        transaction.commit();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
