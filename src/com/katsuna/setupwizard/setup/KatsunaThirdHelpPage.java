package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaThirdHelpPage extends SetupPage {

    public static final String TAG = "KatsunaThirdHelpPage";

    private KatsunaThirdHelpFragment mFragment;

    protected KatsunaThirdHelpPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mFragment = (KatsunaThirdHelpFragment) fragmentManager.findFragmentByTag(getKey());
        if (mFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mFragment = new KatsunaThirdHelpFragment();
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

    public static class KatsunaThirdHelpFragment extends SetupPageFragment {

        @Override
        protected void initializePage() {

        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_third_help_page;
        }

    }

}
