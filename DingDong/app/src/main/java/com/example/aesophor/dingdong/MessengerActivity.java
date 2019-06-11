package com.example.aesophor.dingdong;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.example.aesophor.dingdong.network.Response;
import com.example.aesophor.dingdong.network.SocketServer;
import com.example.aesophor.dingdong.network.StatusCode;
import com.example.aesophor.dingdong.ui.Fragments;
import com.example.aesophor.dingdong.ui.chats.ChatsFragment;
import com.example.aesophor.dingdong.ui.messaging.MessagingFragment;
import com.example.aesophor.dingdong.ui.friends.FriendsFragment;
import com.example.aesophor.dingdong.ui.settings.SettingsFragment;
import com.example.aesophor.dingdong.user.User;

public class MessengerActivity extends AppCompatActivity {

    private ExecutorService executor; // for running SocketServer on a dedicated thread
    private List<Fragment> fragments;
    private User user; // signed in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        executor = Executors.newCachedThreadPool();
        executor.execute(SocketServer.getInstance());

        user = new User(getIntent().getStringExtra("username"));
        Log.i("MessengerActivity.java", "logged in as: " + user.getUsername());

        fragments = new ArrayList<>(Fragments.SIZE.ordinal());
        fragments.add(new FriendsFragment());
        fragments.add(new ChatsFragment());
        fragments.add(new MessagingFragment());
        fragments.add(new SettingsFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        ActionBar bar = getSupportActionBar();
                        switch (item.getItemId()) {
                            case R.id.navigation_friends: {
                                show(Fragments.FRIENDS);
                                return true;
                            }
                            case R.id.navigation_chats: {
                                show(Fragments.CHATS);
                                return true;
                            }
                            case R.id.navigation_settings: {
                                show(Fragments.SETTINGS);
                                return true;
                            }
                            default:
                                return false;
                        }
                    }
                });

        // Show the first segment.
        show(Fragments.FRIENDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        signOut();
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
            case R.id.addFriend: {
                addFriend();
                return true;
            }
            case R.id.signOut: {
                signOut();
                return true;
            }
            case R.id.about: {
                new AlertDialog.Builder(MessengerActivity.this)
                        .setTitle("DingDong")
                        .setMessage(
                          "A chat application created with RESTful architecture.\n\n"
                        + "University of Taipei Android App Development 2019 Spring semester project "
                        + "developed by Marco Wang and Ariel Hsu.")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void show(Fragments fragment) {
        ActionBar bar = getSupportActionBar();
        switch (fragment) {
            case FRIENDS:
                bar.setTitle("Friends");
                break;
            case CHATS:
                bar.setTitle("Chats");
                break;
            case SETTINGS:
                bar.setTitle("Settings");
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragments.get(fragment.ordinal()));
        transaction.commit();
    }


    private void signOut() {
        try {
            Response signOut = user.logout();

            switch (signOut.getStatusCode()) {
                case SUCCESS: {
                    startActivity(new Intent(MessengerActivity.this, SignInActivity.class));
                    executor.shutdown();
                    break;
                }
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addFriend() {
        final EditText usernameField = new EditText(MessengerActivity.this);

        new AlertDialog.Builder(MessengerActivity.this)
                .setTitle("Add Friend")
                .setMessage("Please enter the friend's username")
                .setView(usernameField)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String friendUsername = usernameField.getText().toString();
                        Response result = user.addFriend(friendUsername);

                        if (result.success()) {
                            FriendsFragment fragment = (FriendsFragment) fragments.get(Fragments.FRIENDS.ordinal());
                            fragment.update();
                        } else if (result.getStatusCode() == StatusCode.NOT_REGISTERED) {
                            new AlertDialog.Builder(MessengerActivity.this)
                                    .setTitle("Failed")
                                    .setMessage("The specified username doesn't exist")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        } else {
                            new AlertDialog.Builder(MessengerActivity.this)
                                    .setTitle("Error")
                                    .setMessage(result.getStatusCode().toString())
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    public Fragment getFragment(Fragments fragment) {
        return fragments.get(fragment.ordinal());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}