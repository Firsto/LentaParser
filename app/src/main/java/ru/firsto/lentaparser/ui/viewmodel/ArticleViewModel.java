package ru.firsto.lentaparser.ui.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.firsto.lentaparser.LentaParserApplication;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.event.ArticleDetailsOpenEvent;

/**
 * @author razor
 * @created 07.12.16
 **/

public class ArticleViewModel extends BaseObservable {

    private Context mContext;
    private Article mArticle;
    private BindableFieldTarget mBindableFieldTarget;

    public ObservableField<Drawable> articleImage;

    public ArticleViewModel(Context context, Article article) {
        mContext = context;
        mArticle = article;

        initImage();
    }

    private void initImage() {
        articleImage = new ObservableField<>();
        // Picasso keeps a weak reference to the target so it needs to be stored in a field
        mBindableFieldTarget = new BindableFieldTarget(articleImage, mContext.getResources());

        if (mArticle.enclosure != null) {
            Picasso.with(mContext)
                    .load(mArticle.enclosure.url)
                    .into(mBindableFieldTarget);
        }
    }

    public String getArticleTitle() {
        return mArticle.title;
    }

    public String getArticleDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
        try {
            return DateUtils.getRelativeTimeSpanString(simpleDateFormat.parse(mArticle.pubDate).getTime()).toString();
        } catch (ParseException e) {
            return mArticle.pubDate;
        }
    }

    public String getDesctiption() {
        return mArticle.description;
    }

    public View.OnClickListener onClickArticle() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LentaParserApplication.get().component().eventBus()
                        .post(new ArticleDetailsOpenEvent(mArticle));
            }
        };
    }

    public class BindableFieldTarget implements Target {

        private ObservableField<Drawable> observableField;
        private Resources resources;

        public BindableFieldTarget(ObservableField<Drawable> observableField, Resources resources) {
            this.observableField = observableField;
            this.resources = resources;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            observableField.set(new BitmapDrawable(resources, bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            observableField.set(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            observableField.set(placeHolderDrawable);
        }
    }
}
