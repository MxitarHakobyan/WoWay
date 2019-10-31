package com.mino.woway.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mino.woway.R;
import com.mino.woway.users_model.Owner;
import com.mino.woway.users_model.UserName;
import com.mino.woway.utils.OwnerClient;

import org.json.JSONException;

import java.util.Arrays;

import static com.mino.woway.utils.Constants.NAME_EMAIL;
import static com.mino.woway.utils.Constants.NAME_FIRST_NAME;
import static com.mino.woway.utils.Constants.NAME_ID;
import static com.mino.woway.utils.Constants.NAME_LAST_NAME;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private LoginButton mLoginButton;

    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        checkLoginStatus();
        setCallbackUp();
    }

    private void init() {
        mLoginButton = findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
    }

    private void setCallbackUp() {
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.toString());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(LoginActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
            } else {
                switch2Map(currentAccessToken);
            }
        }
    };

    private void switch2Map(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, (object, response) -> {
            try {

                Owner owner = new Owner(object.getString(NAME_ID),
                        new UserName(object.getString(NAME_FIRST_NAME), object.getString(NAME_LAST_NAME)),
                        object.getString(NAME_EMAIL),
                        R.drawable.ic_location_on_black_24dp);
                ((OwnerClient)  getApplicationContext()).setOwner(owner);


                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            switch2Map(AccessToken.getCurrentAccessToken());
        }
    }
}
