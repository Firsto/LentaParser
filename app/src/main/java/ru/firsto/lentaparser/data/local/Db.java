package ru.firsto.lentaparser.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import ru.firsto.lentaparser.data.model.Article;

public class Db {

    public Db() { }

    public abstract static class ArticleTable {
        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_GUID = "guid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PUBLICATION_DATE = "pub_date";
        public static final String COLUMN_CATEGORY = "capital";

        public static final String COLUMN_ENCLOSURE_URL = "enclosure_url";
        public static final String COLUMN_ENCLOSURE_TYPE = "enclosure_type";
        public static final String COLUMN_ENCLOSURE_LENGTH = "enclosure_length";


        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_GUID + " TEXT PRIMARY KEY, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_LINK + " TEXT NOT NULL, " +
                        COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        COLUMN_PUBLICATION_DATE + " TEXT NOT NULL, " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        COLUMN_ENCLOSURE_URL + " TEXT, " +
                        COLUMN_ENCLOSURE_TYPE + " TEXT, " +
                        COLUMN_ENCLOSURE_LENGTH + " INTEGER" +
                " ); ";

        public static ContentValues toContentValues(Article article) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_GUID, article.guid);
            values.put(COLUMN_TITLE, article.title);
            values.put(COLUMN_LINK, article.link);
            values.put(COLUMN_DESCRIPTION, article.description);
            values.put(COLUMN_PUBLICATION_DATE, article.pubDate);
            values.put(COLUMN_CATEGORY, article.category);
            if (article.enclosure != null) {
                values.put(COLUMN_ENCLOSURE_URL, article.enclosure.url);
                values.put(COLUMN_ENCLOSURE_TYPE, article.enclosure.type);
                values.put(COLUMN_ENCLOSURE_LENGTH, article.enclosure.length);
            }

            return values;
        }

        public static Article parseCursor(Cursor cursor) {
            Article article = new Article();

            article.guid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GUID));
            article.title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            article.link = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK));
            article.description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            article.pubDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLICATION_DATE));
            article.category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));

            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENCLOSURE_URL));
            if (!TextUtils.isEmpty(imageUrl)) {
                article.enclosure = new Article.Enclosure();

                article.enclosure.url = imageUrl;
                article.enclosure.type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENCLOSURE_TYPE));
                article.enclosure.length = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ENCLOSURE_LENGTH));
            }

            return article;
        }
    }
}
