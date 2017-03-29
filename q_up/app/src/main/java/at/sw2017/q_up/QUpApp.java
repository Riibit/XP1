package at.sw2017.q_up;

import android.app.Application;
import android.content.Context;

/**
 * Created by PS on 29.03.17.
 *
 * used to get app context everywhere
 */

public class QUpApp extends Application {
    private static QUpApp instance;

    public static QUpApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}