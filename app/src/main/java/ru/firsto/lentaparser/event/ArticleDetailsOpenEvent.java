package ru.firsto.lentaparser.event;

import ru.firsto.lentaparser.data.model.Article;

/**
 * @author razor
 * @created 07.12.16
 **/

public class ArticleDetailsOpenEvent {
    public final Article mArticle;

    public ArticleDetailsOpenEvent(Article article) {
        mArticle = article;
    }
}
