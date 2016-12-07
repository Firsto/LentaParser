package ru.firsto.lentaparser.di.component;

import dagger.Component;
import ru.firsto.lentaparser.di.PerActivity;
import ru.firsto.lentaparser.di.module.ActivityModule;
import ru.firsto.lentaparser.ui.main.MainActivity;

/**
 * @author razor
 * @created 07.12.16
 **/

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
}
