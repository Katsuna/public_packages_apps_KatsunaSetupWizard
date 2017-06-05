package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.katsuna.commons.entities.Gender;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;

public class KatsunaGenderSetupPage extends SetupPage {

    public static final String TAG = "KatsunaGenderSetupPage";

    private GenderFragment mGenderFragment;

    protected KatsunaGenderSetupPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mGenderFragment = (GenderFragment) fragmentManager.findFragmentByTag(getKey());
        if (mGenderFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mGenderFragment = new GenderFragment();
            mGenderFragment.setArguments(args);
        }
        return mGenderFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.gender_setup;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class GenderFragment extends SetupPageFragment {

        private boolean mPaused = true;
        private RadioGroup mRadioGroupGender;
        private RadioButton mRadioMale;
        private RadioButton mRadioFemale;
        private RadioButton mRadioOther;
        private EditText mTextOtherGenderDescr;

        @Override
        protected void initializePage() {
            mRadioMale = (RadioButton) mRootView.findViewById(R.id.radio_gender_male);
            mRadioFemale = (RadioButton) mRootView.findViewById(R.id.radio_gender_female);
            mRadioOther = (RadioButton) mRootView.findViewById(R.id.radio_gender_other);
            mTextOtherGenderDescr = (EditText) mRootView.findViewById(R.id.text_other_gender_descr);
            mTextOtherGenderDescr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        updateOtherPreference();
                    }
                }
            });
            mRadioGroupGender = (RadioGroup) mRootView.findViewById(R.id.radio_group_gender);
            mRadioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    Preference preference = new Preference();
                    preference.setKey(PreferenceKey.GENDER);
                    switch (checkedId) {
                        case R.id.radio_gender_male:
                            preference.setValue(Gender.MALE.name());
                            mTextOtherGenderDescr.setVisibility(View.GONE);
                            mTextOtherGenderDescr.setText(null);
                            break;
                        case R.id.radio_gender_female:
                            preference.setValue(Gender.FEMALE.name());
                            mTextOtherGenderDescr.setVisibility(View.GONE);
                            mTextOtherGenderDescr.setText(null);
                            break;
                        case R.id.radio_gender_other:
                            preference.setValue(Gender.OTHER.name());
                            preference.setDescr(mTextOtherGenderDescr.getText().toString());
                            mTextOtherGenderDescr.setVisibility(View.VISIBLE);
                            break;
                    }
                    PreferenceUtils.updatePreference(getContext(), preference);
                }
            });

        }

        private void updateOtherPreference() {
            Preference preference = new Preference();
            preference.setKey(PreferenceKey.GENDER);
            preference.setValue(Gender.OTHER.name());
            preference.setDescr(mTextOtherGenderDescr.getText().toString());
            PreferenceUtils.updatePreference(getContext(), preference);
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_gender_setup_page;
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
            switch (profile.genderInfo.gender) {
                case MALE:
                    mRadioMale.setChecked(true);
                    mTextOtherGenderDescr.setVisibility(View.GONE);
                    break;
                case FEMALE:
                    mRadioFemale.setChecked(true);
                    mTextOtherGenderDescr.setVisibility(View.GONE);
                    break;
                case OTHER:
                    mRadioOther.setChecked(true);
                    mTextOtherGenderDescr.setText(profile.genderInfo.descr);
                    mTextOtherGenderDescr.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

}
