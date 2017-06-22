package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaSecondHelpPage extends SetupPage {

    public static final String TAG = "KatsunaSecondHelpPage";

    private KatsunaSecondHelpFragment mFragment;

    protected KatsunaSecondHelpPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mFragment = (KatsunaSecondHelpFragment) fragmentManager.findFragmentByTag(getKey());
        if (mFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mFragment = new KatsunaSecondHelpFragment();
            mFragment.setArguments(args);
        }
        return mFragment;
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

    public static class KatsunaSecondHelpFragment extends SetupPageFragment {

        @Override
        protected void initializePage() {

        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_second_help_page;
        }

    }

}
