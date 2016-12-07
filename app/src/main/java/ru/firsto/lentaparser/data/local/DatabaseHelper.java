package ru.firsto.lentaparser.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.firsto.lentaparser.data.model.Article;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Article> setArticles(final Collection<Article> newArticles) {
        return Observable.create(new Observable.OnSubscribe<Article>() {
            @Override
            public void call(Subscriber<? super Article> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.ArticleTable.TABLE_NAME, null);
                    for (Article article : newArticles) {
                        long result = mDb.insert(Db.ArticleTable.TABLE_NAME,
                                Db.ArticleTable.toContentValues(article),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(article);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Article>> getArticles() {
        return mDb.createQuery(Db.ArticleTable.TABLE_NAME,
                "SELECT * FROM " + Db.ArticleTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Article>() {
                    @Override
                    public Article call(Cursor cursor) {
                        return Db.ArticleTable.parseCursor(cursor);
                    }
                });
    }

}
