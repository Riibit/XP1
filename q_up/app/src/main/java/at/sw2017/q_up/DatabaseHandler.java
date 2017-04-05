package at.sw2017.q_up;

/**
 * Created by PS on 29.03.17.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import at.sw2017.q_up.Place;


public class DatabaseHandler {

    private DatabaseConfig db_config;
    private String server_table_users;
    private String server_table_places;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean sign_in_complete = false;

    // variables to cache server data
    List<Place> placesList = new ArrayList<Place>();
    List<User> usersList = new ArrayList<User>();

    boolean finished = false;


    /**
     * check if sign in is completed (might take a while)
     * @return true if connection is established
     */
    public boolean getSignInComplete() {
        return sign_in_complete;
    }

    /**
     * add listener on start
     */
    public void addAuthStListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * remove listener on stop
     */
    public void removeAuthStListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * constructor
     */
    public DatabaseHandler() {

        // load config file
        db_config = new DatabaseConfig();
        try {
            db_config.loadConfigValues();
        } catch (IOException e) {
            Log.d("DB", "Loading config failed!");
            e.printStackTrace();
        }

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FB", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("FB", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        mAuth.signInWithEmailAndPassword(db_config.getUser(), db_config.getPw())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FB", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FB", "signInWithEmail:failed", task.getException());
                            sign_in_complete = false;
                        }
                        else {
                            sign_in_complete = true;
                        }

                        finished = true;
                    }
                });


//        int bla = 0;
//        while(!finished && sign_in_complete == false) {
//            bla += 1;
//        }
//        finished = false;
    }

    /**
     * @brief read places table
     * @return 0 = OK ; <0 = error
     */
    public Integer readPlacesFromDB() {
        // TODO read table

        return -1;
    }

    /**
     * @brief get cached places
     * @return list of places
     */
    public List<Place> getPlacesList() {
        return placesList;
    }

    /**
     * @brief read users table
     * @return 0 = OK ; <0 = error
     */
    public Integer readUsersFromDB() {
        // TODO read table

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        DatabaseReference usersref = mDatabase.child("users");


        usersref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("hi");
                List Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(String.valueOf(dsp.getValue()));
                    System.out.println("hi");
                }
                finished = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println("ho");
                finished = true;
            }
        });

        while(!finished){}
        finished = false;

        return -1;
    }

    /**
     * @brief get cached users
     * @return list of users
     */
    public List<User> getUsersList() {
        return usersList;
    }
}
