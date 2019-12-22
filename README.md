# Katsuna Setup Wizard

Katsuna setup wizard is an aosp app based on [Cyanogen Setup Wizard](https://github.com/CyanogenMod/android_packages_apps_SetupWizard)

This app is the first one that is launched on a new phone. 

It contains an End User License Agreement (EULA) for katsuna rom and functionality to setup katsuna user profile. It also contains steps to setup wifi connectivity and locale configuration.



## Technical Info

#### Katsuna pages

- package com.katsuna.setupwizard.setup
  - KatsunaWelcomePage
    - Welcome page.
  - KatsunaEulaPage
    - Eula acceptance page.
  - KatsunaAgeSetupPage
    - Page to setup the age of the user.
  - KatsunaColorSetupPage
    - Page to select color profile.
  - KatsunaSizeSetupPage
    - Page to select size profile.
  - KatsunaGenderSetupPage
    - Page to select gender.
  - KatsunaPermissionsPage
    - Page to ask the user for required permissions for certain katsuna apps.
  - HelpPages
    - KatsunaFirstHelpPage, KatsunaSecondHelpPage, KatsunaThirdHelpPage
    - Information and help pages.



#### Dependencies (added for katsuna port)

- This project (as any other Katsuna app) depends on KatsunaCommon project (dev branch) which is an android library module that contains common classes and resources for all katsuna projects.
- Katsuna Dialer requires KatsunaLauncher app because it contains the content provider that manages katsuna user profiles.
- [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)



## License

This project is licensed under the Apache 2.0 License.
