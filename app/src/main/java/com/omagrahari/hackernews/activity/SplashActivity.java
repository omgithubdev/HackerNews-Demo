package com.omagrahari.hackernews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.omagrahari.hackernews.R;
import com.omagrahari.hackernews.activity.Authentication.SignInActivity;
import com.omagrahari.hackernews.activity.home.HomeActivity;
import com.omagrahari.hackernews.helper.Auth;
import com.omagrahari.hackernews.helper.Keys;


public class SplashActivity extends AppCompatActivity {

    private static final int RC_AUTH_RESULT = 4000;
    private static final String TAG = "Splash";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //initialize the library
        Auth.initialize(this);

        if (Auth.isAuthenticated()) {
            navigateToHome();
        } else {
            startActivityForResult(new Intent(this, SignInActivity.class), RC_AUTH_RESULT);
            //start activity for authentication
        }
    }

    /**
     * Navigate to Home
     */
    public void navigateToHome() {
        //move to login screen
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Return to page");
        if (requestCode == RC_AUTH_RESULT) {
            if (data != null) {
                Log.d(TAG, "Return value" + data.getBooleanExtra(Keys.USER_AUTHENTICATED, false));
                if (data.getBooleanExtra(Keys.USER_AUTHENTICATED, false)) {
                    navigateToHome();
                }
            } else {
                finish();
            }
        }
    }
}
