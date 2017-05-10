package at.sw2017.q_up;

/**
 * Created by PS on 10.05.17.
 */

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A very simple implementation of {@link IdlingResource}.
 * <p>
 * Consider using CountingIdlingResource from espresso-contrib package if you use this class from
 * multiple threads or need to keep a count of pending operations.
 */

public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mCallback;
    private String name = "sir";

    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    private Activity activity;

    public SimpleIdlingResource() {
    }

    public SimpleIdlingResource(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String setName(String n) {
        return this.name = n;
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}