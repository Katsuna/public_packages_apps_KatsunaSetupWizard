package com.katsuna.setupwizard.setup;

import android.annotation.IdRes;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.katsuna.commons.entities.ColorProfile;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;
import com.katsuna.setupwizard.ui.SetupWizardActivity;

public class KatsunaColorSetupPage extends SetupPage {

    public static final String TAG = "KatsunaColorSetupPage";

    private ColorFragment mColorFragment;

    protected KatsunaColorSetupPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mColorFragment = (ColorFragment) fragmentManager.findFragmentByTag(getKey());
        if (mColorFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mColorFragment = new ColorFragment();
            mColorFragment.setArguments(args);
        }
        return mColorFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.color_setup;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class ColorFragment extends SetupPageFragment {

        private boolean mPaused = true;

        private RadioGroup mColorProfiles;
        private RadioButton mProfileMain;
        private RadioButton mProfileImpairement;
        private RadioButton mProfileContrast;
        private RadioButton mProfileContrastImpairement;

        @Override
        protected void initializePage() {
            mProfileMain = (RadioButton) mRootView.findViewById(R.id.profile_main);
            mProfileImpairement = (RadioButton) mRootView.findViewById(R.id.profile_impairement);
            mProfileContrast = (RadioButton) mRootView.findViewById(R.id.profile_contrast);
            mProfileContrastImpairement =
                    (RadioButton) mRootView.findViewById(R.id.profile_contrast_impairement);

            mColorProfiles = (RadioGroup) mRootView.findViewById(R.id.color_profiles);
            mColorProfiles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    Preference preference = new Preference();
                    preference.setKey(PreferenceKey.COLOR_PROFILE);
                    switch (checkedId) {
                        case R.id.profile_main:
                            preference.setValue(ColorProfile.MAIN.name());
                            break;
                        case R.id.profile_impairement:
                            preference.setValue(ColorProfile.COLOR_IMPAIREMENT.name());
                            break;
                        case R.id.profile_contrast:
                            preference.setValue(ColorProfile.CONTRAST.name());
                            break;
                        case R.id.profile_contrast_impairement:
                            preference.setValue(ColorProfile.COLOR_IMPAIRMENT_AND_CONTRAST.name());
                            break;
                    }
                    PreferenceUtils.updatePreference(getContext(), preference);
                    ((SetupWizardActivity) getActivity()).applyProfile();
                    loadProfile();
                }
            });
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_color_setup_page;
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
            switch (profile.colorProfile) {
                case MAIN:
                    mProfileMain.setChecked(true);
                    break;
                case COLOR_IMPAIREMENT:
                    mProfileImpairement.setChecked(true);
                    break;
                case CONTRAST:
                    mProfileContrast.setChecked(true);
                    break;
                case COLOR_IMPAIRMENT_AND_CONTRAST:
                    mProfileContrastImpairement.setChecked(true);
                    break;
            }

            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile,
                    mProfileMain);
            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile,
                    mProfileImpairement);
            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile,
                    mProfileContrast);
            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile,
                    mProfileContrastImpairement);
        }
    }

}
