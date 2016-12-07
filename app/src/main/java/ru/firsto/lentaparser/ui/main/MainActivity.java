package ru.firsto.lentaparser.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import ru.firsto.lentaparser.R;
import ru.firsto.lentaparser.data.SyncService;
import ru.firsto.lentaparser.data.model.Article;
import ru.firsto.lentaparser.ui.adapter.ArticlesAdapter;
import ru.firsto.lentaparser.ui.base.BaseActivity;
import ru.firsto.lentaparser.util.DialogFactory;

/**
 * @author razor
 * @created 07.12.16
 **/

public class MainActivity extends BaseActivity
        implements MainMvpView, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "ru.firsto.lentaparser.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject MainPresenter mMainPresenter;
    @Inject ArticlesAdapter mArticlesAdapter;

    private static final String SEARCH_QUERY = "key_search_query";
    private String mSearchQuery;

    RecyclerView mArticlesRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);

        mMainPresenter.attachView(this);

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (savedInstanceState == null) {
            mSearchQuery = "";
        } else {
            mSearchQuery = savedInstanceState.getString(SEARCH_QUERY, "");
        }

        mArticlesRecyclerView = (RecyclerView) findViewById(R.id.list_articles);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mArticlesRecyclerView.setAdapter(mArticlesAdapter);
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMainPresenter.loadArticles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        MenuItem searchItem = menu.findItem(R.id.search);
        if (!TextUtils.isEmpty(mSearchQuery)) {
            String lastQuery = mSearchQuery;
            searchItem.expandActionView();
            searchView.setQuery(lastQuery, true);
            searchView.clearFocus();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_QUERY, mSearchQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.loadArticles();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_articles))
                .show();
    }

    @Override
    public void onLoadArticlesEmpty() {
        mArticlesAdapter.setArticles(Collections.<Article>emptyList());
        mArticlesAdapter.notifyDataSetChanged();
//        Toast.makeText(this, R.string.empty_articles, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadArticles(List<Article> articles) {
        mArticlesAdapter.setArticles(articles);
        mArticlesAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(mSearchQuery)) mArticlesAdapter.filter(mSearchQuery);
    }

    @Override
    public void openArticleDetails(Article article) {
        openArticleDetailsFragment(article);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mArticlesAdapter.filter(query);
        mSearchQuery = query;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mArticlesAdapter.filter(newText);
        mSearchQuery = newText;
        return true;
    }

    public void toggleListVisibility(boolean visible) {
        if (visible) {
            mArticlesRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mArticlesRecyclerView.setVisibility(View.GONE);
        }
    }

    public void openArticleDetailsFragment(Article article) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ArticleDetailsFragment articleDetailsFragment = ArticleDetailsFragment.getInstance(article);
        transaction.replace(R.id.container,
                articleDetailsFragment, ArticleDetailsFragment.class.getSimpleName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
