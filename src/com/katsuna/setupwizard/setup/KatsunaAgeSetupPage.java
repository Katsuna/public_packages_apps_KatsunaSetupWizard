package com.katsuna.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.AlertUtils;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.SetupPageFragment;
import com.katsuna.setupwizard.ui.SetupWizardActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KatsunaAgeSetupPage extends SetupPage {

    public static final String TAG = "KatsunaAgeSetupPage";

    private AgeFragment mAgeFragment;

    protected KatsunaAgeSetupPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public boolean doNextAction() {
        if (mAgeFragment.isValid()) {

            Date date = mAgeFragment.getDate();

            String dateStr = "";
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = dateFormat.format(date);
            }

            Preference preference = new Preference(PreferenceKey.AGE, dateStr);
            PreferenceUtils.updatePreference(mContext, preference);

            return super.doNextAction();
        } else {
            ((SetupWizardActivity) mAgeFragment.getActivity()).enableButtonBar(true);
            return true;
        }
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        mAgeFragment = (AgeFragment) fragmentManager.findFragmentByTag(getKey());
        if (mAgeFragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            mAgeFragment = new AgeFragment();
            mAgeFragment.setArguments(args);
        }
        return mAgeFragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.age_setup;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class AgeFragment extends SetupPageFragment {

        private boolean mPaused = true;
        private EditText mDay;
        private EditText mMonth;
        private EditText mYear;
        private TextView mValidationError;
        private Date mDate;

        @Override
        protected void initializePage() {
            mDay = (EditText) mRootView.findViewById(R.id.day);
            mMonth = (EditText) mRootView.findViewById(R.id.month);
            mYear = (EditText) mRootView.findViewById(R.id.year);
            mValidationError = (TextView) mRootView.findViewById(R.id.validation_error);
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_age_setup_page;
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
            if (profile.age != null && !profile.age.equals("")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(profile.age);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int year = cal.get(Calendar.YEAR);


                    mYear.setText(String.valueOf(year));
                    mMonth.setText(String.valueOf(month));
                    mDay.setText(String.valueOf(day));
                } catch (ParseException e) {
                    Log.e(TAG, e.toString());
                }
            }

            AlertUtils.createListAlert(getContext(), mDay, null, R.string.common_select_day,
                    profile, AlertUtils.getDays(), null, null);
            AlertUtils.createListAlert(getContext(), mMonth, null, R.string.common_select_month,
                    profile, AlertUtils.getMonths(), AlertUtils.getMonthsLabels(), null);
            AlertUtils.createListAlert(getContext(), mYear, "1955", R.string.common_select_year,
                    profile, AlertUtils.getYears(), null, null);

        }

        private String getDateString() {
            String day = mDay.getText().toString();
            String month = mMonth.getText().toString();
            String year = mYear.getText().toString();
            return year + "-" + month + "-" + day;
        }

        public Date getDate() {
            return mDate;
        }

        private boolean dateNotEntered() {
            return (TextUtils.isEmpty(mDay.getText()) && TextUtils.isEmpty(mMonth.getText()) &&
                    TextUtils.isEmpty(mYear.getText()));
        }


        public boolean isValid() {
            mValidationError.setVisibility(View.GONE);

            if (dateNotEntered()) {
                mDate = null;
                return true;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            try {
                mDate = dateFormat.parse(getDateString());
            } catch (ParseException pe) {
                Log.e(TAG, pe.toString());
                mValidationError.setVisibility(View.VISIBLE);
                return false;
            }

            return true;
        }
    }

}
