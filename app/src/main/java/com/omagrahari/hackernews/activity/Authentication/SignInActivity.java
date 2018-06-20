package com.omagrahari.hackernews.activity.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.omagrahari.hackernews.R;
import com.omagrahari.hackernews.helper.Keys;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 001;
    private static final int RC_SIGN_IN_FIREBASE = 123;
    private static final int RC_AUTH_RESULT = 4000;
    private static final String TAG = "GoogleActivity";

    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    private static FirebaseAuth mAuth;
    private ProgressBar progressBar;

    LoginButton mFbLoginButton;
    RelativeLayout mGoogleLogin;
    ImageView mRelFbLogin;
    Button mFirebaseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setViews();

        setVariables();
    }


    /**
     * Initialize Views
     */
    private void setViews() {
        try {
            mFirebaseLogin = findViewById(R.id.firebase_login);
            mFbLoginButton = findViewById(R.id.login_button);
            mGoogleLogin = findViewById(R.id.g_login);
            mRelFbLogin = findViewById(R.id.fb_login);
            progressBar = findViewById(R.id.progressBar);

            progressBar.setVisibility(View.INVISIBLE);

            mRelFbLogin.setOnClickListener(this);

            mGoogleLogin.setOnClickListener(this);

            mFirebaseLogin.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Initialize variables
     */
    private void setVariables() {
        try {
            mAuth = FirebaseAuth.getInstance();

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mCallbackManager = CallbackManager.Factory.create();

            mFbLoginButton.setReadPermissions(Arrays.asList(EMAIL));
            // If you are using in a fragment, call loginButton.setFragment(this);

            // Callback registration
            mFbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(TAG, "FB Login Success");
                    navigateAfterLogin();
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });

            LoginManager.getInstance().registerCallback(mCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            System.out.println("Login Manager Fb");
                            navigateAfterLogin();
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Perform click on fb login button to open FB activity
     */
    public void fbLogin() {
        mFbLoginButton.performClick();
    }

    /**
     * Open Google Sign In PopUp
     */
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * firebase login with phone number
     */

    public void firebaseLogin() {
        try {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false, true)
                            .setAvailableProviders(providers)
                            .setTheme(R.style.CustomFirebaseTheme)
                            .build(),
                    RC_SIGN_IN_FIREBASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Navigate to login::" + requestCode);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    // ...
                    Toast.makeText(SignInActivity.this, "Signin Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case RC_SIGN_IN_FIREBASE:
                if (resultCode == ResultCodes.OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    navigateAfterLogin();
                } else {

                }
                break;
            default:
                System.out.println("Navigate to Facebook::" + AccessToken.getCurrentAccessToken());
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    /**
     * Get credential from Firebase for Google Login
     *
     * @param acct
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Login Successful; Now navigate to home page
                            Toast.makeText(SignInActivity.this, "Welcome " + user.getDisplayName() + " !!", Toast.LENGTH_SHORT).show();

                            navigateAfterLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Navigate after login
     */
    public void navigateAfterLogin() {
        Log.d(TAG, "Navigate to login");
        System.out.println("Navigate to login::");
        Intent intent = new Intent();
        intent.putExtra(Keys.USER_AUTHENTICATED, true);
        //moved to home
        setResult(RC_AUTH_RESULT, intent);
        finish();
    }

    /**
     * On click events
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fb_login:
                fbLogin();
                break;
            case R.id.g_login:
                signIn();
                break;
            case R.id.firebase_login:
                firebaseLogin();
                break;
        }
    }

}
