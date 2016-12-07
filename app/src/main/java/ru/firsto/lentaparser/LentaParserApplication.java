package ru.firsto.lentaparser;

import android.app.Application;
import android.content.Context;

import ru.firsto.lentaparser.di.component.ApplicationComponent;
import ru.firsto.lentaparser.di.component.DaggerApplicationComponent;
import ru.firsto.lentaparser.di.module.ApplicationModule;
import timber.log.Timber;

/**
 * @author razor
 * @created 06.12.16
 **/

public class LentaParserApplication extends Application {

    private static LentaParserApplication sApplication;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        sApplication = this;
    }

    public static LentaParserApplication get() {
        return sApplication;
    }

    public static LentaParserApplication get(Context context) {
        return (LentaParserApplication) context.getApplicationContext();
    }

    public ApplicationComponent component() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
}
