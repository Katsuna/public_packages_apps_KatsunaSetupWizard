package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.katsuna.commons.controls.InteractiveScrollView;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;
import com.katsuna.setupwizard.ui.SetupWizardActivity;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class KatsunaEulaPage extends SetupPage {

    public static final String TAG = "KatsunaEulaPage";

    private KatsunaEulaFragment mEulaFragment;

    protected KatsunaEulaPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mEulaFragment = (KatsunaEulaFragment) fragmentManager.findFragmentByTag(getKey());
        if (mEulaFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mEulaFragment = new KatsunaEulaFragment();
            mEulaFragment.setArguments(args);
        }
        return mEulaFragment;
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

    @Override
    public int getNextButtonTitleResId() {
        return R.string.accept;
    }

    @Override
    public boolean doNextAction() {
        if (mEulaFragment.mEulaRead) {

            // Persist eula acceptance time at file.
            // Location saved: /data/data/com.katsuna.setupwizard/files
            String filename = "eula_acceptance.txt";
            String timestamp = new Date().toString();

            FileOutputStream fos = null;
            try {
                fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(timestamp.getBytes());
                fos.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            return super.doNextAction();
        } else {
            Toast.makeText(mContext, R.string.scroll_down_advice, Toast.LENGTH_SHORT).show();
            ((SetupWizardActivity) mEulaFragment.getActivity()).enableButtonBar(true);
            return true;
        }
    }

    public static class KatsunaEulaFragment extends SetupPageFragment {

        private TextView mEula;
        private TextView mScrollDown;
        private InteractiveScrollView mEulaContainer;
        private boolean mEulaRead;

        @Override
        protected void initializePage() {
            mEula = (TextView) mRootView.findViewById(R.id.eula_full);
            mEula.setText(readEula());

            mScrollDown = (TextView) mRootView.findViewById(R.id.scroll_down);

            mEulaContainer = (InteractiveScrollView) mRootView.findViewById(R.id.eula_container);
            mEulaContainer.setOnBottomReachedListener(new InteractiveScrollView.OnBottomReachedListener() {
                @Override
                public void onBottomReached() {
                    if (!mEulaRead) {
                        mEulaRead = true;
                        mScrollDown.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        private String readEula() {
            StringBuilder buf = new StringBuilder();

            try {
                //store localized eula at locations like raw-el for greek
                InputStream is = getContext().getResources().openRawResource(R.raw.eula);
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String str;

                while ((str = in.readLine()) != null) {
                    buf.append(str);
                    buf.append("\n");
                }
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            return buf.toString();
        }


        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_eula_page;
        }

    }

}
