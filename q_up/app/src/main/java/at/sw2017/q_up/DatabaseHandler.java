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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import at.sw2017.q_up.Place;


public class DatabaseHandler {

    private boolean dbreader_users_active = false;
    private boolean dbreader_places_active = false;

    private DatabaseConfig db_config;
    private String server_table_users;
    private String server_table_places;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    // variables to cache server data
    List<Place> placesList = new ArrayList<Place>();
    List<User> usersList = new ArrayList<User>();

    // used for synchronisation
    private CountDownLatch signInLatch;
    private CountDownLatch getUsersLatch;
    private CountDownLatch getPlacesLatch;

    /**
     * wait for sign in to complete (might take a while)
     * @param timeout_s timeout in seconds
     */
    public void waitSignInComplete(int timeout_s) {
        try {
            signInLatch.await(timeout_s, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * wait for users table operation to complete (might take a while)
     * @param timeout_s timeout in seconds
     */
    public void waitUsersComplete(int timeout_s) {
        try {
            getUsersLatch.await(timeout_s, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * wait for places table operation to complete (might take a while)
     * @param timeout_s timeout in seconds
     */
    public void waitPlacesComplete(int timeout_s) {
        try {
            getPlacesLatch.await(timeout_s, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        signInLatch = new CountDownLatch(1);

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
                        }
                        else {
                        }
                        signInLatch.countDown();
                    }
                });

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        // activate handlers for dynamic reading of databases
        readPlacesFromDB();
        readUsersFromDB();
    }

    /**
     * read places table
     * @return 0 = OK ; <0 = error
     */
    public Integer readPlacesFromDB() {

        // only call the function once
        if (dbreader_places_active == true)
            return 0;
        dbreader_places_active = true;

        getPlacesLatch = new CountDownLatch(1);

        final DatabaseReference placesref = mDatabase.child("places");

        placesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // clear list
                placesList.clear();

                // fill list with new data
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Place place = dsp.getValue(Place.class);
                    placesList.add(place);

                    Log.d("FB", "read place from DB");
                }

                // signal, that we are finished
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
     * get cached places
     * @return list of places
     */
    public List<Place> getPlacesList() {
        return placesList;
    }

    /**
     * add a new place to the database
     * @return 0 if OK
     */
    public Integer addPlace(String name, String lat, String lon, String avgr, String proct) {

        DatabaseReference placeref = FirebaseDatabase.getInstance().getReference("places");

        getPlacesLatch = new CountDownLatch(1);

        String placeId = placeref.push().getKey();
        Place place = new Place(placeId, name, lat, lon, avgr, proct);
        placeref.child(placeId).setValue(place);

        return 0;
    }

    /**
     * remove a place
     * @param id
     * @return 0 if OK
     */
    public int removePlace(String id) {

        DatabaseReference placeref = FirebaseDatabase.getInstance().getReference("places");

        getPlacesLatch = new CountDownLatch(1);

        placeref.child(id).removeValue();
        return 0;
    }

    /**
     * read users table - this should only be ran once
     * @return 0 = OK ; <0 = error
     */
    public Integer readUsersFromDB() {

        // only call the function once
        if (dbreader_users_active == true)
            return 0;
        dbreader_users_active = true;

        getUsersLatch = new CountDownLatch(1);

        final DatabaseReference usersref = mDatabase.child("users");

        usersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // clear list
                usersList.clear();

                // fill list with new data
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    User user = dsp.getValue(User.class);
                    usersList.add(user);

                    Log.d("FB", "read user from DB");
                }
                // signal, that we are finished
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
     * get cached users (no update is forced)
     * @return list of users
     */
    public List<User> getUsersList() {
        return usersList;
    }

    /**
     * add a new user to the database
     * @param name the name of the new user
     * @param pw the password of the new user
     * @return 0 if successful
     */
    public int addUser(String name, String pw) {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("users");

        getUsersLatch = new CountDownLatch(1);

        String userId = userref.push().getKey();
        User user = new User(userId, name, pw, "0");
        userref.child(userId).setValue(user);

        return 0;
    }

    /**
     * remove user from database
     * @param id id of user
     * @return 0 if successful
     */
    public int removeUser(String id) {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("users");

        getUsersLatch = new CountDownLatch(1);

        userref.child(id).removeValue();
        return 0;
    }

    /**
     * change some attribute of a user
     * @param id
     * @param attribute
     * @param value
     * @return 0 if successful
     */
    public int modifyUserAttribute(String id, String attribute, String value) {

        DatabaseReference usersref = FirebaseDatabase.getInstance().getReference("users");

        usersref.child(id).child(attribute).setValue(value);

        return 0;
    }
}
