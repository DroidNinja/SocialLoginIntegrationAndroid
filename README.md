# SocialLoginIntegrationAndroid
Android Project with helper classes for login through facebook, twitter and google

![demo](http://i.imgur.com/R7XnpAC.png)
![demo](http://i.imgur.com/1fi6UZd.png)

I have used following libraries:

**For Google Login**
```
  compile 'com.google.android.gms:play-services-auth:8.4.0'
```
**For Facebook Login**
```
compile 'com.facebook.android:facebook-android-sdk:4.5.0'
```

**For Twitter Login**
```
compile('com.twitter.sdk.android:twitter:1.13.1@aar')
```

You can use following helper classes for integrating any type of login process in your application. 

[TwitterConnectHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/TwitterConnectHelper.java)
You need to approve your application for email access by filling form at: https://support.twitter.com/forms/platform

[GooglePlusSignInHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/GooglePlusSignInHelper.java)
1. Create app in Google Developer Console by filling out this form: https://developers.google.com/mobile/add?platform=android
2. Enable Google Signin.
3. Get Web Client id mentioned in Oauth 2.0 client ids area here: https://console.developers.google.com/apis/credentials
4. Set this Client id before using instance of GooglePlusSignInHelper using GooglePlusSignInHelper.setClient(<YOUR CLIENT ID>)

[FbConnectHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/FbConnectHelper.java)
1. Create app in Facebook Developer Console.
2. Follow the steps mentioned here : https://developers.facebook.com/docs/android/getting-started/

If there is some problem, feel free to contact me( droidninja15@gmail.com ).
