LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE_TAGS := optional

LOCAL_PACKAGE_NAME := CyanogenSetupWizard
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-v4 \
    android-support-v13 \
    play \
    libphonenumber

LOCAL_STATIC_JAVA_AAR_LIBRARIES := cmsdk
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES = cmsdk:cm/libs/org.cyanogenmod.platform.sdk.aar

LOCAL_JAVA_LIBRARIES := telephony-common

# Include res dir from chips
google_play_dir := ../../../external/google/google_play_services/libproject/google-play-services_lib/res
res_dir := $(google_play_dir) res

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, $(res_dir))
LOCAL_AAPT_FLAGS := --auto-add-overlay
LOCAL_AAPT_FLAGS += --extra-packages com.google.android.gms
LOCAL_AAPT_FLAGS += --extra-packages org.cyanogenmod.platform.sdk

include frameworks/opt/setupwizard/library/common.mk

include $(BUILD_PACKAGE)

#######################################
include $(CLEAR_VARS)

include $(BUILD_MULTI_PREBUILT)