package com.nowandroid.musicvk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nowandroid.musicvk.R;
import com.nowandroid.musicvk.fragments.DownloadFragment;
import com.nowandroid.musicvk.fragments.PlayListFragment;
import com.nowandroid.musicvk.fragments.PlayListOnlineFragment;
import com.nowandroid.musicvk.fragments.PopularFragment;
import com.nowandroid.musicvk.fragments.SearchFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends AppCompatActivity {

    private static final String[] sMyScope  = new String[]{
            VKScope.AUDIO,
            VKScope.NOHTTPS
    };
    private              boolean  isResumed = false;
    //private boolean isPlayingSong;
    //    private TextView  mTv;
    //    private ActionBar mAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        mAb = getSupportActionBar();
        //
        //        mTv = new TextView(getApplicationContext());

        //        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        //                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
        //                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView

        //        // Apply the layout parameters to TextView widget
        //        mTv.setLayoutParams(lp);
        //        mTv.setText("MusicVK");
        //        mTv.setTextColor(Color.WHITE);
        //        mTv.setTextSize(24);
        //        mTv.setGravity(Gravity.CENTER);
        //        // Set the ActionBar display option
        //        mAb.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //        // Finally, set the newly created TextView as ActionBar custom view
        //        mAb.setCustomView(mTv);

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_nanigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PopularFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.star:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PopularFragment()).commit();
                        //                mTv.setText("TOP 15");
                        //                mAb.setCustomView(mTv);
                        break;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();
                        //                mTv.setText("Search");
                        //                mAb.setCustomView(mTv);
                        break;
                    case R.id.playListOnline:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlayListOnlineFragment()).commit();
                        //                mTv.setText("Playlist Online");
                        //                mAb.setCustomView(mTv);
                        break;
                    case R.id.download:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DownloadFragment()).commit();
                        //                mTv.setText("Download");
                        //                mAb.setCustomView(mTv);
                        break;
                    case R.id.playList:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlayListFragment()).commit();
                        //                mTv.setText("Playlist");
                        //                mAb.setCustomView(mTv);
                        break;
                }
                return false;
            }
        });

        //VKSdk.login(this, sMyScope);

        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    switch (res) {
                        case LoggedOut:
                            showLogin();
                            break;
                        case LoggedIn:
                            showLogout();
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
                }
            }

            @Override
            public void onError(VKError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                startTestActivity();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
            showLogout();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commitAllowingStateLoss();
    }

    private void showLogout() {
        getSupportFragmentManager()
                .beginTransaction()
                //.replace(R.id.container, new LogoutFragment())
                .replace(R.id.container, new PopularFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void startTestActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }


    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.main_menu, menu);
    //        return super.onCreateOptionsMenu(menu);
    //    }
    //
    //    @Override
    //    public boolean onPrepareOptionsMenu(Menu menu) {
    //        if (isPlayingSong) {
    //            menu.getItem(0).setVisible(true);
    //        } else {
    //            menu.getItem(0).setVisible(false);
    //        }
    //        return super.onPrepareOptionsMenu(menu);
    //    }

    public static class LoginFragment extends android.support.v4.app.Fragment {
        public LoginFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);
            v.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.login(getActivity(), sMyScope);
                }
            });
            return v;
        }

    }

    public static class LogoutFragment extends android.support.v4.app.Fragment {
        public LogoutFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_logout, container, false);

            v.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.logout();
                    if (!VKSdk.isLoggedIn()) {
                        ((MainActivity) getActivity()).showLogin();
                    }
                }
            });
            return v;
        }
    }
}
