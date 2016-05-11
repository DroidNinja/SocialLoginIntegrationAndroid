package com.binarywalllabs.socialintegration.helpers;

/**
 * Created by Arun on 06-09-2015.
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


/**
 * GooglePlusSignInHelper.java
 * This is helper class to use for google+ sign in.
 * For Error code refer : https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult
 */
public class GooglePlusSignInHelper {
    private final static String TAG = "GooglePlusSignInHelper";
        private final static int RC_SIGN_IN = 100;
        private static GoogleSignInOptions gso;

        private static GooglePlusSignInHelper sInstance;
        private static String webClientID;
        private GoogleApiClient googleApiClient;
        private Context context;
        private OnGoogleSignInListener loginResultCallback;
        private ResultCallback<Status> logoutResultCallback;

    /**
     * This method should be called before calling any instance.
     * This is neccessary to get access token and id of user.
     * @param googleClientId
     */
    public static void setClientID(String googleClientId) {
        webClientID = googleClientId;
    }

    /**
     * Interface to listen the Google login
     */
    public interface OnGoogleSignInListener {
        void OnGSignSuccess(GoogleSignInAccount graphResponse);

        void OnGSignError(GoogleSignInResult errorMessage);
    }

        public static GooglePlusSignInHelper getInstance() {
            if (sInstance == null) {
                sInstance = new GooglePlusSignInHelper();
            }
            return sInstance;
        }

        private GooglePlusSignInHelper() {
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestProfile() //for profile related info
                    .requestEmail() //for email
                    .requestIdToken(webClientID) //for accessToken and id
                    .build();
        }

        public void initialize(FragmentActivity activity, OnGoogleSignInListener onGoogleSignInListener)
        {
            loginResultCallback = onGoogleSignInListener;
            context = activity;
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.e(TAG, "onConnectionFailed: " + connectionResult);
                        }
                    } /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "onConnected");
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.i(TAG, "onConnectionSuspended");
                }
            });

            googleApiClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.i(TAG, "onConnectionFailed");
                }
            });
        }

        public boolean isConnected() {
            boolean isConnected = googleApiClient.isConnected();

            Log.i(TAG, "isConnected()" + isConnected);
            return isConnected;
        }

        public void signIn(Activity activity) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        public void signOut() {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (logoutResultCallback != null) {
                                logoutResultCallback.onResult(status);
                            }
                        }
                    });
        }

        private void handleSignInResult(GoogleSignInResult result) {
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                Log.i(TAG, "Signed in");
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                if (loginResultCallback != null) {
                    loginResultCallback.OnGSignSuccess(acct);
                }
            } else {
                Log.i(TAG, "Signed out");

                if (loginResultCallback != null) {
                    loginResultCallback.OnGSignError(result);
                }
            }
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        public void setLogoutResultCallback(ResultCallback<Status> callback) {
            logoutResultCallback = callback;
        }
}
