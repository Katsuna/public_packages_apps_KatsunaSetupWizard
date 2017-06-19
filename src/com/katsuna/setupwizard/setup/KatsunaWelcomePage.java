package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaWelcomePage extends SetupPage {

    public static final String TAG = "KatsunaWelcomePage";

    private KatsunaWelcomeFragment mWelcomeFragment;

    protected KatsunaWelcomePage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mWelcomeFragment = (KatsunaWelcomeFragment) fragmentManager.findFragmentByTag(getKey());
        if (mWelcomeFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mWelcomeFragment = new KatsunaWelcomeFragment();
            mWelcomeFragment.setArguments(args);
        }
        return mWelcomeFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return 0;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class KatsunaWelcomeFragment extends SetupPageFragment {

        @Override
        protected void initializePage() {

        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_welcome_page;
        }

    }

}
