package com.example.aesophor.dingdong;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
