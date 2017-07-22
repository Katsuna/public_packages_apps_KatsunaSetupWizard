package com.katsuna.setupwizard.setup;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.katsuna.commons.KatsunaIntents;
import com.katsuna.commons.entities.ColorProfile;
import com.katsuna.commons.entities.ColorProfileKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorCalc;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.commons.utils.Shape;
import com.katsuna.setupwizard.R;
import com.katsuna.setupwizard.ui.LoadingFragment;
import com.katsuna.setupwizard.ui.SetupPageFragment;
import com.katsuna.setupwizard.ui.SetupWizardActivity;

public class KatsunaPermissionsPage extends SetupPage {

    public static final int REQUEST_FOR_PERMISSIONS_SERVICES = 101;
    public static final int REQUEST_FOR_PERMISSIONS_INFOSERVICES = 102;
    public static final int REQUEST_FOR_PERMISSIONS_WEATHER = 103;
    public static final String TAG = "KatsunaPermissionsPage";
    public static boolean PERMISSION_SERVICES_GRANTED = false;
    public static boolean PERMISSION_INFOSERVICES_GRANTED = false;
    public static boolean PERMISSION_WEATHER_GRANTED = false;
    private LoadingFragment mLoadingFragment;
    private KatsunaPermissionsFragment mFragment;

