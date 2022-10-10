package com.diamond.future;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diamond.future.model.LoginModel;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//
public class LoginActivity extends AppCompatActivity {
    EditText etPhOrEmail, etPassword;
    PreManager preManager;
    ImageView gmail_id, facebook_id;
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN = 123;
    String TAG = "SignUp_Activity";
    private FirebaseAuth mAuth;
    LoginButton facebook_btn;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFacebookAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPhOrEmail = findViewById(R.id.etPhOrEmail);
        etPassword = findViewById(R.id.etPassword);
        gmail_id = findViewById(R.id.gmail_id);
        facebook_id = findViewById(R.id.facebook_iv);
        facebook_btn = findViewById(R.id.facebook_btn);
        facebook_id.setOnClickListener(v -> facebook_btn.performClick());
        mAuth = FirebaseAuth.getInstance();
        mFacebookAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    mFacebookAuth.signOut();
                }
            }
        };
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            //Toast.makeText(LoginActivity.this, "Current Version of this app: " + version, Toast.LENGTH_SHORT).show();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        preManager = new PreManager(this);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
        };
        gmail_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        facebook_btn.setReadPermissions("email", "public_profile");
        facebook_btn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {

            }

        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFacebookAuth.getCurrentUser();
        mFacebookAuth.addAuthStateListener(authListener);
        if (currentUser != null) {
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            mFacebookAuth.signOut();
            //Toast.makeText(LoginActivity.this, "Currently Logged in: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            doLoginGmail(user, "");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    public void Login(View view) {
        if (!isValidEmail(etPhOrEmail.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Enter valid email ", Toast.LENGTH_SHORT).show();

        } else {
            String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            // String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    String refreshedToken = task.getResult();
                    Log.e("tokenforfcm", refreshedToken);
                    doLogin(etPhOrEmail.getText().toString(), etPassword.getText().toString(), refreshedToken);
                }
            });
            // Log.e("tokenforfcm",refreshedToken);
            // doLogin(etPhOrEmail.getText().toString(), etPassword.getText().toString(),refreshedToken);

           /* Toast.makeText(LoginActivity.this, "Login SuccessFully  ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            preManager.setLogin(true);*/
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void doLogin(String email, String pass, String m_androidId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getlogin(email, pass, m_androidId, "android").enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if (response.body().isSuccess() == 1) {
                        preManager.setLogin(true);
                        preManager.setAuthToken(response.body().getUser().getTokenid());
                        preManager.setDeviceId(response.body().getUser().getDeviceid());
                        preManager.setDeviceName(response.body().getUser().getDevicename());
                        preManager.setLOCATION(response.body().getUser().getLocation());
                        preManager.setDeviceVersion(response.body().getUser().getDeviceVersion());
                        preManager.setPassword(etPassword.getText().toString());
                        preManager.setPhNumber(response.body().getUser().getUser_phonenumber());
                        Toast.makeText(LoginActivity.this, "Login SuccessFully  ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Data not found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    public void Signup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

    public void ForgetPass(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);

    }

    private void doLoginGmail(FirebaseUser user, String email) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);
        String email_user;
        if (user.getEmail() != null) {
            email_user=user.getEmail();
        } else {
            email_user=user.getProviderData().get(1).getEmail();
        }
        UserInfo userInfo = user.getProviderData().get(1);
        requestAPI.getSocilLoginGmail(email_user, user.getDisplayName(), email_user, user.getPhoneNumber(), user.getUid(), "android", "V1", user.getPhotoUrl().toString()).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if (response.body().isSuccess() == 1) {
                        preManager.setLogin(true);
                        preManager.setAuthToken(response.body().getUser().getTokenid());
                        preManager.setDeviceId(response.body().getUser().getDeviceid());
                        preManager.setDeviceName(response.body().getUser().getDevicename());
                        preManager.setLOCATION(response.body().getUser().getLocation());
                        preManager.setDeviceVersion(response.body().getUser().getDeviceVersion());
                        preManager.setPassword(etPassword.getText().toString());
                        preManager.setPhNumber(response.body().getUser().getUser_phonenumber());
                        Toast.makeText(LoginActivity.this, "Login SuccessFully  ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Data not found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        signOut();
        if (authListener != null) {
            mFacebookAuth.removeAuthStateListener(authListener);
        }
    }

    private final void signOut() {
        GoogleSignInClient googleSignInClient = this.mGoogleSignInClient;
        if (googleSignInClient != null) {
            Intrinsics.checkNotNull(googleSignInClient);
            googleSignInClient.signOut();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            doLoginFacebook(user);
                            //Toast.makeText(LoginActivity.this, "Authentication Succeeded.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void doLoginFacebook(FirebaseUser user) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getSocilLoginFacebook(user.getProviderData().get(1).getEmail(), user.getDisplayName(), user.getProviderData().get(1).getEmail(), user.getPhoneNumber(), user.getUid(), "android", "V1", user.getPhotoUrl().toString()).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if (response.body().isSuccess() == 1) {
                        preManager.setLogin(true);
                        preManager.setAuthToken(response.body().getUser().getTokenid());
                        preManager.setDeviceId(response.body().getUser().getDeviceid());
                        preManager.setDeviceName(response.body().getUser().getDevicename());
                        preManager.setLOCATION(response.body().getUser().getLocation());
                        preManager.setDeviceVersion(response.body().getUser().getDeviceVersion());
                        preManager.setPassword("");
                        preManager.setPhNumber(response.body().getUser().getUser_phonenumber());
                        //Toast.makeText(LoginActivity.this, "Login SuccessFully  ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Data not found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
}