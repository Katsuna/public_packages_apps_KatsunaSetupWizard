package com.katsuna.setupwizard.setup;

import android.annotation.IdRes;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaHandSetupPage extends SetupPage {

    public static final String TAG = "KatsunaHandSetupPage";

    private HandFragment mHandFragment;

    protected KatsunaHandSetupPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mHandFragment = (HandFragment) fragmentManager.findFragmentByTag(getKey());
        if (mHandFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mHandFragment = new HandFragment();
            mHandFragment.setArguments(args);
        }
        return mHandFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.hand_setup;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class HandFragment extends SetupPageFragment {

        private boolean mPaused = true;
        private RadioGroup mHandProfiles;
        private RadioButton mRightHand;
        private RadioButton mLeftHand;

        @Override
        protected void initializePage() {
            mRightHand = (RadioButton) mRootView.findViewById(R.id.radio_right_hand);
            mLeftHand = (RadioButton) mRootView.findViewById(R.id.radio_left_hand);
            mHandProfiles = (RadioGroup) mRootView.findViewById(R.id.hand_profiles);
            mHandProfiles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    Preference preference = new Preference();
                    preference.setKey(PreferenceKey.RIGHT_HAND);
                    if (checkedId == R.id.radio_right_hand) {
                        preference.setValue(String.valueOf(true));
                    } else {
                        preference.setValue(String.valueOf(false));
                    }
                    PreferenceUtils.updatePreference(getContext(), preference);
                }
            });
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_hand_setup_page;
        }

        @Override
        public void onPause() {
            super.onPause();
            mPaused = true;
        }

        @Override
        public void onResume() {
            super.onResume();
            mPaused = false;
            loadProfile();
        }

        private void loadProfile() {
            UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(getContext());
            if (profile.isRightHanded) {
                mRightHand.setChecked(true);
            } else {
                mLeftHand.setChecked(true);
            }
        }
    }

}
