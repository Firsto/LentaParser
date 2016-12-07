package ru.firsto.lentaparser.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.firsto.lentaparser.data.local.DatabaseHelper;
import ru.firsto.lentaparser.data.local.PreferencesHelper;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.data.model.ArticleResponse;
import ru.firsto.lentaparser.data.remote.ArticlesService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final ArticlesService mArticlesService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(ArticlesService articlesService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mArticlesService = articlesService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Article> syncArticles() {
        return mArticlesService.getResponse()
                .map(new Func1<ArticleResponse, List<Article>>() {
                    @Override
                    public List<Article> call(ArticleResponse articleResponse) {
                        return articleResponse.channel.articles;
                    }
                }).concatMap(new Func1<List<Article>, Observable<Article>>() {
                    @Override
                    public Observable<Article> call(List<Article> articles) {
                        return mDatabaseHelper.setArticles(articles);
                    }
                });
    }

    public Observable<List<Article>> getArticles() {
        return mDatabaseHelper.getArticles().distinct();
    }

}
