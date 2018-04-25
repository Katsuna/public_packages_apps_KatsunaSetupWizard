package com.katsuna.setupwizard.setup;

import android.annotation.IdRes;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.SizeProfile;
import com.katsuna.commons.entities.SizeProfileKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.profile.Adjuster;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalc;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaSizeSetupPage extends SetupPage {

    public static final String TAG = "KatsunaSizeSetupPage";

    private SizeFragment mSizeFragment;

    protected KatsunaSizeSetupPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mSizeFragment = (SizeFragment) fragmentManager.findFragmentByTag(getKey());
        if (mSizeFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mSizeFragment = new SizeFragment();
            mSizeFragment.setArguments(args);
        }
        return mSizeFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.common_size;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class SizeFragment extends SetupPageFragment {

        private boolean mPaused = true;
        private RadioGroup mSizeProfiles;
        private RadioButton mAdvanced;
        private RadioButton mIntermediate;
        private RadioButton mSimple;
        private TextView mIthaca;
        private TextView mIthacaFull;
        private View mFabSample;
        private TextView mFabSampleText;

        @Override
        protected void initializePage() {
            mIthaca = (TextView) mRootView.findViewById(R.id.text_ithaca);
            mIthacaFull = (TextView) mRootView.findViewById(R.id.text_ithaca_full);

            mAdvanced = (RadioButton) mRootView.findViewById(R.id.radio_size_advanced);
            mIntermediate = (RadioButton) mRootView.findViewById(R.id.radio_size_intermediate);
            mSimple = (RadioButton) mRootView.findViewById(R.id.radio_size_simple);
            mSizeProfiles = (RadioGroup) mRootView.findViewById(R.id.radio_group_size);
            mSizeProfiles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    Preference preference = new Preference();
                    preference.setKey(PreferenceKey.OPTICAL_SIZE_PROFILE);
                    SizeProfile sizeProfile = SizeProfile.INTERMEDIATE;
                    switch (checkedId) {
                        case R.id.radio_size_advanced:
                            sizeProfile = SizeProfile.ADVANCED;
                            break;
                        case R.id.radio_size_intermediate:
                            sizeProfile = SizeProfile.INTERMEDIATE;
                            break;
                        case R.id.radio_size_simple:
                            sizeProfile = SizeProfile.SIMPLE;
                            break;
                    }
                    preference.setValue(sizeProfile.name());
                    PreferenceUtils.updatePreference(getContext(), preference);

                    adjustTextSamples(sizeProfile);
                }
            });

            mFabSample = mRootView.findViewById(R.id.commom_size_sample_fab);
            mFabSampleText = (TextView) mRootView.findViewById(R.id.commom_size_sample_fab_text);
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_size_setup_page;
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
            switch (profile.opticalSizeProfile) {
                case SIMPLE:
                    mSimple.setChecked(true);
                    break;
                case INTERMEDIATE:
                    mIntermediate.setChecked(true);
                    break;
                case ADVANCED:
                    mAdvanced.setChecked(true);
                    break;
            }

            adjustTextSamples(profile.opticalSizeProfile);

            Adjuster adjuster = new Adjuster(getContext(), profile);
            adjuster.adjustFabSampleSize(mFabSample, mFabSampleText);
            adjuster.adjustFabSample(mFabSample, mFabSampleText);


            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile, mAdvanced, 1,
                    false);
            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile, mIntermediate, 1,
                    false);
            ColorAdjusterV2.adjustRadioButton(getContext(), profile.colorProfile, mSimple, 1,
                    false);
        }

        private void adjustTextSamples(SizeProfile sizeProfile) {
            OpticalParams tvParams = SizeCalc.getOpticalParams(SizeProfileKey.TITLE, sizeProfile);
            SizeAdjuster.adjustText(getContext(), mIthaca, tvParams);

            tvParams = SizeCalc.getOpticalParams(SizeProfileKey.BODY_1, sizeProfile);
            SizeAdjuster.adjustText(getContext(), mIthacaFull, tvParams);

            UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(getContext());
            Adjuster adjuster = new Adjuster(getContext(), profile);
            adjuster.adjustFabSampleSize(mFabSample, mFabSampleText);
        }
    }

}
