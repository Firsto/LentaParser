package ru.firsto.lentaparser.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.firsto.lentaparser.R;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.databinding.ArticleDetailsContentBinding;
import ru.firsto.lentaparser.ui.base.BaseActivity;
import ru.firsto.lentaparser.ui.viewmodel.ArticleViewModel;

/**
 * @author razor
 * @created 07.12.16
 **/

public class ArticleDetailsFragment extends Fragment {

    public static final String KEY_ARTICLE = "key_article";

    private Article mArticle;

    public static ArticleDetailsFragment getInstance(Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARTICLE, article);
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mArticle = getArguments().getParcelable(KEY_ARTICLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ArticleDetailsContentBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.article_details_content, container, false);

        binding.setViewModel(new ArticleViewModel(getActivity(), mArticle));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        ActionBar actionBar;
        if ((actionBar = ((BaseActivity) getActivity()).getSupportActionBar()) != null) {
            actionBar.setTitle(mArticle.title);

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ((MainActivity) getActivity()).toggleListVisibility(false);
    }

    @Override
    public void onStop() {
        ActionBar actionBar;
        if ((actionBar = ((BaseActivity) getActivity()).getSupportActionBar()) != null) {
            actionBar.setTitle(R.string.app_name);

            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        ((MainActivity) getActivity()).toggleListVisibility(true);

        super.onStop();
    }
}
