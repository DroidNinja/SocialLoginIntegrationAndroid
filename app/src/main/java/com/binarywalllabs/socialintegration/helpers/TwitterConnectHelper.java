package com.binarywalllabs.socialintegration.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * FbConnectHelper.java
 */
public class TwitterConnectHelper {
    private Activity activity;
    private Fragment fragment;
    private OnTwitterSignInListener twitterSignInListener;
    private TwitterAuthClient twitterAuthClient;
    /**
     * Interface to listen the Facebook connect
     */
    public interface OnTwitterSignInListener {
        void onTwitterSuccess(User twitterSessionResult, String email);

        void onTwitterError(String errorMessage);
    }

    public TwitterConnectHelper(Activity activity, OnTwitterSignInListener twitterSignInListener) {
        this.activity = activity;
        twitterAuthClient = new TwitterAuthClient();
        this.twitterSignInListener = twitterSignInListener;
    }

    public TwitterConnectHelper(Fragment fragment, OnTwitterSignInListener twitterSignInListener) {
        this.fragment = fragment;
        twitterAuthClient = new TwitterAuthClient();
        this.twitterSignInListener = twitterSignInListener;
    }

    public void connect() {
        twitterAuthClient.authorize(activity, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(final Result<TwitterSession> twitterSessionResult) {
                Twitter.getApiClient(twitterSessionResult.data).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {

                                final User user = userResult.data;
                                final String email = null;
                                twitterAuthClient.requestEmail(twitterSessionResult.data, new Callback<String>() {
                                    @Override
                                    public void success(Result<String> result) {
                                        // Do something with the result, which provides the email address
                                        twitterSignInListener.onTwitterSuccess(user, result.data);
                                    }

                                    @Override
                                    public void failure(TwitterException exception) {
                                        // Do something on failure
                                        Log.e(TwitterConnectHelper.class.getSimpleName(), exception.toString());
                                        twitterSignInListener.onTwitterError(exception.toString());
                                    }
                                });
                            }

                            @Override
                            public void failure(TwitterException e) {
                                twitterSignInListener.onTwitterError(e.toString());
                            }

                        });
            }

            @Override
            public void failure(TwitterException e) {
                twitterSignInListener.onTwitterError(e.toString());
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (twitterAuthClient != null)
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
