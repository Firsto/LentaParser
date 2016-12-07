package ru.firsto.lentaparser.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.firsto.lentaparser.data.remote.ArticlesService;
import ru.firsto.lentaparser.di.ApplicationContext;

/**
 * @author razor
 * @created 06.12.16
 **/
@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ArticlesService provideArticlesService() {
        return ArticlesService.Creator.newArticlesService();
    }
}
