/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.setupwizard.setup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.IWindowManager;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyanogenmod.setupwizard.R;
import com.cyanogenmod.setupwizard.SetupWizardApp;
import com.cyanogenmod.setupwizard.cmstats.SetupStats;
import com.cyanogenmod.setupwizard.ui.SetupPageFragment;
import com.cyanogenmod.setupwizard.util.SetupWizardUtils;

public class CyanogenSettingsPage extends SetupPage {

    public static final String TAG = "CyanogenSettingsPage";

    public static final String KEY_SEND_METRICS = "send_metrics";

    public static final String PRIVACY_POLICY_URI = "https://cyngn.com/oobe-legal?hideHeader=1";

    public CyanogenSettingsPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public Fragment getFragment(FragmentManager fragmentManager, int action) {
        Fragment fragment = fragmentManager.findFragmentByTag(getKey());
        if (fragment == null) {
            Bundle args = new Bundle();
            args.putString(Page.KEY_PAGE_ARGUMENT, getKey());
            args.putInt(Page.KEY_PAGE_ACTION, action);
            fragment = new CyanogenSettingsFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public String getKey() {
        return TAG;
    }

    @Override
    public int getTitleResId() {
        return R.string.setup_services;
    }

    @Override
    public void onFinishSetup() {
        handleEnableMetrics();
    }

    private void handleEnableMetrics() {
        Bundle privacyData = getData();
        if (privacyData != null
                && privacyData.containsKey(KEY_SEND_METRICS)) {
            /*CMSettings.Secure.putInt(mContext.getContentResolver(),
                    CMSettings.Secure.STATS_COLLECTION, privacyData.getBoolean(KEY_SEND_METRICS)
                            ? 1 : 0);*/
        }
    }

    public static class CyanogenSettingsFragment extends SetupPageFragment {

        private View mMetricsRow;
        private CheckBox mMetrics;


        private View.OnClickListener mMetricsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = !mMetrics.isChecked();
                mMetrics.setChecked(checked);
                mPage.getData().putBoolean(KEY_SEND_METRICS, checked);
            }
        };

        @Override
        protected void initializePage() {
            String privacy_policy = getString(R.string.services_privacy_policy);
            String policySummary = getString(R.string.services_explanation, privacy_policy);
            SpannableString ss = new SpannableString(policySummary);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    final Intent intent = new Intent(SetupWizardApp.ACTION_VIEW_LEGAL);
                    intent.setData(Uri.parse(PRIVACY_POLICY_URI));
                    try {
                        getActivity().startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Unable to start activity " + intent.toString(), e);
                    }
                }
            };
            ss.setSpan(clickableSpan,
                    policySummary.length() - privacy_policy.length() - 1,
                    policySummary.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView privacyPolicy = (TextView) mRootView.findViewById(R.id.privacy_policy);
            privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
            privacyPolicy.setText(ss);

            mMetricsRow = mRootView.findViewById(R.id.metrics);
            mMetricsRow.setOnClickListener(mMetricsClickListener);
            String metricsHelpImproveCM =
                    getString(R.string.services_help_improve_cm, getString(R.string.os_name));
            String metricsSummary = getString(R.string.services_metrics_label,
                    metricsHelpImproveCM, getString(R.string.os_name));
            final SpannableStringBuilder metricsSpan = new SpannableStringBuilder(metricsSummary);
            metricsSpan.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, metricsHelpImproveCM.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView metrics = (TextView) mRootView.findViewById(R.id.enable_metrics_summary);
            metrics.setText(metricsSpan);
            mMetrics = (CheckBox) mRootView.findViewById(R.id.enable_metrics_checkbox);
        }

        @Override
        protected int getLayoutResource() {
            return R.layout.setup_cyanogen_services;
        }

        @Override
        public void onResume() {
            super.onResume();
            updateMetricsOption();
        }

        private void updateMetricsOption() {
            final Bundle myPageBundle = mPage.getData();
            boolean metricsChecked =
                    !myPageBundle.containsKey(KEY_SEND_METRICS) || myPageBundle
                            .getBoolean(KEY_SEND_METRICS);
            mMetrics.setChecked(metricsChecked);
            myPageBundle.putBoolean(KEY_SEND_METRICS, metricsChecked);
        }
    }
}
