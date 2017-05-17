package at.sw2017.q_up;

/**
 * Created by PS on 29.03.17.
 */

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import java.util.concurrent.locks.ReentrantLock;

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
    ReentrantLock placesListLock = new ReentrantLock();
    List<Place> placesList = new ArrayList<Place>();
    ReentrantLock usersListLock = new ReentrantLock();
    List<User> usersList = new ArrayList<User>();

    public void placesLock() { placesListLock.lock(); }
    public void usersLock() { usersListLock.lock(); }
    public void placesUnlock() { placesListLock.unlock(); }
    public void usersUnlock() { usersListLock.unlock(); }

    // used for synchronisation
    private CountDownLatch signInLatch;
    private CountDownLatch getUsersLatch;
    private CountDownLatch getPlacesLatch;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource placesIdlingResource;
    @Nullable
    private SimpleIdlingResource usersIdlingResource;

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

        if (placesIdlingResource == null) {
            placesIdlingResource = new SimpleIdlingResource();
            placesIdlingResource.setName("placesIR");
        }
        if (usersIdlingResource == null) {
            usersIdlingResource = new SimpleIdlingResource();
            usersIdlingResource.setName("usersIR");
        }

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
                Log.d("FB", "read places from DB");

                placesLock();
                // clear list
                placesList.clear();
                // fill list with new data
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Place place = dsp.getValue(Place.class);
                    placesList.add(place);
                }
                placesUnlock();

                // signal, that we are finished
                getPlacesLatch.countDown();
                placesIdlingResource.setIdleState(true);
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
        if (!placesListLock.isLocked())
            throw new AssertionError("List must be locked and unlocked!");
        return placesList;
    }

    public boolean isPlacesListEmpty() {
        boolean retval = false;
        placesLock();
        if (placesList.isEmpty())
            retval = true;
        placesUnlock();
        return retval;
    }

    /**
     * add a new place to the database
     * @return 0 if OK
     */
    public Integer addPlace(String name, String lat, String lon, String rating_pos, String rating_neg, String proct) {

        DatabaseReference placeref = FirebaseDatabase.getInstance().getReference("places");

        String placeId = placeref.push().getKey();
        Place place = new Place(placeId, name, lat, lon, rating_pos, rating_neg, proct);

        getPlacesLatch = new CountDownLatch(1);
        placesIdlingResource.setIdleState(false);
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
        placesIdlingResource.setIdleState(false);
        placeref.child(id).removeValue();
        return 0;
    }

    /**
     * change some attribute of a user
     * @param id
     * @param attribute
     * @param value
     * @return 0 if successful
     */
    public int modifyPlaceAttribute(String id, String attribute, String value) {

        DatabaseReference placesref = FirebaseDatabase.getInstance().getReference("places");

        getPlacesLatch = new CountDownLatch(1);
        placesIdlingResource.setIdleState(false);
        placesref.child(id).child(attribute).setValue(value);
        return 0;
    }

    /**
     * give place a positive vote
     * just returns when given an invalid id.
     * @param id id of place
     */
    public void votePlacePositive(String id) {
        int rating = 0;
        boolean found = false;

        placesLock();
        for (Place p : placesList) {
            if (p.placeId.equals(id)) {
                found = true;
                rating = Integer.parseInt(p.ratingPositive);
            }
        }
        placesUnlock();

        // invalid id - return!
        if (found == false)
            return;

        rating += 1;
        modifyPlaceAttribute(id, "ratingPos", Integer.toString(rating));
    }

    /**
     * give place a negative vote
     * just returns when given an invalid id.
     * @param id id of place
     */
    public void votePlaceNegative(String id) {
        int rating = 0;
        boolean found = false;

        placesLock();
        for (Place p : placesList) {
            if (p.placeId.equals(id)) {
                found = true;
                rating = Integer.parseInt(p.ratingNegative);
            }
        }
        placesUnlock();

        // invalid id - return!
        if (found == false)
            return;

        rating += 1;
        modifyPlaceAttribute(id, "ratingNeg", Integer.toString(rating));
    }

    /**
     * get the number of queued up users in a place
     * @param id id of the place
     * @return number of users - 0 if no users are queued
     */
    public int getQueuedUserCountFromPlace(String id) {
        int people_in_queue = 0;
        usersLock();
        for (User u : usersList) {
            if (u.idCheckInPlace.equals(id))
                people_in_queue += 1;
        }
        usersUnlock();
        return people_in_queue;
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
                Log.d("FB", "read users from DB");

                usersLock();
                // clear list
                usersList.clear();
                // fill list with new data
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    usersList.add(user);
                }
                usersUnlock();

                // signal, that we are finished
                getUsersLatch.countDown();
                usersIdlingResource.setIdleState(true);
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
        if (!usersListLock.isLocked())
            throw new AssertionError("List must be locked and unlocked!");
        return usersList;
    }

    public boolean isUsersListEmpty() {
        boolean retval = false;
        usersLock();
        if (usersList.isEmpty())
            retval = true;
        usersUnlock();
        return retval;
    }

    /**
     * add a new user to the database
     * @param name the name of the new user
     * @param pw the password of the new user
     * @return 0 if successful
     */
    public int addUser(String name, String pw) {

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("users");

        String userId = userref.push().getKey();
        User user = new User(userId, name, pw, "0");

        getUsersLatch = new CountDownLatch(1);
        usersIdlingResource.setIdleState(false);
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
        usersIdlingResource.setIdleState(false);
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

        getUsersLatch = new CountDownLatch(1);
        usersIdlingResource.setIdleState(false);
        usersref.child(id).child(attribute).setValue(value);
        return 0;
    }

    /**
     * checks user into place
     * just returns when given an invalid id.
     * @param userID id of user
     * @param placeID id of place
     */
    public void checkUserIntoPlace(String userID, String placeID) {
        boolean found = false;

        usersLock();
        for (User u : usersList) {
            if (u.userId.equals(userID)) {
                found = true;
            }
        }
        usersUnlock();

        // invalid user id - return!
        if (found == false)
            return;

        // reset flag
        found = false;

        placesLock();
        for (Place p : placesList) {
            if (p.placeId.equals(placeID)) {
                found = true;
            }
        }
        placesUnlock();

        // invalid place id - return!
        if (found == false)
            return;

        modifyUserAttribute(userID, "idCheckInPlace" ,placeID);
    }

    /**
     * checks user out of any place he is checked into
     * just returns when given an invalid id.
     * @param userID id of user
     */
    public void checkOutOfPlace(String userID) {
        boolean found = false;
        usersLock();
        for (User u : usersList) {
            if (u.userId.equals(userID)) {
                found = true;
            }
        }
        usersUnlock();

        // invalid user id - return!
        if (found == false)
            return;

        modifyUserAttribute(userID, "idCheckInPlace" , "");
    }

    /**
     * Only called from test, creates and returns a new simple IdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getPlacesIdlingResource() {
        return placesIdlingResource;
    }
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getUsersIdlingResource() {
        return usersIdlingResource;
    }
}
