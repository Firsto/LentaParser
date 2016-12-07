package ru.firsto.lentaparser.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import ru.firsto.lentaparser.LentaParserApplication;
import ru.firsto.lentaparser.R;
import ru.firsto.lentaparser.di.component.ActivityComponent;
import ru.firsto.lentaparser.di.component.DaggerActivityComponent;
import ru.firsto.lentaparser.di.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(LentaParserApplication.get(this).component())
                .activityModule(new ActivityModule(this))
                .build();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
