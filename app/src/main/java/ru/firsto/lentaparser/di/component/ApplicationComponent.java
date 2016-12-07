package ru.firsto.lentaparser.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.firsto.lentaparser.data.DataManager;
import ru.firsto.lentaparser.data.SyncService;
import ru.firsto.lentaparser.data.local.DatabaseHelper;
import ru.firsto.lentaparser.data.local.PreferencesHelper;
import ru.firsto.lentaparser.data.remote.ArticlesService;
import ru.firsto.lentaparser.di.ApplicationContext;
import ru.firsto.lentaparser.di.module.ApplicationModule;
import ru.firsto.lentaparser.util.RxEventBus;

/**
 * @author razor
 * @created 06.12.16
 **/

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    ArticlesService articlesService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();
}
