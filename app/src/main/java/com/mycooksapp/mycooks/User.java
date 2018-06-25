package com.mycooksapp.mycooks;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class User {

    private DatabaseReference db;
    private FirebaseAuth auth;

    private Activity activity;

    private String uid;
    private String email;

    User() {
        auth = FirebaseAuth.getInstance();
    }

    User(Activity activity, String userId, String userEmail) {
        db = FirebaseDatabase.getInstance().getReference();
        this.activity = activity;
        uid = userId;
        email = userEmail;
    }

    void updateUserSignInInfo() {
        db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("email").exists()) {
                    db.child("users").child(uid).child("email").setValue(email);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Error Code 1 :O", Toast.LENGTH_LONG).show();
            }
        });
    }

    void signOut() {
        auth.signOut();
    }
}
