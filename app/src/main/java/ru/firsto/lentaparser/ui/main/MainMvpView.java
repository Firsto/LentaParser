package ru.firsto.lentaparser.ui.main;

import java.util.List;

import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.ui.base.MvpView;

/**
 * @author razor
 * @created 07.12.16
 **/

public interface MainMvpView extends MvpView {

    void showProgressDialog();

    void hideProgressDialog();

    void showError();

    void onLoadArticlesEmpty();

    void onLoadArticles(List<Article> articles);

    void openArticleDetails(Article article);
}