    protected KatsunaPermissionsPage(Context context, SetupDataCallbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    public boolean doPreviousAction() {
        mFragment.adjustActivityButtons(true);
        return super.doPreviousAction();
    }

    @Override
    public boolean doNextAction() {
        mFragment.adjustActivityButtons(true);
        return super.doNextAction();
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
        return R.string.permissions;
    }

    @Override
    public int getPrevButtonTitleResId() {
        return R.string.back;
    }

    public static class KatsunaPermissionsFragment extends SetupPageFragment {

        private Button mGrantToServicesButton;
        private Button mGrantToInfoServicesButton;
        private Button mGrantToWeatherButton;

        @Override
        protected void initializePage() {
            mGrantToServicesButton = (Button) mRootView.findViewById(R.id.grant_services);
            mGrantToServicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PERMISSION_SERVICES_GRANTED) return;

                    startActivity(KatsunaIntents.PERMISSIONS_SERVICES,
                            KatsunaPermissionsPage.REQUEST_FOR_PERMISSIONS_SERVICES);
                }
            });

            mGrantToInfoServicesButton = (Button) mRootView.findViewById(R.id.grant_infoservices);
            mGrantToInfoServicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PERMISSION_INFOSERVICES_GRANTED) return;

                    startActivity(KatsunaIntents.PERMISSIONS_INFOSERVICES,
                            KatsunaPermissionsPage.REQUEST_FOR_PERMISSIONS_INFOSERVICES);
                }
            });

            mGrantToWeatherButton = (Button) mRootView.findViewById(R.id.grant_weather);
            mGrantToWeatherButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PERMISSION_WEATHER_GRANTED) return;

                    startActivity(KatsunaIntents.PERMISSIONS_WIDGETS,
                            KatsunaPermissionsPage.REQUEST_FOR_PERMISSIONS_WEATHER);

                }
            });

            adjustProfile();
        }

        @Override
        public void onResume() {
            super.onResume();
            if (PERMISSION_SERVICES_GRANTED && PERMISSION_INFOSERVICES_GRANTED &&
                    PERMISSION_WEATHER_GRANTED)
            {
                adjustActivityButtons(true);
            } else {
                adjustActivityButtons(false);
            }
        }

        private void startActivity(String action, int requestCode) {
            // construct intent
            Intent intent = new Intent(action);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(0);

            // check if there is an activity to handle it
            PackageManager packageManager = getActivity().getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, requestCode);
            } else {
                Log.e(TAG, "No Intent available to handle action: " + action);
            }
        }

        private void adjustProfile() {
            Context context = getContext();
            UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(context);
            if (PERMISSION_SERVICES_GRANTED) {
                adjustButtonDisabled(mGrantToServicesButton, profile);
            } else {
                adjustButtonEnabled(mGrantToServicesButton, profile);
            }
            if (PERMISSION_INFOSERVICES_GRANTED) {
                adjustButtonDisabled(mGrantToInfoServicesButton, profile);
            } else {
                adjustButtonEnabled(mGrantToInfoServicesButton, profile);
            }
            if (PERMISSION_WEATHER_GRANTED) {
                adjustButtonDisabled(mGrantToWeatherButton, profile);
            } else {
                adjustButtonEnabled(mGrantToWeatherButton, profile);
            }
        }

        private void adjustButtonEnabled(Button button, UserProfile profile) {
            Context context = getContext();
            int color1 = ColorCalc.getColor(context, ColorProfileKey.ACCENT1_COLOR, profile.colorProfile);
            int color2 = ColorCalc.getColor(context, ColorProfileKey.ACCENT2_COLOR, profile.colorProfile);
            int whiteResId = ContextCompat.getColor(context, R.color.common_white);
            int black87ResId = ContextCompat.getColor(context, R.color.common_black87);

            if (profile.colorProfile == ColorProfile.CONTRAST) {
                Shape.setRoundedBackground(button, color1);
                button.setTextColor(color2);
            } else {
                Shape.setRoundedBackground(button, color1);
                button.setTextColor(black87ResId);
            }
        }

        private void adjustButtonDisabled(Button button, UserProfile profile) {
            Context context = getContext();

            button.setText(R.string.granted);

            int color1 = ColorCalc.getColor(context, ColorProfileKey.ACCENT1_COLOR, profile.colorProfile);
            int color2 = ColorCalc.getColor(context, ColorProfileKey.ACCENT2_COLOR, profile.colorProfile);
            int whiteResId = ContextCompat.getColor(context, R.color.common_white);
            int black87ResId = ContextCompat.getColor(context, R.color.common_black87);

            if (profile.colorProfile == ColorProfile.CONTRAST) {
                Shape.setRoundedBackground(button, color2);
                Shape.setRoundedBorder(button, color1);
                button.setTextColor(color1);
            } else {
                Shape.setRoundedBorder(button, color2);
                button.setTextColor(color2);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.e(TAG, "onActivityResult requestCode: " + requestCode + "resultCode:" + resultCode + "data: " + data);
            //super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                boolean permissionsGranted = data.getBooleanExtra("permissionsGranted", true);
                if (requestCode == REQUEST_FOR_PERMISSIONS_SERVICES) {
                    Log.d(TAG, "onActivityResult permissionsGranted for services");
                    if (permissionsGranted) {
                        Context context = getContext();
                        UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(context);
                        adjustButtonDisabled(mGrantToServicesButton, profile);
                        PERMISSION_SERVICES_GRANTED = true;
                    }
                } else if (requestCode == REQUEST_FOR_PERMISSIONS_INFOSERVICES) {
                    Log.d(TAG, "onActivityResult permissionsGranted for infoservices");
                    if (permissionsGranted) {
                        Context context = getContext();
                        UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(context);
                        adjustButtonDisabled(mGrantToInfoServicesButton, profile);
                        PERMISSION_INFOSERVICES_GRANTED = true;
                    }
                } else if (requestCode == REQUEST_FOR_PERMISSIONS_WEATHER) {
                    Log.d(TAG, "onActivityResult permissionsGranted for weather");
                    if (permissionsGranted) {
                        Context context = getContext();
                        UserProfile profile = ProfileReader.getUserProfileFromKatsunaServices(context);
                        adjustButtonDisabled(mGrantToWeatherButton, profile);
                        PERMISSION_WEATHER_GRANTED = true;
                    }
                }

                if (PERMISSION_SERVICES_GRANTED && PERMISSION_INFOSERVICES_GRANTED &&
                        PERMISSION_WEATHER_GRANTED)
                {
                    adjustActivityButtons(true);
                } else {
                    adjustActivityButtons(false);
                }
            }
        }

        public void adjustActivityButtons(boolean flag) {
            if (flag) {
                ((SetupWizardActivity) getActivity()).applyProfile();
            } else {
                ((SetupWizardActivity) getActivity()).applyProfileForPermissionsPage();
            }
        }


        @Override
        protected int getLayoutResource() {
            return R.layout.katsuna_permissions_page;
        }

    }

}
