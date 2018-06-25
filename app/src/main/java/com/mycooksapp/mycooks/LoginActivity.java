package com.mycooksapp.mycooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Utility utility = new Utility(this);

    private FirebaseAuth auth;
    private FirebaseUser fbUser;
    private GoogleSignInOptions gso;
    private GoogleApiClient gap;

    private final int RC_SIGN_IN = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        fbUser = auth.getCurrentUser();

        ConstraintLayout progLayout = (ConstraintLayout) findViewById(R.id.loginProgressLayout);
        ProgressBar progBar = (ProgressBar) findViewById(R.id.loginProgress);
        utility.setProgress(progLayout, progBar);

        if (isUser()) {
            nextActivity();
        } else {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            gap = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            findViewById(R.id.loginGoogle).setOnClickListener(this);
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isUser() {
        boolean user = false;
        if (fbUser != null) {
            user = true;
        }

        return user;
    }

    private void signIn() {
        utility.showProgress();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gap);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account, account.getEmail());
            } else {
                Toast.makeText(LoginActivity.this, "Google sign-in failed :(", Toast.LENGTH_LONG).show();
                utility.hideProgress();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account, final String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Auth.GoogleSignInApi.signOut(gap);
                            fbUser = auth.getCurrentUser();
                            User user = new User(LoginActivity.this, fbUser.getUid(), email);
                            user.updateUserSignInInfo();
                            utility.hideProgress();
                            nextActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed :(", Toast.LENGTH_SHORT).show();
                            utility.hideProgress();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
        utility.hideProgress();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.loginGoogle:
                signIn();
                break;
            default:
                break;
        }
    }
}
