package ru.firsto.lentaparser.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.firsto.lentaparser.R;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.databinding.ItemArticleBinding;
import ru.firsto.lentaparser.di.ActivityContext;
import ru.firsto.lentaparser.ui.viewmodel.ArticleViewModel;
import ru.firsto.lentaparser.util.RxEventBus;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleItemViewHolder> {

    private Context mContext;

    private List<Article> mArticles;
    private List<Article> mCopyArticles;

    private final RxEventBus mRxEventBus;

    @Inject
    public ArticlesAdapter(@ActivityContext Context context, RxEventBus rxEventBus) {
        mContext = context;
        mRxEventBus = rxEventBus;
        mArticles = new ArrayList<>();
        mCopyArticles = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
        mArticles = new ArrayList<>(articles);
        mCopyArticles = new ArrayList<>(articles);
    }

    @Override
    public ArticleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemArticleBinding itemArticleBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_article,
                parent,
                false);

        return new ArticleItemViewHolder(itemArticleBinding);
    }

    @Override
    public void onBindViewHolder(final ArticleItemViewHolder holder, int position) {
        final Article article = mArticles.get(position);

        holder.binding.setViewModel(new ArticleViewModel(mContext, article));

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mRxEventBus.post(new ArticleDetailsOpenEvent(article));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void filter(String text) {
        mArticles.clear();
        if (text.isEmpty()) {
            mArticles.addAll(mCopyArticles);
        } else {

            for (Article article : mCopyArticles) {
                if (article.title.toLowerCase().contains(text.toLowerCase())) {
                    mArticles.add(article);
                }
            }
        }
        notifyDataSetChanged();
    }

    class ArticleItemViewHolder extends RecyclerView.ViewHolder {

        private ItemArticleBinding binding;

        public ArticleItemViewHolder(ItemArticleBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }
}
