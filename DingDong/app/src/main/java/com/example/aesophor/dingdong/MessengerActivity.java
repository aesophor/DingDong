package com.example.aesophor.dingdong;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.aesophor.dingdong.network.Response;
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
        setContentView(R.layout.activity_messenger);

        user = new User(getIntent().getStringExtra("username"));
        Log.i("MessengerActivity.java", "logged in as: " + user.getUsername());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.signOut: {
                signOut();
                return true;
            }
            case R.id.about: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void signOut() {
        try {
            Response signOut = user.logout();

            switch (signOut.getStatusCode()) {
                case SUCCESS: {
                    startActivity(new Intent(MessengerActivity.this, SignInActivity.class));
                    break;
                }
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
