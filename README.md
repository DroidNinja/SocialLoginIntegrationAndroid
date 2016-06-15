# SocialLoginIntegrationAndroid
Android Project with helper classes for login through facebook, twitter and google

![demo](http://i.imgur.com/R7XnpAC.png)
![demo](http://i.imgur.com/1fi6UZd.png)

I have used following libraries:

**For Google Login**
```
  compile 'com.google.android.gms:play-services-auth:9.0.2'
```

1. Create app in Google Developer Console by filling out this form: 

    https://developers.google.com/mobile/add?platform=android

2. Enable Google Signin.

3. Get Web Client id mentioned in Oauth 2.0 client ids area here: 

    https://console.developers.google.com/apis/credentials

4. Set this Client id before using instance of GooglePlusSignInHelper :
    
    ```
    GooglePlusSignInHelper.setClient(<YOUR CLIENT ID>)
    ```

Note: If you want google+ information, you need to do following steps:
  
  1. Make sure that Google+ Api is enabled in Google Developer Console.

  2. Include this dependency:

```
  compile 'com.google.android.gms:play-services-plus:9.0.2'
```

 Then, you can access all information through person object in GooglePlusSignInHelper success callback.


**For Facebook Login**
```
compile 'com.facebook.android:facebook-android-sdk:4.5.0'
```

1. Create app in Facebook Developer Console.

2. Follow the steps mentioned here : 
   
   https://developers.facebook.com/docs/android/getting-started/


**For Twitter Login**
```
compile('com.twitter.sdk.android:twitter:1.13.1@aar')
```

You need to approve your application for email access by filling form at:
 
 https://support.twitter.com/forms/platform

You can use following helper classes for integrating any type of login process in your application. 

[TwitterConnectHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/TwitterConnectHelper.java)

[GooglePlusSignInHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/GooglePlusSignInHelper.java)

[FbConnectHelper](https://github.com/DroidNinja/SocialLoginIntegrationAndroid/blob/master/app/src/main/java/com/binarywalllabs/socialintegration/helpers/FbConnectHelper.java)

If there is some problem, feel free to contact me( droidninja15@gmail.com ).
