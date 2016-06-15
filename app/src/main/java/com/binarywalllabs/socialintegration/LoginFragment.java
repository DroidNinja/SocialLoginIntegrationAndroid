package com.binarywalllabs.socialintegration;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.binarywalllabs.socialintegration.constants.AppConstants;
import com.binarywalllabs.socialintegration.helpers.FbConnectHelper;
import com.binarywalllabs.socialintegration.helpers.GooglePlusSignInHelper;
import com.binarywalllabs.socialintegration.helpers.TwitterConnectHelper;
import com.binarywalllabs.socialintegration.managers.SharedPreferenceManager;
import com.binarywalllabs.socialintegration.model.UserModel;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements TwitterConnectHelper.OnTwitterSignInListener, FbConnectHelper.OnFbSignInListener, GooglePlusSignInHelper.OnGoogleSignInListener{

    private static final String TAG = LoginFragment.class.getSimpleName();
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.login_layout)
    LinearLayout view;

    private FbConnectHelper fbConnectHelper;
    private GooglePlusSignInHelper gSignInHelper;
    private TwitterConnectHelper twitterConnectHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setup();
    }

    private void setup() {
        GooglePlusSignInHelper.setClientID(AppConstants.GOOGLE_CLIENT_ID);
        gSignInHelper = GooglePlusSignInHelper.getInstance();
        gSignInHelper.initialize(getActivity(), this);

        fbConnectHelper = new FbConnectHelper(this,this);
        twitterConnectHelper = new TwitterConnectHelper(getActivity(), this);
    }

    @OnClick(R.id.login_google)
    public void loginwithGoogle(View view) {
        gSignInHelper.signIn(getActivity());
        setBackground(R.color.g_color);
    }

    @OnClick(R.id.login_facebook)
    public void loginwithFacebook(View view) {
        fbConnectHelper.connect();
        setBackground(R.color.fb_color);
    }

    @OnClick(R.id.login_twitter)
    public void loginwithTwitter(View view) {
        twitterConnectHelper.connect();
        setBackground(R.color.twitter_color);
    }

    private void setBackground(int colorId)
    {
        getView().setBackgroundColor(getActivity().getResources().getColor(colorId));
        progressBar.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    private void resetToDefaultView(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        getView().setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
        progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        gSignInHelper.onActivityResult(requestCode, resultCode, data);
        twitterConnectHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        UserModel userModel = getUserModelFromGraphResponse(graphResponse);
        if(userModel!=null) {
            SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
            startHomeActivity(userModel);
        }
    }

    private UserModel getUserModelFromGraphResponse(GraphResponse graphResponse)
    {
        UserModel userModel = new UserModel();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.userName = jsonObject.getString("name");
            userModel.userEmail = jsonObject.getString("email");
            String id = jsonObject.getString("id");
            String profileImg = "http://graph.facebook.com/"+ id+ "/picture?type=large";
            userModel.profilePic = profileImg;
            Log.i(TAG,profileImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    @Override
    public void OnFbError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }

    @Override
    public void OnGSignSuccess(GoogleSignInAccount acct, Person person) {
        UserModel userModel = new UserModel();
        userModel.userName = (acct.getDisplayName()==null)?"":acct.getDisplayName();
        userModel.userEmail = acct.getEmail();

        Log.i(TAG, "OnGSignSuccess: AccessToken "+ acct.getIdToken());

        if(person!=null) {
            int gender = person.getGender();

            if (gender == 0)
                userModel.gender = "MALE";
            else if (gender == 1)
                userModel.gender = "FEMALE";
            else
                userModel.gender = "OTHERS";

            Log.i(TAG, "OnGSignSuccess: gender "+ userModel.gender);
        }

        Uri photoUrl = acct.getPhotoUrl();
        if(photoUrl!=null)
            userModel.profilePic = photoUrl.toString();
        else
            userModel.profilePic = "";
        Log.i(TAG, acct.getIdToken());

        SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
        startHomeActivity(userModel);
    }

    @Override
    public void OnGSignError(GoogleSignInResult errorMessage) {
        resetToDefaultView("Google Sign in error@");
    }

    private void startHomeActivity(UserModel userModel)
    {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra(UserModel.class.getSimpleName(), userModel);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    @Override
    public void onTwitterSuccess(User user, String email) {
        UserModel userModel = new UserModel();
        userModel.userName = user.name;
        userModel.userEmail = email;
        userModel.profilePic = user.profileImageUrl;

        SharedPreferenceManager.getSharedInstance().saveUserModel(userModel);
        startHomeActivity(userModel);
    }

    @Override
    public void onTwitterError(String errorMessage) {
        resetToDefaultView(errorMessage);
    }
}
