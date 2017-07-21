package com.katsuna.setupwizard.setup;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.katsuna.commons.KatsunaIntents;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.SetupWizardApp;
import com.katsuna.setupwizard.ui.LoadingFragment;
import com.katsuna.setupwizard.ui.SetupPageFragment;
import com.katsuna.setupwizard.util.SetupWizardUtils;

public class KatsunaPermissionsPage extends SetupPage {

    public static final int REQUEST_FOR_PERMISSIONS = 101;

    public static final String TAG = "KatsunaPermissionsPage";

    private LoadingFragment mLoadingFragment;
    private KatsunaPermissionsFragment mFragment;

    protected KatsunaPermissionsPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mFragment = (KatsunaPermissionsFragment) fragmentManager.findFragmentByTag(getKey());
        if (mFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mFragment = new KatsunaPermissionsFragment();
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
        return R.string.permissions_page;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class KatsunaPermissionsFragment extends SetupPageFragment {

        private Button mAcceptButton;

        @Override
        protected void initializePage() {
            mAcceptButton = (Button) mRootView.findViewById(R.id.accept_button);
            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(KatsunaIntents.PERMISSIONS_SERVICES);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setFlags(0);
                    startActivityForResult(i, KatsunaPermissionsPage.REQUEST_FOR_PERMISSIONS);
                }
            });

        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.e(TAG, "onActivityResult requestCode: " + requestCode + "resultCode:" + resultCode + "data: " + data );
            //super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_FOR_PERMISSIONS) {
                if (resultCode == Activity.RESULT_OK) {
                    boolean permissionsGranted = data.getBooleanExtra("permissionsGranted", true);
                    Log.e(TAG, "onActivityResult permissionsGranted: " + permissionsGranted);
                }
                Log.e(TAG, "onActivityResult requestCode: " + requestCode);
            }
        }


        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_permissions_page;
        }

    }

}
