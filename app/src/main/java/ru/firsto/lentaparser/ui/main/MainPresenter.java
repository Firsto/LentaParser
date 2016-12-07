package ru.firsto.lentaparser.ui.main;

import java.util.List;

import javax.inject.Inject;

import ru.firsto.lentaparser.data.DataManager;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.event.ArticleDetailsOpenEvent;
import ru.firsto.lentaparser.ui.base.BasePresenter;
import ru.firsto.lentaparser.util.RxEventBus;
import ru.firsto.lentaparser.util.RxUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author razor
 * @created 07.12.16
 **/

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private final RxEventBus mRxEventBus;
    private Subscription mLoadArticlesSubscription;
    private Subscription mOpenArticleSubscription;

    @Inject
    public MainPresenter(DataManager dataManager, RxEventBus rxEventBus) {
        mDataManager = dataManager;
        mRxEventBus = rxEventBus;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);

        mOpenArticleSubscription = mRxEventBus
                .filteredObservable(ArticleDetailsOpenEvent.class)
                .subscribe(new Action1<ArticleDetailsOpenEvent>() {
                    @Override
                    public void call(ArticleDetailsOpenEvent articleDetailsOpenEvent) {
                        getMvpView().openArticleDetails(articleDetailsOpenEvent.mArticle);
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.unsubscribe(mLoadArticlesSubscription);
        RxUtil.unsubscribe(mOpenArticleSubscription);
    }

    public void loadArticles() {
        checkViewAttached();
        RxUtil.unsubscribe(mLoadArticlesSubscription);
        getMvpView().showProgressDialog();
        mLoadArticlesSubscription = mDataManager.getArticles()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Article>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the articles.");
                        getMvpView().hideProgressDialog();
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        if (articles.isEmpty()) {
                            getMvpView().onLoadArticlesEmpty();
                        } else {
                            getMvpView().hideProgressDialog();
                            getMvpView().onLoadArticles(articles);
                        }
                    }
                });
    }
}
