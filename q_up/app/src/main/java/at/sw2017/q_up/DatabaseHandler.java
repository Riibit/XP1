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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import at.sw2017.q_up.Place;


public class DatabaseHandler {

    private DatabaseConfig db_config;
    private String server_table_users;
    private String server_table_places;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean sign_in_complete = false;

    private CountDownLatch getUsersLatch;
    private CountDownLatch getPlacesLatch;

    // variables to cache server data
    List<Place> placesList = new ArrayList<Place>();
    List<User> usersList = new ArrayList<User>();


    /**
     * check if sign in is completed (might take a while)
     * @return true if connection is established
     */
    public boolean getSignInComplete() {
        return sign_in_complete;
    }

    /**
     * used to sync DB reading
     * @return latch to wait for event
     */
    public CountDownLatch getGetUsersLatch() {
        return getUsersLatch;
    }

    /**
     * used to sync DB reading
     * @return latch to wait for event
     */
    public CountDownLatch getGetPlacesLatch() {
        return getPlacesLatch;
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
                    }
                });
    }

    /**
     * @brief read places table
     * @return 0 = OK ; <0 = error
     */
    public Integer readPlacesFromDB() {

        getPlacesLatch = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        final DatabaseReference placesref = mDatabase.child("places");

        placesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Integer id = Integer.parseInt(dsp.getKey());
                    String name = "";
                    Float lat = 0.0f;
                    Float lon = 0.0f;
                    Float avgR = 0.0f;
                    Integer proctime = 0;

                    for (DataSnapshot u_dsp : dsp.getChildren()) {
                        String key = u_dsp.getKey();
                        if ("name".equals(key))
                            name = u_dsp.getValue().toString();
                        else if ("lat".equals(key))
                            lat = Float.parseFloat(u_dsp.getValue().toString());
                        else if ("lon".equals(key))
                            lon = Float.parseFloat(u_dsp.getValue().toString());
                        else if ("avgr".equals(key))
                            avgR = Float.parseFloat(u_dsp.getValue().toString());
                        else if ("proctime".equals(key))
                            proctime = Integer.parseInt(u_dsp.getValue().toString());
                    }
                    placesList.add(new Place(id, name, lat, lon, avgR, proctime));

                    Log.d("FB", "read place from DB");
                }
                getPlacesLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FB", "read places failed with: " + databaseError.toString());
            }
        });

        return 0;
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

        getUsersLatch = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        final DatabaseReference usersref = mDatabase.child("users");

        usersref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Integer id = Integer.parseInt(dsp.getKey());
                    String name = "";
                    String pw = "";
                    Integer chinplace = 0;

                    for (DataSnapshot u_dsp : dsp.getChildren()) {
                        String key = u_dsp.getKey();
                        if ("name".equals(key))
                            name = u_dsp.getValue().toString();
                        else if ("pw".equals(key))
                            pw = u_dsp.getValue().toString();
                        else if ("id_q_place".equals(key))
                            chinplace = Integer.parseInt(u_dsp.getValue().toString());
                    }
                    usersList.add(new User(id, name, pw, chinplace));

                    Log.d("FB", "read user from DB");
                }
                getUsersLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FB", "read users failed with: " + databaseError.toString());
            }
        });

        return 0;
    }

    /**
     * @brief get cached users
     * @return list of users
     */
    public List<User> getUsersList() {
        return usersList;
    }
}
